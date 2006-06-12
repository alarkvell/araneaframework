/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package org.araneaframework.framework.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.Custom;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.EmptyCallStackException;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.core.PopupFlowPseudoWidget;

/**
 * A {@link org.araneaframework.framework.FlowContext} where the flows are structured as a stack.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardFlowContainerWidget extends StandardWidget implements FlowContext {
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  private static final Logger log = Logger.getLogger(StandardFlowContainerWidget.class);
  
  /**
   * The key used for the CallStack in the OutputData attribute set.
   */
  public static final String CALL_STACK_KEY = "org.araneaframework.framework.container.StandardCallStackWidget.CALL_STACK";
  /**
   * The key of the callable child.
   */
  public static final String FLOW_KEY = "flow"; 
  
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  /**
   * The stack of all the calls.
   */
  protected LinkedList callStack = new LinkedList();
  /**
   * The top callable widget.
   */
  protected Widget top;
  protected FlowContext.Configurator topConfigurator;
  protected FlowContext.Handler topHandler;
  
  private Map nestedEnvironmentEntries = new HashMap();
  private Map nestedEnvEntryStacks = new HashMap();

  //*******************************************************************
  // CONSTRUCTORS
  //*******************************************************************
  
  /**
   * Constructs a StandardCallStackWidget with topWidget being the first
   * callable on the stack.
   */
  public StandardFlowContainerWidget(Widget topWidget) {
    this.top = topWidget;
  }
  
  /**
   * TODO: javadoc
   */
  public StandardFlowContainerWidget(Widget topWidget, FlowContext.Configurator configurator, FlowContext.Handler handler) {
    this.top = topWidget;
    this.topConfigurator = configurator;
    this.topHandler = handler;
  }
  
  public StandardFlowContainerWidget() {
  }
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  
  public void setTop(Widget topWidget) {
    this.top = topWidget;
  }
  
  public void setTopConfigurator(FlowContext.Configurator topConfigurator) {
    this.topConfigurator = topConfigurator;
  }

  public void setTopHandler(FlowContext.Handler topHandler) {
    this.topHandler = topHandler;
  }

  public void start(Widget flow, Configurator configurator, Handler handler) {
    flow = decorateCallableWidget((Widget) flow);
    CallFrame frame = makeCallFrame((Widget) flow, configurator, handler);
    
    log.debug("Starting flow '" + flow.getClass().getName() +"'");
    
    if (_getChildren().get(FLOW_KEY) != null) {
      ((Widget) getChildren().get(FLOW_KEY))._getComponent().disable();      
      _getChildren().remove(FLOW_KEY);
    }  
    
    callStack.addFirst(frame);
    
    addWidget(FLOW_KEY, (Widget) flow);

    if (configurator != null) {
      try {
        configurator.configure(flow);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }    
  }

  public void start(PopupFlowPseudoWidget flow, Configurator configurator, Handler handler) {
    flow.getPopupContext().open(flow.getMessage(), flow.getPopupWindowProperties(), flow.getOpener());
    flow.setConfigurator(configurator);
    flow.setHandler(handler);

    if (configurator != null) {
	  try {
        configurator.configure(flow.getWidget());
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }
  
  public void replace(Widget flow, Configurator configurator) {
    flow = decorateCallableWidget((Widget) flow);
    CallFrame previousFrame = (CallFrame) callStack.removeFirst();
    CallFrame frame = makeCallFrame((Widget) flow, configurator, previousFrame.getHandler());
    
    log.debug("Replacing flow '" + previousFrame.getWidget().getClass().getName() + 
        "' with flow '" + flow.getClass().getName() + "'");
    
    removeWidget(FLOW_KEY);
    
    callStack.addFirst(frame);    
    
    addWidget(FLOW_KEY, (Widget) flow);
    
    if (configurator != null) {
      try {
        configurator.configure(flow);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  public void finish(Object returnValue) {
    if (callStack.size() == 0)
      throw new EmptyCallStackException();
    
    CallFrame previousFrame = (CallFrame) callStack.removeFirst();
    CallFrame frame = callStack.size() > 0 ? (CallFrame) callStack.getFirst() : null;
    
    log.debug("Finishing flow '" + previousFrame.getWidget().getClass().getName() + "'");
    
    removeWidget(FLOW_KEY);
    if (frame != null) {
      _getChildren().put(FLOW_KEY, frame.getWidget());
      ((Component) getChildren().get(FLOW_KEY))._getComponent().enable();
    }
    
    if (previousFrame.getHandler() != null) {
      try {
        previousFrame.getHandler().onFinish(returnValue);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }                
  }

  public void cancel() {
    if (callStack.size() == 0)
      throw new EmptyCallStackException();
    
    CallFrame previousFrame = (CallFrame) callStack.removeFirst();
    CallFrame frame = callStack.size() > 0 ? (CallFrame) callStack.getFirst() : null;
    
    log.debug("Cancelling flow '" + previousFrame.getWidget().getClass().getName() + "'");
    
    removeWidget(FLOW_KEY);
    if (frame != null) {
      _getChildren().put(FLOW_KEY, frame.getWidget());    
      ((Component) getChildren().get(FLOW_KEY))._getComponent().enable();
    }
    
    if (previousFrame.getHandler() != null) try {
      previousFrame.getHandler().onCancel();
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }   
  }
  
  public FlowContext.FlowReference getCurrentReference() {
  	return new FlowReference();
  }
  
  private LinkedList getEnvEntryStack(Object entryId) {
    LinkedList envEntryStack = (LinkedList) nestedEnvEntryStacks.get(entryId);
    
    if (envEntryStack == null) {
      envEntryStack = new LinkedList();
      nestedEnvEntryStacks.put(entryId, envEntryStack);
    }
    
    return envEntryStack;
  }
  
  private void pushGlobalEnvEntry(Object entryId, Object envEntry) {
    getEnvEntryStack(entryId).addFirst(envEntry);
    
    refreshGlobalEnvironment();
  }
  
  private void popGlobalEnvEntry(Object entryId) {
    getEnvEntryStack(entryId).removeFirst();
    
    refreshGlobalEnvironment();
  }
  
  public void addNestedEnvironmentEntry(Custom.CustomWidget scope, final Object entryId, Object envEntry) {
    pushGlobalEnvEntry(entryId, envEntry);
    
    BaseWidget scopeWidget = new BaseWidget() {
      protected void destroy() throws Exception {
        popGlobalEnvEntry(entryId);
      }
    };
    ComponentUtil.addListenerComponent(scope, scopeWidget);
  }
  
  public boolean isNested() {
    return callStack.size() != 0;
  }
  
  public void reset(final EnvironmentAwareCallback callback) {   
    log.debug("Resetting all flows in '" + getClass().getName() + "'");
    
    for (Iterator i = callStack.iterator(); i.hasNext();) {
      CallFrame frame = (CallFrame) i.next();
      
      _getChildren().put(FLOW_KEY, frame.getWidget());
      removeWidget(FLOW_KEY);
    }
    
    callStack.clear();
    
    if (callback != null) try {
      callback.call(getChildWidgetEnvironment());
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }
  
  public void setTitle(String titleLabelId) throws Exception {    
    ((CallFrame) callStack.getFirst()).setTitle(titleLabelId);
  }
  
  
  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************
  
  protected void init() throws Exception {
    super.init();
            
    if (top != null)
      start(top, topConfigurator, topHandler);
  }
  
  protected void destroy() throws Exception {
    if (callStack.size() > 0)
      callStack.removeFirst();
    
    for (Iterator i = callStack.iterator(); i.hasNext();) {
      CallFrame frame = (CallFrame) i.next();
      i.remove();
      
      frame.getWidget()._getComponent().destroy();          
    }
    
    super.destroy();    
  }
  
  /**
   * Invokes render on the top frame on the stack of callframes.
   */
  protected void render(OutputData output) throws Exception {
    //Don't render empty callstack
    if (getCallStack().size() == 0) return; 
    
    output.pushAttribute(CALL_STACK_KEY, callStack);
      
    try {          
      output.pushScope(FLOW_KEY);
      
      try {   
        getWidget(FLOW_KEY)._getWidget().render(output);
      } 
      finally {
        output.popScope();
      }
    }
    finally {       
      output.popAttribute(CALL_STACK_KEY);
    }
  }
  
  private void refreshGlobalEnvironment() {
    nestedEnvironmentEntries.clear();
    
    nestedEnvironmentEntries.put(FlowContext.class, this);

    for (Iterator i = nestedEnvEntryStacks.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      Object entryId = entry.getKey();
      LinkedList stack = (LinkedList) entry.getValue();
      if (stack.size() > 0) {
        Object envEntry = stack.getFirst();
        nestedEnvironmentEntries.put(entryId, envEntry);
      }
    }    
  }
  
  protected Environment getChildWidgetEnvironment() throws Exception {
    refreshGlobalEnvironment();   
    
    return new StandardEnvironment(getEnvironment(), nestedEnvironmentEntries);
  }
  
  /**
   * Returns a new CallFrame constructed of the callable, configurator and handler.
   */
  protected CallFrame makeCallFrame(Widget callable, Configurator configurator, Handler handler) {
    return new CallFrame(callable, configurator, handler);
  }
  
  /**
   * This method may be overidden to decorate the called widget. 
   */
  protected Widget decorateCallableWidget(Widget widget) {
    return widget;
  }
  
  //*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************
  
  protected class FlowReference implements FlowContext.FlowReference {
  	private int currentDepth = StandardFlowContainerWidget.this.callStack.size();
  	
		public void reset(EnvironmentAwareCallback callback) throws Exception {
			Iterator i = callStack.iterator();
      while (i.hasNext() && callStack.size() > currentDepth) {
        CallFrame frame = (CallFrame) i.next();
        
        _getChildren().put(FLOW_KEY, frame.getWidget());
        removeWidget(FLOW_KEY);
        
        i.remove();
      }
      
      if (callStack.size() > 0) {
        CallFrame frame = (CallFrame) callStack.getFirst();
        _getChildren().put(FLOW_KEY, frame.getWidget());
        ((Component) getChildren().get(FLOW_KEY))._getComponent().enable();
      }

      callback.call(getChildWidgetEnvironment());
		}  	
  }
  
  /**
   * A widget, configurator and a handler are encapsulated into one logical structure,
   * a call frame. Class is used internally.
   */
  protected static class CallFrame implements Serializable {
    Widget widget;
    Configurator configurator;
    Handler handler;
    String title;
    
    protected CallFrame(Widget widget, Configurator configurator, Handler handler) {
      this.configurator = configurator;
      this.handler = handler;
      this.widget = widget;
    }

    public Configurator getConfigurator() {
      return configurator;
    }

    public Handler getHandler() {
      return handler;
    }

    public Widget getWidget() {
      return widget;
    }

    protected String getTitle() {
      return title;
    }

    protected void setTitle(String title) {
      this.title = title;
    }
  }

  protected LinkedList getCallStack() {
    return callStack;
  }
}
