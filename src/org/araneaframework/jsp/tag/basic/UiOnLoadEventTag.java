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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Load event tag.
 * 
 * @author Maksim Boiko (max@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "onLoadEvent"
 *   body-content = "empty"
 *   description = "Registers events on body load."
 */
public class UiOnLoadEventTag extends UiBaseTag{
  
  
  //
  // Attributes
  //
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "true"
	 *   description = "Event to register."
	 */
  public void setEvent(String event) throws JspException{
    this.event = (String) evaluate("event", event, String.class);
  }
  
  //
  // Implementation
  //
		
	protected int before(Writer out) throws Exception {
		super.before(out);
		UiUtil.writeStartTag(out, "script");
		out.write("addClientLoadEvent(");
		out.write("function() {");
		UiUtil.writeEscaped(out, event);
		out.write("} );\n");
		UiUtil.writeEndTag(out, "script");
		
	  return SKIP_BODY;
	}
  
  protected void init() {
    this.event = "return true;";
  }
		
	protected String event;
}
