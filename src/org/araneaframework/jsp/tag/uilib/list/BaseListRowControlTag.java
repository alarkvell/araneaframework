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
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.uilib.WidgetTag;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * Base class for list row form elements.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.4
 */
public class BaseListRowControlTag extends PresentationTag {

  /**
   * A custom label for the control.
   */
  protected String labelId;

  /**
   * Specifies whether the control should be rendered as disabled. Default
   * is active state.
   */
  protected boolean disabled;

  /**
   * Specifies custom <code>onclick</code> event. Default is none.
   */
  protected String onclick;

  /**
   * Specifies custom <code>acceskey</code> (defined by HTML). Default is none.
   */
  protected String accesskey;

  /**
   * HTML tabindex for the form element. This value must be a number between 0 and 32767.
   */
  protected String tabindex;

  /**
   * Specifies the initial state of the form control. Default is unchecked.
   */
  protected boolean checked = false;

  /**
   * The name of the event handler (in the widget that contains the list) that
   * will be invoked when the selection changes.
   */
  protected String onChangeEventId;

  /**
   * The script that will be called before the submit.
   */
  protected String eventPrecondition;

  /**
   * Update regions that must be updated.
   */
  protected String updateRegions;

  /**
   * Global update regions that must be updated.
   */
  protected String globalUpdateRegions;


  protected void writeOnChangeEvent(Writer out) throws Exception {
    UiUpdateEvent event = new UiUpdateEvent();
    event.setId(onChangeEventId);
    event.setParam((String) requireContextEntry(BaseListRowsTag.ROW_REQUEST_ID_KEY));
    event.setTarget((String) requireContextEntry(WidgetTag.WIDGET_ID_KEY));
    event.setEventPrecondition(eventPrecondition);
    event.setUpdateRegionNames(JspUpdateRegionUtil.getUpdateRegionNames(
        pageContext, updateRegions, globalUpdateRegions));

    JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onchange", event);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies a custom label for the control."
   */
  public void setLabelId(String labelId) throws JspException {
    this.labelId = (String) evaluateNotNull("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.Boolean"
   *   required = "false"
   *   description = "Specifies whether the control should be rendered as disabled. Default is active state."
   */
  public void setDisabled(String disabled) throws JspException {
    Boolean tempResult = (Boolean) evaluateNotNull("disabled", disabled, Boolean.class);
    this.disabled = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies custom <code>onclick</code> event. Default is none."
   */
  public void setOnclick(String onclick) throws JspException {
    this.onclick = (String) evaluateNotNull("onclick", onclick, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies custom <code>acceskey</code> (defined by HTML). Default is none."
   */
  public void setAccessKey(String accessKey) throws JspException {
    this.accesskey = (String) evaluateNotNull("accessKey", accessKey, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.Boolean"
   *   required = "false"
   *   description = "Specifies the initial state of the control. Default is unchecked."
   */
  public void setChecked(String checked) throws JspException {
    Boolean tempResult = (Boolean) evaluateNotNull("checked", checked, Boolean.class);
    this.checked = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "HTML tabindex for the form element. This value must be a number between 0 and 32767."
   */
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = (String) evaluateNotNull("tabindex", tabindex, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "The name of the event handler (in the widget that contains the list) that will be invoked when the selection changes."
   */   
  public void setOnChangeEventId(String onChangeEventId) throws JspException {
    this.onChangeEventId = (String) evaluateNotNull("onChangeEventId", onChangeEventId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "The script that will be called before the submit."
   */
  public void setEventPrecondition(String eventPrecondition) throws JspException {
    this.eventPrecondition = (String) evaluateNotNull("eventPrecondition", eventPrecondition, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Update regions that must be updated."
   */
  public void setUpdateRegions(String updateRegions) throws JspException {
    this.updateRegions = (String) evaluateNotNull("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Global update regions that must be updated."
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions)
      throws JspException {
    this.globalUpdateRegions = (String) evaluateNotNull("globalUpdateRegions",
        globalUpdateRegions, String.class);
  }

}
