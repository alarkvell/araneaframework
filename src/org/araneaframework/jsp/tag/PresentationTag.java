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

package org.araneaframework.jsp.tag;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;

/**
 * Attributed base tag.
 * 
 * @author Oleg Mürk
 */
public class PresentationTag extends BaseTag implements AttributedTagInterface {

  protected String style;

  protected String styleClass;

  protected String baseStyleClass;

  protected Map<String, Object> attributes = new HashMap<String, Object>();

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(AttributedTagInterface.ATTRIBUTED_TAG_KEY, this);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return EVAL_BODY_INCLUDE;
  }

  @Override
  public void setPageContext(PageContext pageContext) {
    super.setPageContext(pageContext);
  }

  /**
   * Callback: add attribute.
   */
  public void addAttribute(String name, String value) {
    if (value == null) {
      this.attributes.remove(name);
    } else {
      this.attributes.put(name, evaluate("value", value, Object.class));
    }
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Inline style for HTML tag."
   */
  public void setStyle(String style) {
    this.style = evaluate("style", style, String.class);
  }

  /**
   * Callback method: get custom CSS class for tag or <code>null</code>.
   */
  public String getStyle() {
    return this.style;
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "CSS class for the tag."
   */
  public void setStyleClass(String styleClass) {
    this.styleClass = evaluate("styleClass", styleClass, String.class);
  }

  /**
   * Callback: get default css class for tag or <code>null</code>.
   */
  protected String getStyleClass() {
    return calculateStyleClass(this.baseStyleClass, this.styleClass);
  }

  /**
   * Calculates the "sum" of base and custom style class. It means that the resulting string will be like following:
   * "[baseStyleClass][[ ]styleClass]". Does not check whether either CSS classes strings already contain each others
   * CSS classes. However both strings will be trimmed. When both CSS classes resolve to empty strings then
   * <code>null</code> will be returned.
   * 
   * @param baseStyleClass The base CSS class (usually fixed value in the code).
   * @param styleClass The custom CSS class (usually provided to the tag in JSP).
   * @return The "sum" of given CSS classes string to be used in an element's class attribute, or <code>null</code>.
   * @since 1.1
   */
  public static final String calculateStyleClass(String baseStyleClass, String styleClass) {
    styleClass = StringUtils.trimToEmpty(styleClass);

    StringBuffer result = new StringBuffer(StringUtils.trimToEmpty(baseStyleClass));
    if (result.length() > 0 && styleClass.length() > 0) {
      result.append(' ');
    }
    result.append(styleClass);

    return result.length() == 0 ? null : result.toString();
  }
}
