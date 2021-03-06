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

package org.araneaframework.example.common.tags.example.component;

import org.araneaframework.jsp.tag.uilib.list.ListTag;

import java.io.Writer;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.Tag;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.UiEvent;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.layout.LayoutRowHtmlTag;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.OrderInfoField;
import org.araneaframework.uilib.list.structure.ListField;
import org.araneaframework.uilib.list.structure.ListStructure;

/**
 * @author Oleg Mürk
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "componentListHeader"
 *  body-content = "empty"
 *  description = "Inside this tag list header should be written out."
 */

public class ComponentListHeaderTag extends LayoutRowHtmlTag {

  public final static String ORDER_EVENT_ID = "order";

  public final static String COMPONENT_LIST_STYLE_CLASS = "data";

  private String updateRegions;

  private String globalUpdateRegions;

  private List<String> updateRegionNames;

  public ComponentListHeaderTag() {
    this.styleClass = ComponentListTag.COMPONENT_LIST_STYLE_CLASS;
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    this.updateRegionNames = JspUpdateRegionUtil.getUpdateRegionNames(this.pageContext, this.updateRegions,
        this.globalUpdateRegions);

    super.doStartTag(out);

    writeHeader(out);
    return Tag.EVAL_BODY_INCLUDE;
  }

  @SuppressWarnings("unchecked")
  protected void writeHeader(Writer out) throws Exception {
    // Get list data
    String listId = (String) requireContextEntry(ListTag.LIST_FULL_ID_KEY);
    ListWidget<?>.ViewModel viewModel = (ListWidget.ViewModel) requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY);

    // Get order data
    ListStructure.ViewModel listStructureViewModel = viewModel.getListStructure();
    OrderInfo.ViewModel orderInfoViewModel = viewModel.getOrderInfo();

    for (ListField.ViewModel columnViewModel : listStructureViewModel.getColumnList()) {

      // Write cell
      JspUtil.writeOpenStartTag(out, "th");
      JspUtil.writeCloseStartTag(out);

      // Write link if needed
      if (listStructureViewModel.isColumnOrdered(columnViewModel.getId())) {
        // Draw column ordering if needed
        for (OrderInfoField.ViewModel orderInfoFieldViewModel : orderInfoViewModel.getFields()) {

          if (orderInfoFieldViewModel.getId().equals(columnViewModel.getId())) {
            StringBuffer url = ((HttpServletRequest) this.pageContext.getRequest()).getRequestURL();
            // Found
            if (orderInfoFieldViewModel.isAscending()) {
              JspUtil.writeOpenStartTag(out, "img");
              JspUtil.writeAttribute(out, "src", url.append(FileImportUtil.getImportString("gfx/ico_sortup.gif")));
              JspUtil.writeCloseStartEndTag(out);
            } else {
              JspUtil.writeOpenStartTag(out, "img");
              JspUtil.writeAttribute(out, "src", url.append(FileImportUtil.getImportString("gfx/ico_sortdown.gif")));
              JspUtil.writeCloseStartEndTag(out);
            }
            out.write("&nbsp;");
            break;
          }
        }

        UiEvent orderEvent = new UiUpdateEvent(ComponentListHeaderTag.ORDER_EVENT_ID, listId, columnViewModel.getId(),
            this.updateRegionNames);

        JspUtil.writeOpenStartTag(out, "a");
        JspUtil.writeAttribute(out, "class", "aranea-link-button");
        JspUtil.writeEventAttributes(out, orderEvent);
        JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");

        JspUtil.writeCloseStartTag_SS(out);
      }
      if (columnViewModel.getLabel() != null) {
        JspUtil.writeEscaped(out, JspUtil.getResourceString(this.pageContext, columnViewModel.getLabel()));
      }

      // Write link if needed
      if (listStructureViewModel.isColumnOrdered(columnViewModel.getId())) {
        JspUtil.writeEndTag(out, "a");
      }

      // Write cell
      JspUtil.writeEndTag(out, "th");
    }
    JspUtil.writeEndTag(out, "tr");
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code>&lt;ui:updateRegion/&gt;</code> for details."
   */
  public void setUpdateRegions(String updateRegions) {
    this.updateRegions = evaluate("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated globally. Please see <code>&lt;ui:updateRegion&gt;</code> for details."
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) {
    this.globalUpdateRegions = evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
  }
}
