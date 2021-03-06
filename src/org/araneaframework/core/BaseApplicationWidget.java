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

package org.araneaframework.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Scope;
import org.araneaframework.Service;
import org.araneaframework.Viewable;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A full featured Widget with support for composite, event-listeners, view-model.
 */
public class BaseApplicationWidget extends BaseWidget implements ApplicationWidget {

  private static final Log LOG = LogFactory.getLog(BaseApplicationWidget.class);

  private Map<String, List<EventListener>> eventListeners;

  private EventListener globalListener;

  private Map<String, List<ActionListener>> actionListeners;

  private Map<String, Object> viewData;

  private Map<String, Object> viewDataOnce;

  protected class ViewableImpl implements Viewable.Interface {

    public Object getViewModel() {
      try {
        return BaseApplicationWidget.this.getViewModel();
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  protected class CompositeImpl implements Composite.Interface {

    /**
     * Returns a map of all the child components under this Composite.
     * 
     * @return a map of child components
     */
    public Map<String, Component> getChildren() {
      return BaseApplicationWidget.this.getChildren();
    }

    /**
     * @see org.araneaframework.Composite.Interface#attach(java.lang.Object, org.araneaframework.Component)
     */
    public void attach(String key, Component comp) {
      _getChildren().put(key, comp);
    }

    /**
     * @see org.araneaframework.Composite.Interface#detach(java.lang.Object)
     */
    public Component detach(String key) {
      return _getChildren().remove(key);
    }
  }

  public class ViewModel implements ApplicationWidget.WidgetViewModel {

    private Map<String, Object> viewData;

    public ViewModel() {
      if (BaseApplicationWidget.this.viewData == null) {
        this.viewData = new HashMap<String, Object>();
      } else {
        this.viewData = new HashMap<String, Object>(BaseApplicationWidget.this.viewData);
      }

      if (BaseApplicationWidget.this.viewDataOnce != null) {
        this.viewData.putAll(BaseApplicationWidget.this.viewDataOnce);
      }
    }

    /** @since 1.1 */
    public Scope getScope() {
      return BaseApplicationWidget.this.getScope();
    }

    public Map<String, Component> getChildren() {
      return BaseApplicationWidget.this.getChildren();
    }

    public Map<String, Object> getData() {
      return this.viewData;
    }
  }

  private Map<String, List<EventListener>> getEventListeners() {
    if (this.eventListeners == null) {
      this.eventListeners = new LinkedHashMap<String, List<EventListener>>();
    }
    return this.eventListeners;
  }

  private Map<String, List<ActionListener>> getActionListeners() {
    if (this.actionListeners == null) {
      this.actionListeners = new LinkedHashMap<String, List<ActionListener>>();
    }
    return this.actionListeners;
  }

  private Map<String, Object> getViewData() {
    if (this.viewData == null) {
      this.viewData = new LinkedHashMap<String, Object>();
    }
    return this.viewData;
  }

  private Map<String, Object> getViewDataOnce() {
    if (this.viewDataOnce == null) {
      this.viewDataOnce = new LinkedHashMap<String, Object>();
    }
    return this.viewDataOnce;
  }

  /**
   * Returns the widget's Environment by default. Usually overridden.
   */
  protected Environment getChildWidgetEnvironment() throws Exception {
    return getEnvironment();
  }

  @Override
  protected void propagate(Message message) throws Exception {
    _propagate(message);
  }

  @Override
  protected void update(InputData input) throws Exception {
    if (this.viewDataOnce != null) {
      this.viewDataOnce.clear();
    }

    handleUpdate(input);

    for (Component child : getChildren().values()) {
      if (child != null && child instanceof Widget) {
        ((Widget) child)._getWidget().update(input);
      }
    }
  }

  /**
   * If path hasNextStep() routes to the correct child, otherwise calls the appropriate listener.
   */
  @Override
  protected void event(Path path, InputData input) throws Exception {
    if (path != null && path.hasNext()) {
      Object next = path.next();

      Assert.notNull(this, next, "Cannot deliver event to child under null key!");

      Widget pWidget = (Widget) getChildren().get(next);

      if (pWidget == null) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Widget '" + getScope() + "' could not deliver event as child '" + next
              + "' was not found or not a Widget!" + Assert.thisToString(this));
        }
        return;
      }

      pWidget._getWidget().event(path, input);
    } else {
      handleEvent(input);
    }
  }

  /**
   * CallBack called when <code>update(InputData)</code> is invoked.
   * 
   * @param input The request data.
   */
  protected void handleUpdate(InputData input) throws Exception {}

  /**
   * Calls the respective listeners.
   */
  protected void handleEvent(InputData input) throws Exception {
    String eventId = getEventId(input);

    if (eventId == null) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Widget '" + getScope() + "' cannot deliver event for a null action id!" + Assert.thisToString(this));
      }
      return;
    }

    List<EventListener> listeners = this.eventListeners == null ? null : this.eventListeners.get(eventId);

    if (LOG.isTraceEnabled()) {
      LOG.trace("Delivering event '" + eventId + "' to widget '" + getScope() + "', type: '" + getClass().getName()
          + "'");
    }

    try {
      if (listeners != null && !listeners.isEmpty()) {
        for (EventListener listener : listeners) {
          listener.processEvent(eventId, input);
        }
        return;
      } else if (this.globalListener != null) {
        this.globalListener.processEvent(eventId, input);
        return;
      }
    } catch (Exception e) {
      throw new EventException(this, String.valueOf(getScope()), eventId, e);
    }

    if (LOG.isWarnEnabled()) {
      LOG.warn("Widget '" + getScope() + "' cannot deliver event as no event listeners were registered for the event "
          + "id '" + eventId + "'!" + Assert.thisToString(this));
    }

  }

  /**
   * If {@link Path#hasNext()} routes to the action to child, otherwise calls the appropriate
   * {@link org.araneaframework.core.ActionListener}.
   */
  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (path != null && path.hasNext()) {
      Object next = path.next();

      Assert.notNull(this, next, "Cannot deliver action to child under null key!");

      Service service = (Service) getChildren().get(next);
      if (service == null) {
        LOG.warn("Service '" + getScope() + "' could not deliver action as child '" + next + "' was not found!"
            + Assert.thisToString(this));
        return;
      }

      service._getService().action(path, input, output);
    } else {
      handleAction(input, output);
    }
  }

  /**
   * Calls the appropriate listener
   */
  protected void handleAction(InputData input, OutputData output) throws Exception {
    String actionId = getActionId(input);

    if (actionId == null) {
      if (LOG.isWarnEnabled()) {
        LOG
            .warn("Service '" + getScope() + "' cannot deliver action for a null action id!"
                + Assert.thisToString(this));
      }
      return;
    }

    List<ActionListener> listeners = this.actionListeners == null ? null : this.actionListeners.get(actionId);

    if (LOG.isTraceEnabled()) {
      LOG.trace("Delivering action '" + actionId + "' to service '" + getScope() + "', type: '" + getClass().getName()
          + "'");
    }

    if (listeners != null && !listeners.isEmpty()) {
      for (ActionListener listener : listeners) {
        listener.processAction(actionId, input, output);
      }
      return;
    }

    if (LOG.isWarnEnabled()) {
      LOG.warn("Service '" + getScope() + "' cannot deliver action as no action listeners were registered for action "
          + "id '" + actionId + "'!" + Assert.thisToString(this));
    }
  }

  /**
   * Renders the component to output, meant for overriding.
   */
  @Override
  protected void render(OutputData output) throws Exception {}

  /**
   * Returns the id of the event in InputData. By default returns EVENT_HANDLER_ID_KEY from the input's global data.
   */
  protected String getEventId(InputData input) {
    return input.getGlobalData().get(ApplicationWidget.EVENT_HANDLER_ID_KEY);
  }

  /**
   * Returns the id of the action based on the input. Uses the ACTION_HANDLER_ID_KEY key to extract it from InputData's
   * global data.
   */
  protected String getActionId(InputData input) {
    return input.getGlobalData().get(ApplicationService.ACTION_HANDLER_ID_KEY);
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * Returns the Viewable.Interface internal implementation.
   * 
   * @return the Viewable.Interface implementation
   */
  public Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }

  /**
   * Returns the Composite.Interface internal implementation.
   * 
   * @return the Composite.Interface implementation
   */
  public Composite.Interface _getComposite() {
    return new CompositeImpl();
  }

  /**
   * Returns all the child-components of this component.
   * 
   * @return a map of the child-components under this component
   */
  public Map<String, Component> getChildren() {
    return Collections.unmodifiableMap(new LinkedHashMap<String, Component>(_getChildren()));
  }

  /**
   * Returns the widget with the specified key.
   * 
   * @param key of the child being returned
   * @return the Widget under the provided key
   */
  public Widget getWidget(String key) {
    return (Widget) getChildren().get(key);
  }

  /**
   * Adds a widget as a child widget with the key. The child is initialized with the environment provided.
   * 
   * @param key of the the child Widget
   * @param child Widget being added
   * @param env the Environment the child will be initialized with
   */
  public void addWidget(String key, Widget child, Environment env) {
    _addComponent(key, child, env);
  }

  /**
   * Adds a widget as a child widget with the key. The child is initialized with the Environment of this Widget
   * 
   * @param key of the the child Widget
   * @param child Widget being added
   */
  public void addWidget(String key, Widget child) {
    try {
      addWidget(key, child, this.getChildWidgetEnvironment());
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Removes component from the children and calls destroy on it.
   * 
   * @param key of the child being removed
   */
  public void removeWidget(Object key) {
    _removeComponent(key);
  }

  /**
   * Enables the widget with the specified key. Only a disabled widgets can be enabled.
   */
  public void enableWidget(String key) {
    _enableComponent(key);
  }

  /**
   * Disables the widget with the specified key. Only a enabled widgets can be disabled.
   */
  public void disableWidget(String key) {
    _disableComponent(key);
  }

  @Override
  public Environment getEnvironment() {
    return super.getEnvironment();
  }

  public final Environment getChildEnvironment() {
    try {
      return getChildWidgetEnvironment();
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Adds a global event-listener to this Widget. A global event-listener gets all the events.
   * 
   * @param eventListener a EventListener added as the global event-listener.
   */
  public void setGlobalEventListener(EventListener eventListener) {
    Assert.notNullParam(this, eventListener, "eventListener");
    this.globalListener = eventListener;
  }

  /**
   * Clears the global event-listener of this Widget.
   */
  public void clearGlobalEventListener() {
    this.globalListener = null;
  }

  /**
   * Adds an EventListener to this Widget with an eventId. Multiple listeners can be added under one eventId.
   * 
   * @param eventId the eventId of the listener
   * @param listener the EventListener being added
   * @see #removeEventListener
   */
  public void addEventListener(String eventId, EventListener listener) {
    Assert.notNullParam(this, eventId, "eventId");
    Assert.notNullParam(this, listener, "listener");
    List<EventListener> list = getEventListeners().get(eventId);
    if (list == null) {
      list = new ArrayList<EventListener>(1);
    }
    list.add(listener);
    getEventListeners().put(eventId, list);
  }

  /**
   * Removes the listener from the Widget's event listeners.
   * 
   * @param listener the EventListener to remove
   * @see #addEventListener
   */
  public void removeEventListener(EventListener listener) {
    Assert.notNullParam(this, listener, "listener");
    for (List<EventListener> listeners : getEventListeners().values()) {
      listeners.remove(listener);
    }
  }

  /**
   * Clears all the EventListeners from this Widget with the specified eventId.
   * 
   * @param eventId the id of the EventListeners.
   */
  public void clearEventlisteners(String eventId) {
    Assert.notNullParam(this, eventId, "eventId");
    getEventListeners().remove(eventId);
  }

  /**
   * Adds custom data to the widget view model (<code>${widget.custom['key']} == ${viewData.key}</code>). This data will
   * be available, until explicitly removed with {@link #removeViewData(String)}.
   * 
   * @param key The key under which <code>customDataItem</code> will be made available.
   * @param customDataItem The data that will be made available.
   */
  public void putViewData(String key, Object customDataItem) {
    Assert.notNullParam(this, key, "key");
    getViewData().put(key, customDataItem);
  }

  /**
   * Removes the custom data under <code>key</code>.
   * 
   * @param key The key, which will be removed from widget's view data.
   */
  public void removeViewData(String key) {
    Assert.notNullParam(this, key, "key");
    getViewData().remove(key);
  }

  /**
   * Adds custom data to the widget view model (<code>${widget.custom['key']} == ${viewData.key}</code>). This data will be available during this request
   * only. It will be discarded right before update() is called.
   * 
   * @param key The key under which <code>customDataItem</code> will be made available.
   * @param customDataItem The data that will be made available.
   */
  public void putViewDataOnce(String key, Object customDataItem) {
    Assert.notNullParam(this, key, "key");
    getViewDataOnce().put(key, customDataItem);
  }

  /**
   * Returns the view model of this control. The view model is used to retrieve data that MVC view layer can access.
   * Note that returned view model and its type is entirely the sole responsibility of the control. However, the
   * controls may change the return type to their implementation of view model.
   */
  public Object getViewModel() {
    return new ViewModel();
  }

  /**
   * Adds the ActionListener listener with the specified action id.
   */
  public void addActionListener(String actionId, ActionListener listener) {
    Assert.notNullParam(this, actionId, "actionId");
    Assert.notNullParam(this, listener, "listener");

    List<ActionListener> list = getActionListeners().get(actionId);

    if (list == null) {
      list = new ArrayList<ActionListener>();
      getActionListeners().put(actionId, list);
    }

    list.add(listener);
    ComponentUtil.registerActionListener(getEnvironment(), getScope(), actionId, listener);
  }

  /**
   * Removes the ActionListener listener from this component.
   */
  public void removeActionListener(ActionListener listener) {
    Assert.notNullParam(this, listener, "listener");
    for (Map.Entry<String, List<ActionListener>> entry : getActionListeners().entrySet()) {
      if (entry.getValue().contains(listener)) {
        boolean existed = entry.getValue().remove(listener);
        if (existed) {
          ComponentUtil.unregisterActionListener(getEnvironment(), getScope(), entry.getKey(), listener);
        }
      }
    }
  }

  /**
   * Clears all the ActionListeners with the specified actionId.
   * 
   * @param actionId the actionId
   */
  public void clearActionListeners(String actionId) {
    Assert.notNullParam(this, actionId, "actionId");
    getActionListeners().remove(actionId);
  }
}
