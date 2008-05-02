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
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.list.ListWidget;

/**
 * @jsp.tag
 *   name = "listRowCheckBox"
 *   body-content = "empty"
 *   display-name = "listRowCheckBox"
 *   description = "Represents a list row check box. If you want, you can bind its state to the list row model object Boolean field value."
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.3
 */
public class ListRowCheckBoxHtmlTag extends PresentationTag {

  /**
   * The value of the selected check box that will be returned with request.
   */
  public static final String LIST_CHECK_VALUE = ListWidget.LIST_CHECK_VALUE;

  /**
   * This will be used to create the ID of the check box of the list row as following:
   * <code>[listId].[SELECTION_SCOPE].[rowRequestId]</code>.
   */
  public static final String SELECTION_SCOPE = ListWidget.LIST_CHECK_SCOPE;

  /**
   * The script that performs update of the select-all check box state.
   */
  protected static final String SCRIPT_ON_CLICK = "return Aranea.UI.updateListSelectAlls(this);";

  protected String value;

  protected boolean disabled;

  protected String onclick;

  protected String accesskey;

  protected String tabindex;

  protected boolean checked = false;

  public ListRowCheckBoxHtmlTag() {
    this.baseStyleClass = "aranea-checkbox";
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    String id = getCheckBoxId();

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "type", "checkbox");
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "name", id);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "value", value != null ? value : LIST_CHECK_VALUE);
    JspUtil.writeAttribute(out, "onclick", getOnclickScript());

    JspUtil.writeAttribute(out, "tabindex", tabindex);
    JspUtil.writeAttribute(out, "accessKey", accesskey);

    if (isChecked()) {
      JspUtil.writeAttribute(out, "checked", "checked");
    }

    if (disabled) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    JspUtil.writeCloseStartTag(out);

    return SKIP_BODY;
  }

  /**
   * Creates the onclick event script, including the onclick script that the
   * user specifies through attribute value.
   * 
   * @return The entire script for check box onclick event.
   */
  protected String getOnclickScript() {
    StringBuffer result = new StringBuffer();
    if (StringUtils.isNotBlank(this.onclick)) {
      result.append(this.onclick);
      result.append("; ");
    }
    result.append(SCRIPT_ON_CLICK);
    return result.toString();
  }

  /**
   * Creates the check box ID. Note that it is very important how the ID looks
   * like. It means that the ID of the row check box must begin with the ID
   * value of the select-all check box to make the JavaScript methods work.
   * 
   * @return The ID that will be used for the generated check box.
   * @throws JspException This method requires listId and rowRequestId entries
   *             from the context.
   */
  protected String getCheckBoxId() throws JspException {
    String listId = (String) requireContextEntry(ListTag.LIST_FULL_ID_KEY);         
    String rowRequestId = (String) requireContextEntry(BaseListRowsTag.ROW_REQUEST_ID_KEY);     
    return listId + "." + SELECTION_SCOPE + "." + rowRequestId;
  }

  protected boolean isChecked() throws JspException {
    Object row = requireContextEntry(BaseListRowsTag.ROW_KEY);
    ListWidget.ViewModel viewModel = (ListWidget.ViewModel) requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY);
    List checkedRows = (List) viewModel.getData().get(SELECTION_SCOPE);

    boolean prevChecked = checkedRows != null && checkedRows.contains(row);

    return this.checked || prevChecked;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   rtexprvalue = "true"
   *   description = "Allows a custom value for this check box (when it is submitted). Default value is <code>selected</code>."
   */
  public void setValue(String value) throws JspException {
    this.value = (String) evaluateNotNull("value", value, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.Boolean"
   *   required = "false"
   *   rtexprvalue = "true"
   *   description = "Specifies whether the check box should be rendered as disabled. Default is active state."
   */
  public void setDisabled(String disabled) throws JspException {
    Boolean tempResult = (Boolean) evaluateNotNull("disabled", disabled, Boolean.class);
    this.disabled = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   rtexprvalue = "true"
   *   description = "Specifies custom <code>onclick</code> event. Default is none."
   */
  public void setOnclick(String onclick) throws JspException {
    this.onclick = (String) evaluateNotNull("onclick", onclick, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   rtexprvalue = "true"
   *   description = "Specifies custom <code>acceskey</code> (defined by HTML). Default is none."
   */
  public void setAccessKey(String accessKey) throws JspException {
    this.accesskey = (String) evaluateNotNull("accessKey", accessKey, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.Boolean"
   *   required = "false"
   *   rtexprvalue = "true"
   *   description = "Specifies the initial value of the check box. Default is unchecked."
   */
  public void setChecked(String checked) throws JspException {
    Boolean tempResult = (Boolean) evaluateNotNull("checked", checked, Boolean.class);
    this.checked = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   rtexprvalue = "true"
   *   description = "HTML tabindex for the check box."
   */   
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = (String) evaluateNotNull("tabindex", tabindex, String.class);
  }

}
