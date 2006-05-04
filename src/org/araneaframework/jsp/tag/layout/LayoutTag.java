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

package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Aranea's layout tag, allows placing row's and cells inside.
 * In HTML, this tag corresponds to &lt;table&gt; with <code>class</code> and <code>width</code> attributes.
 * 
 * @jsp.tag
 *   name = "layout"
 *   body-content = "JSP"
 *   description = "Represents a layout. Layouts allow to describe the way content will be placed on the page."
 *
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class LayoutTag extends LayoutBaseTag implements LayoutInterface {
  {
    baseStyleClass = "aranea-layout-base";
  }
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

	UiUtil.writeOpenStartTag(out, "table");
	writeTableAttributes(out);
	UiUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;
  }

  /** Overwrite if other attributes besides <code>styleclass</code> and <code>width</code> are needed for HTML table. */
  protected void writeTableAttributes(Writer out) throws Exception {
    addAttribute("class", getStyleClass());
    addAttribute("width",  width);
    UiUtil.writeAttributes(out, attributes);
  }

  protected int doEndTag(Writer out) throws Exception {
    UiUtil.writeEndTag(out, "table");
    return super.doEndTag(out);
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Default style of rows in this layout. This is multi-valued attribute." 
   */
  public void setRowClasses(String rowClasses) throws JspException {
    this.rowClasses = UiUtil.parseMultiValuedAttribute((String)evaluate("rowClasses", rowClasses, String.class));
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Default styleclass of cells in this layout. This is multi-valued attribute."
   */
  public void setCellClasses(String cellClasses) throws JspException {
    this.cellClasses = UiUtil.parseMultiValuedAttribute((String)evaluate("cellClasses", cellClasses, String.class));
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Width of the layout."
   */
  public void setWidth(String width) throws JspException {
    this.width = (String)evaluate("width", width, String.class);
  }
}
