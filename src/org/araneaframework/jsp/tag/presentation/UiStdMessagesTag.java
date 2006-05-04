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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.PageContext;
import org.araneaframework.OutputData;
import org.araneaframework.framework.filter.StandardMessagingFilterWidget;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.tag.aranea.UiAraneaRootTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Message tag - show the messages in <code>MessageContext</code> 
 * with given type. 
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "messages"
 *   body-content = "empty"
 */

public class UiStdMessagesTag extends UiPresentationTag {
  protected String type;

  public UiStdMessagesTag() {
    styleClass = "aranea-messages";
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    OutputData output = (OutputData) UiUtil.requireContextEntry(pageContext, UiAraneaRootTag.OUTPUT_DATA_KEY, PageContext.REQUEST_SCOPE);
    Map messageMap = (Map) output.getAttribute(StandardMessagingFilterWidget.MESSAGE_KEY);

    List entries = new ArrayList();
    for (Iterator i = messageMap.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      if (type == null || ((String)entry.getKey()).equals(type)) {
        entries.add(entry);
      }
    }

    if (entries.size() == 0)
      return EVAL_PAGE;

    /* matching messages, write them out */
    UiUtil.writeOpenStartTag(out, "div");
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeCloseStartTag(out);

    UiUtil.writeStartTag(out, "div");
    UiUtil.writeStartTag(out, "div");
    UiUtil.writeStartTag(out, "div");

    for (Iterator i = entries.iterator(); i.hasNext(); ) {
      List messages = (List) ((Map.Entry) i.next()).getValue();

      for (Iterator j = messages.iterator(); j.hasNext();) {
        out.write((String) j.next());
        if (j.hasNext())
          UiUtil.writeStartEndTag(out, "br");
      }
      if (i.hasNext())
        UiUtil.writeStartEndTag(out, "br");;
    }

    UiUtil.writeEndTag(out, "div");
    UiUtil.writeEndTag(out, "div");
    UiUtil.writeEndTag(out, "div");
    UiUtil.writeEndTag(out, "div");

    return EVAL_PAGE;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Type of messages to show."
   */
  public void setType(String type) {
    this.type = type;
  }
}
