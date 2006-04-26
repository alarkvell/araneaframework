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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;


/**
 * Element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "element"
 *   body-content = "JSP"
 *   description = "Defines an HTML element."
 */
public class UiElementTag extends UiBaseTag implements UiAttributedTagInterface {
	public final static String KEY_REQUEST = "org.araneaframework.jsp.ui.basic.UiElementTag.KEY";

	protected String name = null;
	protected Map attributes = new HashMap();
	protected boolean hasContent = false;
	
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);

		addContextEntry(KEY_REQUEST, this);
		addContextEntry(UiPresentationTag.ATTRIBUTED_TAG_KEY_REQUEST, this);

		UiUtil.writeOpenStartTag(out, name);

		// Continue
		return EVAL_BODY_INCLUDE;		
	}

	/**
	 * After tag.
	 */
	protected int doEndTag(Writer out) throws Exception {
		if (hasContent)
			UiUtil.writeEndTag_SS(out, name);
		else {
			UiUtil.writeAttributes(out, attributes);
			UiUtil.writeCloseStartEndTag_SS(out);
		}

		super.doEndTag(out);
		return EVAL_PAGE;      
	}
	
	public void addAttribute(String name, String value) throws JspException {
		value = (String)evaluate("value", value, String.class);
		attributes.put(name, value);
	}

	protected void onContent() {
		this.hasContent = true;
	}

	protected void writeAttributes(Writer out) throws Exception {
		UiUtil.writeAttributes(out, attributes);
	}

	/* ***********************************************************************************
	 * Tag attributes
	 * ***********************************************************************************/

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "true" 
	 *   description = "HTML element name."
	 */
	public void setName(String name) throws JspException {
		this.name = (String)evaluateNotNull("name", name, String.class);
	}
}
