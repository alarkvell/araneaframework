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

package org.araneaframework.jsp.tag.presentation;  

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.OutputData;
import org.araneaframework.http.util.URLUtil;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.aranea.AraneaRootTag;
import org.araneaframework.jsp.tag.form.BaseSystemFormHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;

/**
 * Button base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseSimpleButtonTag extends PresentationTag {
  protected String id;
  protected String labelId;   
  protected String systemFormId;
  protected String contextWidgetId;
  protected String localizedLabel;
  protected String onClickPrecondition = "return true;";

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    systemFormId = (String) requireContextEntry(BaseSystemFormHtmlTag.ID_KEY);

    if (labelId != null)
      localizedLabel = JspUtil.getResourceString(pageContext, labelId);

    contextWidgetId = JspWidgetUtil.getContextWidgetFullId(pageContext);

    return EVAL_BODY_INCLUDE;    
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Button id, allows to access button from JavaScript." 
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Id of button label." 
   */
  public void setLabelId(String labelId) throws JspException {
    this.labelId = (String)evaluate("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Precondition for deciding whether go to server side or not." 
   */
  public void setOnClickPrecondition(String onClickPrecondition) throws JspException {
    this.onClickPrecondition = (String) evaluate("onChangePrecondition", onClickPrecondition, String.class);
  }
  
  public StringBuffer getRequestURL() throws JspException {
    OutputData output = (OutputData) requireContextEntry(AraneaRootTag.OUTPUT_DATA_KEY);
    StringBuffer sb = new StringBuffer(URLUtil.getServletRequestURL(output.getInputData()));
    return sb;
  }
}