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

package org.araneaframework.template.tags.uilib.list;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.tag.uilib.list.UiListTag;
import org.araneaframework.jsp.util.UiStdScriptUtil;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.template.tags.container.SampleContainerFooterTag;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.SequenceHelper;


/**
 * List widget sequence footer tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "listSequenceFooter"
 *   body-content = "JSP"
 */
public class SampleContainerListSequenceFooterTag extends SampleContainerFooterTag {
	public final static String PREVIOUS_PAGE_EVENT_ID = "previousPage";
	public final static String NEXT_PAGE_EVENT_ID = "nextPage";
	public final static String PREVIOUS_BLOCK_EVENT_ID = "previousBlock";
	public final static String NEXT_BLOCK_EVENT_ID = "nextBlock";
	public final static String FIRST_PAGE_EVENT_ID = "firstPage";
	public final static String LAST_PAGE_EVENT_ID = "lastPage";  
	public final static String JUMP_TO_PAGE_EVENT_ID = "jumpToPage";
	
	public final static String PREVIOUS_PAGE_LABEL_ID = "list.sequence.previous_page";
	public final static String NEXT_PAGE_LABEL_ID = "list.sequence.next_page";
	
	public final static String LEFT_CELL_STYLE="left";
	public final static String RIGHT_CELL_STYLE="right";
	public final static String SEQUENCE_CELL_STYLE="sequence";
	
	public final static String SHOW_SLICE_EVENT_ID = "showSlice";
	public final static String SHOW_ALL_EVENT_ID = "showAll";
	
	public final static String DEFAULT_NO_DATA_STRING_ID = "list.info.noData";
	
	protected String systemFormId;
	protected String listId;
	
	protected String numberStyleClass;
	protected String infoStyleClass;
	protected String firstClass = "first";
	protected String prevClass = "prev";
	protected String nextClass = "next";
	protected String lastClass = "last";
	protected String showAll = "demo.showAll";
	
	protected String noDataStringId;
	
	protected void init() {
		super.init();
		this.noDataStringId = DEFAULT_NO_DATA_STRING_ID;
		inTable = false;
		styleClass = "pages";
		numberStyleClass = "nr";
		infoStyleClass = "info";
	}
	
	//
	// Implementation
	//
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Data string id for empty list message." 
	 */
	public void setNoDataStringId(String noDataStringId) throws JspException {
		this.noDataStringId = (String)evaluateNotNull("noDataStringId", noDataStringId, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Style for list footer info." 
	 */	
	public void setInfoStyleClass(String infoStyleClass) {
		this.infoStyleClass = infoStyleClass;
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Style for list footer numbers." 
	 */
	public void setNumberStyleClass(String numberStyleClass) {
		this.numberStyleClass = numberStyleClass;
	}

	public int before(Writer out) throws Exception {
		super.before(out);
		
		// Get system form id 
		systemFormId = (String)readAttribute(UiSystemFormTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get list data
		listId = (String)readAttribute(UiListTag.LIST_FULL_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);    
		ListWidget.ViewModel viewModel = (ListWidget.ViewModel)readAttribute(UiListTag.LIST_VIEW_MODEL_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get sequnce data
		SequenceHelper.ViewModel sequenceViewModel = viewModel.getSequence();

		long firstPage = sequenceViewModel.getFirstPage().longValue();
		long lastPage = sequenceViewModel.getLastPage().longValue();
		
		long blockFirstPage = sequenceViewModel.getBlockFirstPage().longValue();
		long blockLastPage = sequenceViewModel.getBlockLastPage().longValue();
		
		long currentPage = sequenceViewModel.getCurrentPage().longValue();
		long totalItemCount = sequenceViewModel.getTotalItemCount().longValue();
		boolean allItemsShown = sequenceViewModel.getAllItemsShown().booleanValue();
		
		long firstShown = sequenceViewModel.getPageFirstItem().longValue();
		long lastShown = sequenceViewModel.getPageLastItem().longValue();
		
		if (totalItemCount > 0 && !allItemsShown) {
			/* FIRST, PREV */
			UiUtil.writeOpenStartTag(out, "div");
			UiUtil.writeAttribute(out, "class", numberStyleClass);
			UiUtil.writeCloseStartTag(out);
			
			writeOpenEventLink(out, FIRST_PAGE_EVENT_ID, null, firstPage != currentPage, firstClass);
			out.write("&nbsp;");
			UiUtil.writeEndTag_SS(out, "a");
			
			writeOpenEventLink(out, PREVIOUS_PAGE_EVENT_ID, null, firstPage < currentPage, prevClass);
			out.write("&nbsp;");
			UiUtil.writeEndTag_SS(out, "a");

			/* END FIRST, PREV */
			
			for(long page = blockFirstPage; page  <= blockLastPage; page++) {
				// Jump to page
				writeOpenEventLink(out, JUMP_TO_PAGE_EVENT_ID, new Long(page).toString(), page != currentPage, page == currentPage ? "active" : null);
				UiUtil.writeEscaped(out, new Long((page - firstPage) + 1).toString());
				UiUtil.writeEndTag_SS(out, "a");
			}
			
			
			writeOpenEventLink(out, NEXT_PAGE_EVENT_ID, null, currentPage < lastPage, nextClass);
			out.write("&nbsp;");
			UiUtil.writeEndTag_SS(out, "a");

			writeOpenEventLink(out, LAST_PAGE_EVENT_ID, null, lastPage != currentPage, lastClass);
			out.write("&nbsp;");
			UiUtil.writeEndTag_SS(out, "a");

			UiUtil.writeEndTag(out, "div"); // numbers

			UiUtil.writeOpenStartTag(out, "div");
			UiUtil.writeAttribute(out, "class", infoStyleClass);
			UiUtil.writeCloseStartTag(out);
			
			out.write("Showing [");
			out.write(new Long(firstShown).toString());
			out.write("-");
			out.write(new Long(lastShown).toString());
			out.write("]. Total ");
			UiUtil.writeEscaped(out, new Long(totalItemCount).toString());
			out.write(". ");
			
			UiUtil.writeOpenStartTag(out, "a");
			UiUtil.writeAttribute(out, "class", "aranea-link-button");
			UiUtil.writeAttribute(out, "href", "javascript:");
			
			UiStdWidgetCallUtil.writeEventAttributeForEvent(
					pageContext,
					out, 
					"onclick", 
					systemFormId, 
					listId, 
					allItemsShown ? SHOW_SLICE_EVENT_ID : SHOW_ALL_EVENT_ID, 
							null,
							null);
			UiUtil.writeCloseStartTag_SS(out);
			UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, showAll));
			UiUtil.writeEndTag_SS(out, "a");

			UiUtil.writeEndTag(out, "div"); //info
		} else {
			
			UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, noDataStringId));
		}
		
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
	
	protected void writeOpenEventLink(Writer out, String eventId, String eventParam, boolean enabled, String styleClass) throws IOException, JspException {
		UiUtil.writeOpenStartTag(out, "a");
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeAttribute(out, "href", "javascript:");

		if (enabled)
			UiStdWidgetCallUtil.writeEventAttributeForEvent(
					pageContext,
					out, 
					"onclick", 
					systemFormId,  
					listId, 
					eventId, 
					eventParam,
					null);
		else
			UiStdScriptUtil.writeEmptyEventAttribute(out, "onclick");                     
		UiUtil.writeCloseStartTag_SS(out);                                    
	}
}
