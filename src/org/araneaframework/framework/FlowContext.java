/*
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
 */

package org.araneaframework.framework;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.Closure;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;

/**
 * This context provides support for flow navigation and nesting. A flow is started using 
 * {@link #start(Widget, org.araneaframework.framework.FlowContext.Configurator, org.araneaframework.framework.FlowContext.Handler)}
 * and continues to be active until it explicitly returns control to the caller using {@link #finish(Object)} or
 * {@link #cancel()}. 
 * 
 * @see org.araneaframework.framework.container.StandardFlowContainerWidget
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface FlowContext extends Serializable {
  public final static String PROPERTY__AUTOCONFIRM_ID = "AUTOCONFIRM_ID";
  /** @since 1.1 */ 
  int TRANSITION_START = 1;
  /** @since 1.1 */
  int TRANSITION_FINISH = 2;
  /** @since 1.1 */
  int TRANSITION_CANCEL = 3;
  /** @since 1.1 */
  int TRANSITION_REPLACE = 4;
  /** @since 1.1 */
  int TRANSITION_RESET = 5;

  /** 
   * Starts a new nested subflow. Current flow becomes inactive untils subflow calls {@link #finish(Object)} or 
   * {@link #cancel()}.
   * @since 1.0.9
   */
  public void start(Widget flow);

  /**
   * Starts a new nested subflow. Current flow becomes inactive untils subflow calls {@link #finish(Object)} or 
   * {@link #cancel()}. {@link Handler} allows to receive notification, when the subflow ends execution.
   * @since 1.0.9
   */
  public void start(Widget flow, Handler<?> handler);
  
  /**
   * Starts a new nested subflow, that can be configured using the configurator. Current flow becomes inactive
   * untils subflow calls {@link #finish(Object)} or {@link #cancel()}. {@link Handler} allows to receive notification,
   * when the subflow ends execution.
   */
  public void start(Widget flow, Configurator configurator, Handler<?> handler);

  /**
   * Destroys the current flow and starts a new one. When the new flow will end execution it will return control
   * to the caller of the current flow (if there is one). 
   */
  public void replace(Widget flow);
  
  /**
   * Destroys the current flow and starts a new one. When the new flow ends execution it will return control
   * to the caller of the current flow (if there is one). Started subflow can be configured using the configurator.
   * @since 1.0.9 
   */
  public void replace(Widget flow, Configurator configurator);

  /**
   * Finishes the current flow passing control back to the calling flow. Optionally may return some value that 
   * can be interpreted by the calling flow as the result of the call.
   */
  public void finish(Object result);
  
  /**
   * Finishes the current flow passing control back to the calling flow. 
   * Should be interpreted by the calling flow as a unsuccessful return. 
   */
  public void cancel();  
  
  /**
   * Returns whether the current flow is nested, that is has a caller flow.
   */
  public boolean isNested();
  
  /**
   * Resets all currently running flows and calls the <code>callback</code> allowing to start 
   * new flows. Useful e.g. in a menu, when selecting a new menu item and reseting the old
   * stack. 
   */
  public void reset(EnvironmentAwareCallback callback);
  
//  /**
//   * Returns a reference to the current flow that can be used later to manipulate the current flow.
//   * @deprecated to be removed in Aranea 2.0. Also see {@link FlowReference}
//   */
//  public FlowReference getCurrentReference();

  /**
   * Adds an environment entry that is visible in all subflows.
   */
  public <T> void addNestedEnvironmentEntry(ApplicationWidget scope, final Class<T> entryId, T envEntry);

//  /** 
//   * This is unused -- only implementation is a protected class StandardFlowContainerWidget.FlowReference
//   * FlowReference.reset() is not called from anywhere and is duplicate of FlowContext.reset() anyway.
//   * @deprecated to be removed in Aranea 2.0 
//   */
//  public interface FlowReference extends Serializable {
//    /**
//     * Resets the flow stack up to the referred flow and provides the callback with the local environment
//     * that can be used to manipulate the flow stack further.
//     */
//    public void reset(EnvironmentAwareCallback callback) throws Exception;
//  }

  /**
   * Sets the <code>FlowContext.TransitionHandler</code> which performs the
   * flow navigation.
   * 
   * @since 1.1
   */ 
  void setTransitionHandler(TransitionHandler handler);

  /**
   * Returns currently active <code>FlowContext.TransitionHandler</code>. If the
   * most current child is a {@link FlowContextWidget}, it will take its
   * currenty active <code>FlowContext.TransitionHandler</code> (recursively)
   * (since 1.2.2).
   * 
   * @since 1.1
   */
  TransitionHandler getTransitionHandler();

  /**
   * Callback that will be run when flow has finished some way. 
   */
  public interface Handler<T> extends Serializable {
    public void onFinish(T returnValue);   
    public void onCancel();
  }
  
  /**
   * Configurator runs when {@link FlowContext} starts flow.
   */
  public interface Configurator extends Serializable {
    public void configure(Widget flow) throws Exception;
  }
  
  /**
   * Performs the flow transitions in {@link FlowContext}.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.1
   */
  interface TransitionHandler extends Serializable {

    /**
     * The implementation should handle the transition with given data.
     * @param eventType <code>FlowContext.START<code> .. <code>FlowContext.RESET<code>
     * @param activeFlow active flow at the moment of transition request
     * @param transition <code>Serializable</code> closure that needs to be executed for transition to happen
     */
    void doTransition(int eventType, Widget activeFlow, Closure transition);
  }
  
  /**
   * Sets the title of total page.
   */
  public void setPageTitle(String pageTitle);
  /**
   * Sets the title of current page.
   */
  public void setComponentTitle(String componentTitle);
  /**
   * Returns title of current page.
   */
  public String getPageTitle();
  /**
   * Returns title of current component.
   */
  public String getComponentTitle();

  /**
   * @return all component titles in current flow call stack
   */
  List<String> getComponentTitles();
  
  public Map<String, Object> getProperties();

  public void putProperty(String key, Object value);
  
  public void removeProperty(String key);
}
