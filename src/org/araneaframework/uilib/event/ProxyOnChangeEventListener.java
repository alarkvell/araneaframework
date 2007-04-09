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

package org.araneaframework.uilib.event;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class ProxyOnChangeEventListener implements OnChangeEventListener {
  public static final Logger log = Logger.getLogger(ProxyOnChangeEventListener.class);
  
  protected Widget eventTarget;
  protected String eventId;
  
  public ProxyOnChangeEventListener(Widget eventTarget, String eventId) {
    this.eventTarget = eventTarget;
    this.eventId = eventId;
  }
  
  public void onChange() throws Exception {
    String eventHandlerName = "handleEvent" + eventId.substring(0, 1).toUpperCase() + eventId.substring(1);
    
    Method eventHandler;
    // lets try to find a handle method with an empty argument
    try {
      eventHandler = eventTarget.getClass().getMethod(eventHandlerName, new Class[] {});
      eventHandler.invoke(eventTarget, new Object[] {});
      return;
    } catch (NoSuchMethodException e) {/*OK*/}
    
    // lets try to find a method with a String type argument
    try {
      eventHandler = eventTarget.getClass().getMethod(eventHandlerName, new Class[] { String.class });
      eventHandler.invoke(eventTarget, new Object[] { null });
      return;
    } catch (NoSuchMethodException e) {/*OK*/}
    
    log.warn("Widget '" + eventTarget.getScope() +
        "' cannot deliver event as no event listeners were registered for the event id '" + eventId + "'!" + Assert.thisToString(eventTarget)); 
  }
}
