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

package org.araneaframework.example.common.tags.wizard;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.example.common.framework.context.WizardContext;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.presentation.UiEventButtonBaseTag;
import org.araneaframework.jsp.tag.presentation.UiStdEventButtonTag;
import org.araneaframework.jsp.util.UiWidgetUtil;


/**
 * This tag displays wizard's navigation buttons.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 * 
 * @jsp.tag
 *   name = "wizardFooter"
 *   body-content = "empty"
 *   description = "Includes navigation buttons."
 */
public class WizardFooterTag extends UiBaseTag {
  public static final String WIZARD_GOTO_EVENT_ID = "goto";
  public static final String WIZARD_SUBMIT_EVENT_ID = "submit";
  public static final String WIZARD_CANCEL_EVENT_ID = "cancel";
  
  public static final String WIZARD_GOTO_NEXT_LABEL_ID = "#Next";
  public static final String WIZARD_GOTO_PREV_LABEL_ID = "#Previous";
  public static final String WIZARD_SUBMIT_LABEL_ID = "#Submit";
  public static final String WIZARD_CANCEL_LABEL_ID = "#Cancel";

  protected int doStartTag(Writer out) throws Exception {
		  	
  	WizardContext wizard = (WizardContext) UiWidgetUtil.getWidgetFromContext(null, pageContext);
		
		int index = wizard.getCurrentPageIndex();
		int count = wizard.countPages();
		
		writeEventButtonTag(WIZARD_GOTO_EVENT_ID,
				new Integer(index - 1).toString(), index == 0, WIZARD_GOTO_PREV_LABEL_ID);
		writeEventButtonTag(WIZARD_GOTO_EVENT_ID,
				new Integer(index + 1).toString(), index == count - 1, WIZARD_GOTO_NEXT_LABEL_ID);
		
		writeEventButtonTag(WIZARD_SUBMIT_EVENT_ID, null, false, WIZARD_SUBMIT_LABEL_ID);
		writeEventButtonTag(WIZARD_CANCEL_EVENT_ID, null, false, WIZARD_CANCEL_LABEL_ID);
		
		return SKIP_BODY;		
	}
  
  protected void writeEventButtonTag(String eventId, String eventParam, boolean disabled, String labelId) throws JspException {
  	UiEventButtonBaseTag buttonTag = new UiStdEventButtonTag();
  	registerSubtag(buttonTag);
  	buttonTag.setEventId(eventId);
  	if (eventParam != null) {
    	buttonTag.setEventParam(eventParam);  		
  	}
  	if (disabled == true) {
    	buttonTag.setDisabled(Boolean.toString(disabled));  		
  	}
  	if (labelId != null) {
    	buttonTag.setLabelId(labelId);  		
  	}  	
		executeSubtag(buttonTag);
		unregisterSubtag(buttonTag);  	
  }
}
