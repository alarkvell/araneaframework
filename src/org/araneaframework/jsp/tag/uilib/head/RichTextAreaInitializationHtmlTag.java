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

package org.araneaframework.jsp.tag.uilib.head;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.jsp.tag.basic.ElementHtmlTag;
import org.araneaframework.jsp.tag.uilib.form.element.text.FormRichTextAreaHtmlTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * The rich text editor (tinyMCE at the moment) requires a global initialization and configuration. This tag initializes
 * the editor and configures it with default values. Additional configuration options can be given via nested attribute
 * tags.
 * 
 * @author Toomas Römer
 * 
 * @jsp.tag
 *  name = "richTextAreaInit"
 *  body-content = "JSP"
 *  description = "Initializes configures the rich-text area component."
 */
public class RichTextAreaInitializationHtmlTag extends ElementHtmlTag {

  public static final String KEY = "org.araneaframework.jsp.tag.uilib.head.KEY";

  /**
   * The prefix that makes the given String parameter value to TinyMCE to be treated as JavaScript function that will
   * be rendered as the value after given prefix without apostrophes and quotation marks.
   * 
   * @since 2.0
   */
  protected static final String FUNCTION_PREFIX = "fn:";

  @Override
  protected int doStartTag(Writer out) throws Exception {
    setName("script");

    super.doStartTag(out);

    JspUtil.writeAttribute(out, "language", "javascript");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag(out);
    out.write("Aranea.TinyMCEInit=function(){tinyMCE.init({\n");

    setDefaultSettings();

    return EVAL_BODY_INCLUDE;
  }

  /**
   * Adds default settings to the configuration. All the options set in this method can be overridden via nested
   * attribute tags.
   */
  protected void setDefaultSettings() {
    this.attributes.put("editor_selector", FormRichTextAreaHtmlTag.EDITOR_SELECTOR);
    this.attributes.put("mode", "textareas");
    this.attributes.put("theme", "simple");
    this.attributes.put("strict_loading_mode", Boolean.TRUE);

    OverlayContext overlay = getEnvironment().getEntry(OverlayContext.class);
    if (overlay != null && overlay.isOverlayActive()) {
      this.attributes.put("oninit", "fn:Aranea.ModalBox.resize");
    }
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    writeAttributes(out);
    out.write("})};\n");
    JspUtil.writeEndTag(out, "script");
    return EVAL_PAGE;
  }

  protected void writeAttributes(Writer out) throws Exception {
    Iterator<Map.Entry<String, Object>> ite = this.attributes.entrySet().iterator();

    while (ite.hasNext()) {
      Map.Entry<String, Object> entry = ite.next();

      StringBuffer buf = new StringBuffer("\t");
      buf.append(entry.getKey());
      buf.append(" : ");

      if (entry.getValue() instanceof String) {
        String value = entry.getValue().toString();
        if (StringUtils.startsWith(value, FUNCTION_PREFIX)) {
          buf.append(StringUtils.substringAfter(value, FUNCTION_PREFIX));
        } else {
          buf.append('\'').append(value).append('\'');
        }
      } else {
        buf.append(ObjectUtils.toString(entry.getValue(), "null"));
      }

      buf.append(ite.hasNext() ? ",\n" : "");
      out.write(buf.toString());
    }
  }
}
