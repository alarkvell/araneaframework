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

package org.araneaframework.jsp.tag.uilib.list;

import java.io.Writer;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "listRowButton"
 *   body-content = "JSP"
 *   description = "Represents an HTML form button."
 */
public class UiStdListRowButtonTag extends UiListRowButtonBaseTag {
  {
    baseStyleClass = "aranea-button";
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);                
    // Write button tag             
    UiUtil.writeOpenStartTag(out, "button");
    UiUtil.writeAttribute(out, "id", id);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "style", getStyle());
    if (eventId != null)
      UiStdWidgetCallUtil.writeEventAttributeForEvent(
          pageContext,
          out,
          "onclick",
          systemFormId,  
          contextWidgetId, 
          eventId, 
          eventParam, 
          onClickPrecondition,
          updateRegionNames);       
    UiUtil.writeCloseStartTag_SS(out);

    return EVAL_BODY_INCLUDE;
  }    

  protected int doEndTag(Writer out) throws Exception {
    if (localizedLabel != null)
      UiUtil.writeEscaped(out, localizedLabel);
    UiUtil.writeEndTag(out, "button"); 
    return super.doEndTag(out);
  }  
}
