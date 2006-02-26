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
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.UiPresentationTag;


/**
 * Attribute tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "attribute"
 *   body-content = "empty"
 *   description = "Defines an attribute of the containing element."
 */
public class UiAttributeTag extends UiBaseTag {

  //
  // Attributes
  //
 
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "true" 
	 *   description = "Attribute name."
	 */
	public void setName(String name) throws JspException {
		this.name = (String)evaluateNotNull("name", name, String.class);
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "true"
	 *   description = "Attribute value." 
	 */
	public void setValue(String value) throws JspException {
		this.value = (String)evaluate("value", value, String.class);
	}
  
  //
  // Implementation
  //
		
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		UiAttributedTagInterface attributedTag = (UiAttributedTagInterface)readAttribute(UiPresentationTag.ATTRIBUTED_TAG_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		attributedTag.addAttribute(name, value);
		
		// Continue
	  return SKIP_BODY;
	}
  
  protected void init() {
    this.name = null;
    this.value = null;
  }
		
	protected String name;
	protected String value;
}
