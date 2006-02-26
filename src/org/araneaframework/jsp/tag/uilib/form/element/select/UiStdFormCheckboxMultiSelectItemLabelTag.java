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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementLabelBaseTag;
import org.araneaframework.jsp.tag.uilib.form.UiStdFormSimpleLabelTag;
import org.araneaframework.uilib.form.control.MultiSelectControl;


/**
 * Standard form element label tag.
 * 
 * @author Marko Muts 
 * @author Jevgeni Kabanov
 * 
 * @jsp.tag
 *   name = "checkboxMultiSelectItemLabel"
 *   body-content = "JSP"
 *   description = "Represents localizable label."
 */
public class UiStdFormCheckboxMultiSelectItemLabelTag extends UiFormElementLabelBaseTag {
  //
  // Attributes
  //  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Select item value." 
	 */
  public void setValue(String value) throws JspException  {
    this.value = (String)evaluateNotNull("value", value, String.class);
  }
    
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		// Prepare	
		MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel)controlViewModel);
		
		if (viewModel.getSelectItemByValue(value) == null) 
			throw new UiException("Value '" + value + "' not found in values list.");    
				
    String label =  viewModel.getSelectItemByValue(value).getDisplayString();    
    
    UiStdFormSimpleLabelTag.writeSelectLabel(
                                out, 
                                label, 
                                getStyleClass()
                              );    
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}
	
  protected void init() {
    super.init();
    value = null;
  }	
  
  protected String value;
}




