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

package org.araneaframework.template.tags.wizard;

import java.io.Writer;
import org.araneaframework.OutputData;
import org.araneaframework.core.Standard;
import org.araneaframework.core.Standard.StandardWidgetInterface;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;


/**
 * This tag includes wizard's current page.
 * It must be nested into WizardTag.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 * 
 * @jsp.tag
 *   name = "wizardBody"
 *   body-content = "JSP"
 *   description = "Includes the current wizard page."
 */
public class WizardBodyTag extends UiBaseTag {
  protected int before(Writer out) throws Exception {
	  Standard.StandardWidgetInterface widget = (StandardWidgetInterface) UiWidgetUtil.getWidgetFromContext(null, pageContext);
		
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
    
	  out.flush();
	  widget._getWidget().render(output);	  
		
		return SKIP_BODY;		
	}
}
