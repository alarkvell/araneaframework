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

package org.araneaframework;

import java.io.Serializable;
import org.araneaframework.core.NoSuchEnvironmentEntryException;

/**
 * A special data structure providing encapsulation of data needed by different components. 
 * Every Aranea component has an environment. The environment can be inhereted from the parent
 * or be standalone. No component knows from which component the Environment comes from.
 * <br><br>
 * Component does know about the hooks in the environment. As different contexts are added to
 * the environment the component in need of them is reponsible of checking them and acting upon
 * them. 
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Environment extends Serializable {
  
  /**
   * The key that can be used to retrieve Environment (as an example a request scope attribute).
   */
  public static final String ENVIRONMENT_KEY = "org.araneaframework.Environment";
  
  /**
   * Returns the entry with the specified key from this Environment. 
   * Returns null if the entry is not present in the environment.
   */
  public Object getEntry(Object key);
  
  /**
   * Does the same as {@link #getEntry(Object)}, but throws a {@link NoSuchEnvironmentEntryException} if 
   * entry cannot be found. 
   * 
   * @throws NoSuchEnvironmentEntryException If environment entry could not be found.
   */
  public Object requireEntry(Object key) throws NoSuchEnvironmentEntryException;
}
