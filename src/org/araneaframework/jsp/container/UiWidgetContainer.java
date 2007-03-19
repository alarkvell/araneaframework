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

package org.araneaframework.jsp.container;

import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 *
 */
public interface UiWidgetContainer {
  
  public static final String KEY = "org.araneaframework.jsp.container.UiWidgetContainer";
  
  /**
   * Returns a widget from the container.
   */
  public Map getWidgets();
  
  /**
   * Returns the mapping from tags to their classes.
   * 
   * @param uri - uri of the taglib sought
   */
  public Map getTagMapping(PageContext pageContext, String uri) throws JspException;
}
