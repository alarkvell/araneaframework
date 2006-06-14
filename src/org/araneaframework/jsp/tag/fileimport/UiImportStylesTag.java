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

package org.araneaframework.jsp.tag.fileimport;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.filter.StandardServletFileImportFilterService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 * @jsp.tag
 *   name = "importStyles"
 *   body-content="empty"
 *   description = "Imports CSS files."
 */
public class UiImportStylesTag extends UiImportFileTag {
	public static final String DEFAULT_GROUP_NAME = "defaultStyles";
	
	private String media;
	
	public int doStartTag(Writer out) throws Exception {
		// if filename specified we include the file
		if (includeFileName != null) {
			writeContent(out, 
				StandardServletFileImportFilterService.IMPORTER_FILE_NAME+"="+includeFileName);
		}
		else if (includeGroupName != null){
			writeContent(out,
				StandardServletFileImportFilterService.IMPORTER_GROUP_NAME+"="+includeGroupName);
		}
		else {
			writeContent(out,
					StandardServletFileImportFilterService.IMPORTER_GROUP_NAME+"="+DEFAULT_GROUP_NAME);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "The media type the css file should be applied to."
	 */
	public void setMedia(String media) throws JspException {
		this.media = (String) evaluate("media", media, String.class);
	}
	 
	protected void writeContent(Writer out, String keyValue) throws Exception {		
		UiUtil.writeOpenStartTag(out, "link");
		UiUtil.writeAttribute(out, "rel", "stylesheet");
		UiUtil.writeAttribute(out, "type", "text/css");
		UiUtil.writeAttribute(out, "href", 
				((HttpServletRequest)pageContext.getRequest()).getRequestURL().append("?").append(keyValue)
				, false);
		UiUtil.writeAttribute(out, "media", this.media);	
		UiUtil.writeCloseStartEndTag(out);
		out.write("\n");
	}
}
