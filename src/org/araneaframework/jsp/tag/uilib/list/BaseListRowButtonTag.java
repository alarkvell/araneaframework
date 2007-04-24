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

package org.araneaframework.jsp.tag.uilib.list;  

import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.presentation.BaseSimpleButtonTag;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;

/**
 * List row button base tag.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class BaseListRowButtonTag extends BaseSimpleButtonTag {
  protected String updateRegions;

  protected List updateRegionNames;
  
  protected UiUpdateEvent event;
  
  {
    event = new UiUpdateEvent();
  }

  protected int doStartTag(Writer out) throws Exception {
    int result = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, id);
    
    if (contextWidgetId == null)
      throw new AraneaJspException("'listRow(Link)Button' tags can only be used in a context widget!");

    event.setParam((String) requireContextEntry(ListRowsTag.ROW_REQUEST_ID_KEY));
    event.setTarget(contextWidgetId);
    event.setUpdateRegionNames(JspUpdateRegionUtil.getUpdateRegionNames(pageContext, updateRegions));
    event.setEventPrecondition(onClickPrecondition);
    
    return result;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Event id." 
   */
  public void setEventId(String eventId) throws JspException {
    event.setId((String)evaluate("eventId", eventId, String.class));
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code><ui:updateRegion></code> for details." 
   */
  public void setUpdateRegions(String updateRegions) throws JspException {
    this.updateRegions = (String) evaluate("updateRegions", updateRegions, String.class);
  }

  public void doFinally() {
    super.doFinally();
    event.clear();
  }
}




