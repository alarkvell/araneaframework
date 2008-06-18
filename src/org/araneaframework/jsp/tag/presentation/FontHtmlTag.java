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
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard font tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "font"
 *   body-content = "JSP"
 *   description = "Acts as <i>&lt;font&gt;</i> HTML tag."
 */
public class FontHtmlTag extends BaseTag {
  protected String face = null;
  protected String size = null;
  protected String color = null;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    JspUtil.writeOpenStartTag(out, "font");
    JspUtil.writeAttribute(out, "face", face);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "color", color);
    JspUtil.writeCloseStartTag_SS(out);

    // Continue
    return EVAL_BODY_INCLUDE;    
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {   
    JspUtil.writeEndTag(out, "font");

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
   *   description = "Font face." 
   */
  public void setFace(String face){
    this.face = evaluate("face", face, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Font size." 
   */
  public void setSize(String size){
    this.size = evaluate("size", size, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Font color (any HTML valid color)." 
   */
  public void setColor(String color){
    this.color = evaluate("color", color, String.class);
  }
}
