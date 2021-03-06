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

package org.araneaframework.jsp.tag.basic;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.BaseTag;


/**
 * Attribute tag, tries to define attribute of the containing tag (element).
 * First searches for HTML element from <code>PageContext</code> under the 
 * {@link org.araneaframework.jsp.tag.basic.AttributedTagInterface#HTML_ELEMENT_KEY}
 * and if it is found writes out javascript that should set elements attribute.
 * 
 * When this does not succeed, searches {@link org.araneaframework.jsp.tag.basic.AttributedTagInterface#ATTRIBUTED_TAG_KEY}
 * and tries to attach attribute to found {@link org.araneaframework.jsp.tag.basic.AttributedTagInterface}, if any.
 * This only works when {@link org.araneaframework.jsp.tag.basic.AttributedTagInterface} has been implemented 
 * correctly by containing tag and whole HTML for containing tag is written out in <code>doEndTag()</code> method.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "attribute"
 *   body-content = "empty"
 *   description = "Defines an attribute of the containing element."
 */
public class AttributeHtmlTag extends BaseTag {

  protected String name;

  protected String value;

  /**
   * @since 1.2.2
   */
  protected boolean omitEmpty = true;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (!(this.omitEmpty && StringUtils.isBlank(this.value))) {
      String elementKey = (String)getContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY);

    if (elementKey != null) {
      writeAttributeScript(out, elementKey);
      return SKIP_BODY;
    }

    AttributedTagInterface attributedTag = (AttributedTagInterface)requireContextEntry(AttributedTagInterface.ATTRIBUTED_TAG_KEY);
    attributedTag.addAttribute(this.name, this.value);
    }

    return SKIP_BODY;
  }

  public void writeAttributeScript(Writer out, String elementKey) throws IOException {
    out.write("<script type=\"text/javascript\">$('");
    out.write(elementKey);
    out.write("').writeAttribute('");
    out.write(this.name);
    out.write("','");
    out.write(this.value);
    out.write("')</script>");
  }

  /* ***********************************************************************************
   * Tag attributes
   * ********************************************************************************* */

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true" 
   *   description = "Attribute name."
   */
  public void setName(String name) throws JspException{
    this.name = evaluateNotNull("name", name, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true"
   *   description = "Attribute value." 
   */
  public void setValue(String value){
    this.value = evaluate("value", value, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   description = "Whether to omit this attribute, if value is empty."
   * @since 1.2.2 
   */
  public void setOmitEmpty(boolean omitEmpty) {
    this.omitEmpty = omitEmpty;
  }
}
