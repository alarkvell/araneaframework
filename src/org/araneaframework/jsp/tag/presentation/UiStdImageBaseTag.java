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

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Standard image base tag.
 * 
 * @author Oleg Mürk
 */
public abstract class UiStdImageBaseTag extends UiImageBaseTag {
  
  //
  // Implementation
  //  
  
  protected void init() {
	  super.init();
	  this.styleClass = "aranea-image";
  }
  
  protected int before(Writer out) throws Exception {
    super.before(out);
    writeImageLocal(out, src, width, height, alt, getStyleClass()); 
    // Continue
    return EVAL_BODY_INCLUDE;    
  }
  
  /**
   * Method to write out image with given properties and default style.
   */ 
  public void writeImage(Writer out, String src, String width, String height) throws JspException, IOException {
	  writeImage(out, src, width, height, null, getStyleClass());
  }
  
  /**
   * Static method to write out image with given properties.
   */ 
  public static void writeImage(Writer out, String src, String width, String height, String styleClass) throws JspException, IOException {
    writeImage(out, src,width, height, null, styleClass);
  }
  
  public void writeImageLocal(Writer out, String src, String width, String height, String alt, String styleClass) throws JspException, IOException {
    writeImage(out, src,width, height, null, styleClass);
  }
  
  /**
   * Static method to write out image with given properties.
   */ 
  public static void writeImage(Writer out, String src, String width, String height, String alt, String styleClass) throws JspException, IOException {
    UiUtil.writeOpenStartTag(out, "img");
    if (styleClass != null)
        UiUtil.writeAttribute(out, "class", styleClass);

    UiUtil.writeAttribute(out, "src", src, false);
    UiUtil.writeAttribute(out, "width", width);
    UiUtil.writeAttribute(out, "height", height);
    UiUtil.writeAttribute(out, "border", 0+"");
    if (alt != null) 
      UiUtil.writeAttribute(out, "alt", alt);
    
    UiUtil.writeCloseStartEndTag_SS(out);
  }
}
