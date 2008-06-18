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

package org.araneaframework.tests.mock;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.core.BaseEnvironment;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.ConfigurationContext;


/**
 * TODO write javadoc
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 * 
 */
public class MockEnvironment extends BaseEnvironment {
  @SuppressWarnings("unchecked")
  private Map<Class, Object> contexts = new HashMap<Class, Object>();

  public MockEnvironment() {
    contexts.put(ConfigurationContext.class, new MockConfiguration());
    contexts.put(LocalizationContext.class, new MockLocalizationContext());
    contexts.put(MessageContext.class, new MockMessageContext());
  }
  
  @SuppressWarnings("unchecked")
  public <T> T getEntry(Class<T> key) {
    return (T) contexts.get(key);
  }
}
