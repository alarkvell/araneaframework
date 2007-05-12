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

package org.araneaframework.jsp.tag.form;        

import java.io.Writer;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;


/**
 * Abstract base class for systemForm tags. System form maps into HTML form.
 * 
 * @author Oleg Mürk
 */
public abstract class BaseSystemFormHtmlTag extends BaseTag {

  public final static String GET_METHOD = "get";
  public final static String POST_METHOD = "post";

  private String id = null;
  protected String derivedId = null;
  protected String method = null;
  protected String enctype = null;
  protected String styleClass = null;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Compute new id for systemForm.
    derivedId = id == null ? "systemForm" : id;

    // Write form 
    JspUtil.writeOpenStartTag(out, "form");
    JspUtil.writeAttribute(out, "id", derivedId);
    JspUtil.writeAttribute(out, "name", derivedId);    
    JspUtil.writeAttribute(out, "method", method);
    JspUtil.writeAttribute(out, "enctype", enctype);
    JspUtil.writeAttribute(out, "accept-charset", getAcceptCharset());
    JspUtil.writeAttribute(out, "action", ((HttpServletResponse)pageContext.getResponse()).encodeURL(getFormAction()));
    if (styleClass != null)
      JspUtil.writeAttribute(out, "class", styleClass);
    JspUtil.writeAttribute(out, "style", "margin: 0px");
    JspUtil.writeAttribute(out, "onsubmit", "return false;");
    JspUtil.writeAttribute(out, "arn-systemForm", "true");
    JspUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;
  }

  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag(out, "form");

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;      
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Form name, autogenerated if omitted."
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *  type = "java.lang.String" 
   *   required = "true" 
   *  description = "Submitting method: GET or POST." 
   */
  public void setMethod(String method) throws JspException {
    this.method = ((String)evaluateNotNull("method", method, String.class)).toLowerCase();

    if (!this.method.equals(GET_METHOD) && 
        !this.method.equals(POST_METHOD))
      throw new AraneaJspException("Wrong form method value '" + method + "'");
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Form data encoding type." 
   */
  public void setEnctype(String enctype) throws JspException {
    this.enctype = (String)evaluate("enctype", enctype, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false" 
   *   description = "CSS class for tag"
   */
  public void setStyleClass(String styleClass) throws JspException {
    this.styleClass = (String) evaluate("styleClass", styleClass, String.class);
  }

  /* ***********************************************************************************
   * ABSTRACT METHODS
   * ***********************************************************************************/

  protected abstract String getAcceptCharset();
  protected abstract String getFormAction();
}
