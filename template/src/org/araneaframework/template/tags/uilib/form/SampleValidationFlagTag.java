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

package org.araneaframework.template.tags.uilib.form;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;



/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 */
public class SampleValidationFlagTag extends UiFormElementBaseTag {
  
  /**
   */
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    UiUtil.writeOpenStartTag(out, "img");
    UiUtil.writeOpenAttribute(out, "id");   
    out.write("img_");
    UiUtil.writeEscapedAttribute(out, formFullId);
    out.write(".");
    UiUtil.writeEscapedAttribute(out, derivedId);
    UiUtil.writeCloseAttribute(out);
    if (formElementViewModel.isValid())
      UiUtil.writeAttribute(out, "style", "visibility: hidden;");
    else
      UiUtil.writeAttribute(out, "style", "visibility: visible;");
    UiUtil.writeAttribute(out, "src", "gfx/uistd_validation_error.gif");
    UiUtil.writeCloseStartEndTag_SS(out);   
    
    return SKIP_BODY;
  }
}
