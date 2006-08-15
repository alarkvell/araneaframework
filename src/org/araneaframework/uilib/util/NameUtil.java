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

package org.araneaframework.uilib.util;

import java.io.Serializable;


/**
 * This class is a general helper, which is used throughout UiLib to parse <code>String</code>s like
 * events and hierarchical names. 
 * 
* @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
* 
*/
public class NameUtil implements Serializable {

  /**
   * Returns the fullname concatenated from the <code>prefix</code>
   * and the <code>name</code>. If prefix isn't empty, a <code>"."</code>
   * is added after it.
   * 
   * @param prefix the full name prefix.
   * @param name the current name.
   * @return full name.
   */
  public static String getFullName(String prefix, String name) {
    return "".equals(prefix) || prefix == null ? name : prefix + "." + name;
  }
  
  /**
   * Returns the prefix of the full name (prefix is part before the first 
   * <code>"."</code>, or the whole full name if it contains no dots).
   * 
   * @param fullName the full name.
   * @return prefix of the full name.
   */
  public static String getNamePrefix(String fullName) {
    int dotIndex = fullName.indexOf(".");
    return (dotIndex == -1 ? fullName : fullName.substring(0, dotIndex));
  }
  
  /**
   * Returns suffix of the fullname(suffix is part after the first 
   * <code>"."</code>, or an empty <code>String</code> if full name 
   * contains no dots).
   * 
   * @param fullName full name.
   * @return suffix of the full name.
   */
  public static String getNameSuffix(String fullName) {
    int dotIndex = fullName.indexOf(".");    
    return (dotIndex == -1 ? "" : fullName.substring(dotIndex + 1));   
  }
  
  /**
   * Returns the event name extracted from the action parameter of the
   * <code>"Event"</code> action. It is the part in the action parameter before the first <code>"$"</code>.
   * 
   * @param eventActionParam the action parameter of the <code>"UiEvent"</code> action
   * @return the event name.
   */
  public static String getEventName(String eventActionParam) {
    int dollarIndex = eventActionParam.indexOf("$");    
    return (dollarIndex == -1 ? eventActionParam : eventActionParam.substring(0, dollarIndex));    
  }
  
  /**
   * Returns the event parameter extracted from the action parameter of the
   * <code>"Event"</code> action. It is the part in the action parameter after the first <code>"$"</code>.
   * 
   * @param eventActionParam the action parameter of the <code>"UiEvent"</code> action
   * @return the event parameter.
   */
  public static String getEventParam(String eventActionParam) {
    int dollarIndex = eventActionParam.indexOf("$");    
    return (dollarIndex == -1 ? "" : eventActionParam.substring(dollarIndex + 1));    
  }  
}
