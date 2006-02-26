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
import org.araneaframework.jsp.util.UiUtil;

/**
 * Standard button tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "basicLinkButton"
 *   body-content = "JSP"
 *   description = "Represents a link with an onClick JavaScript action."
 */
public class UiStdLinkButtonTag extends UiButtonBaseTag {
  
	protected void init() {
		super.init();
		styleClass = "aranea-link-button"; 
	}
  
  //
  // Implementation
  //  
  
  protected int before(Writer out) throws Exception {
    super.before(out);

    UiUtil.writeOpenStartTag(out, "a");
    UiUtil.writeAttribute(out, "id", id);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "href", "javascript:");
    UiUtil.writeAttribute(out, "onclick", onclick);    
    UiUtil.writeCloseStartTag_SS(out);
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }
  
  protected int after(Writer out) throws Exception {  
    if (labelId != null)            
      UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, labelId));
    UiUtil.writeEndTag(out, "a"); 
    
    // Continue
    super.after(out);
    return EVAL_PAGE;      
  }
}




