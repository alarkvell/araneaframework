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

package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.BaseFormSimpleElementDisplayHtmlTag;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "dateInputDisplay"
 *   body-content = "JSP"
 *   description = "Form date display field, represents UiLib 'DateControl'."
 */
public class FormDateInputDisplayHtmlTag extends BaseFormSimpleElementDisplayHtmlTag {

  public FormDateInputDisplayHtmlTag() {
    this.baseStyleClass = "aranea-date-display";
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlTypes("DateControl", "JodaDateControl");
    return super.doEndTag(out);
  }
}
