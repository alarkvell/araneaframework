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

package org.araneaframework.framework.core;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.framework.FilterWidget;

/**
 * A filter widget is a Widget which filters requests to its child widget. A filter overrides one of the methods:
 * <ul>
 * <li><code>action(Path, InputData, OutputData)</code></li>
 * <li><code>update(InputData)</code></li>
 * <li><code>event(Path, InputData)</code></li>
 * <li><code>render(OutputData)</code></li>
 * </ul>
 * and does the filtering by allowing the action to be invoked on the child or not. This class is a skeleton which lets
 * all the requests through, sets the child, handles the initilization and destroying of the child.
 * <p>
 * The child is initialized with <code>getChildWidgetEnvironment()</code> which by default returns this component's
 * Environment. For alternate environments it should be overridden.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class BaseFilterWidget extends BaseWidget implements FilterWidget {

  protected Widget childWidget;

  public BaseFilterWidget() {}

  public BaseFilterWidget(Widget childWidget) {
    setChildWidget(childWidget);
  }

  /**
   * Sets the child to childWidget.
   */
  public void setChildWidget(Widget childWidget) {
    Assert.notNull(this, childWidget, "Filter cannot have a null child!");
    this.childWidget = childWidget;
  }

  public Widget getChildWidget() {
    return this.childWidget;
  }

  @Override
  protected void init() throws Exception {
    Assert.notNull(this, this.childWidget, "Filter cannot have a null child!");
    this.childWidget._getComponent().init(getScope(), getChildWidgetEnvironment());
  }

  @Override
  protected void propagate(Message message) throws Exception {
    message.send(null, this.childWidget);
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    this.childWidget._getService().action(path, input, output);
  }

  @Override
  protected void update(InputData input) throws Exception {
    this.childWidget._getWidget().update(input);
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
    this.childWidget._getWidget().event(path, input);
  }

  @Override
  protected void render(OutputData output) throws Exception {
    this.childWidget._getWidget().render(output);
  }

  @Override
  protected void destroy() throws Exception {
    this.childWidget._getComponent().destroy();
  }

  /**
   * By default returns the widget's Environment. The child is initilized with the return value of this method.
   */
  protected Environment getChildWidgetEnvironment() {
    return getEnvironment();
  }
}
