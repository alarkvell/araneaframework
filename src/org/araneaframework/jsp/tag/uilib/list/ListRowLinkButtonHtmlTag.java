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

package org.araneaframework.jsp.tag.uilib.list;

import java.io.Writer;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "listRowLinkButton"
 *  body-content = "JSP"
 *  description = "Represents a link with an onClick JavaScript event."
 */
public class ListRowLinkButtonHtmlTag extends BaseListRowButtonTag {

  {
    baseStyleClass = "aranea-link-button";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, id);

    JspUtil.writeOpenStartTag(out, "a");
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "border", "0");
    JspUtil.writeAttribute(out, "href", "#");
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);

    if (this.event.getId() != null) {
      JspUtil.writeEventAttributes(out, this.event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
    }

    JspUtil.writeCloseStartTag_SS(out);

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    if (this.localizedLabel != null) {
      JspUtil.writeEscaped(out, this.localizedLabel);
    }
    JspUtil.writeEndTag(out, "a");
    return super.doEndTag(out);
  }
}
