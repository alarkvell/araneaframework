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

package org.araneaframework.core;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.Viewable;

/**
 * An interface defining a set of standard components via its subinterfaces.
 * <br><br>
 * Provides a <code>CustomComponent</code>,<code>CustomService</code> and a
 * <code>CustomWidget</code>. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface ApplicationComponent extends Component, Serializable, Composite, Viewable {
  /**
   * Get the child Environment of this component.
   */
  public Environment getChildEnvironment();
  
  /**
   * A model based on which a view can be constructed.
   */
  public interface ComponentViewModel extends Serializable {
    /**
     * Return component's child components.
     */
    public Map getChildren();
  }
}
