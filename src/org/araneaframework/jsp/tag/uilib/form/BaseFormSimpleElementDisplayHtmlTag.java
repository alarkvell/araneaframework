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

package org.araneaframework.jsp.tag.uilib.form;

import java.io.Writer;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
@SuppressWarnings("unchecked")
public abstract class BaseFormSimpleElementDisplayHtmlTag extends BaseFormElementDisplayTag {

  @Override
  protected int doEndTag(Writer out) throws Exception {
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel) this.controlViewModel);

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag(out);

    String s = viewModel.getSimpleValue();
    if (s != null) {
      JspUtil.writeEscaped(out, s);
    }

    JspUtil.writeEndTag(out, "span");
    return super.doEndTag(out);
  }
}
