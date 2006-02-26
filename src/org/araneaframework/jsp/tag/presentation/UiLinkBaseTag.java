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

package org.araneaframework.jsp.tag.presentation;   

import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiPresentationTag;

/**
 * Link base tag.
 * 
 * @author Oleg Mürk
 */
public class UiLinkBaseTag extends UiPresentationTag {

  //
  // Attributes
  //  
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Link id, allows to access link from JavaScript." 
	 */
  public void setId(String id) throws JspException {
    this.id = (String)this.evaluateNotNull("id", id, String.class);
  }
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "true"
	 *   description = "Link target URL." 
	 */
  public void setHref(String href) throws JspException {
    this.href = (String)this.evaluateNotNull("href", href, String.class);
  }
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Link target, same as <i>&lt;a&gt;</i> HTML tag <i>target</i> attribute." 
	 */
  public void setTarget(String target) throws JspException {
    this.target = (String)this.evaluateNotNull("target", target, String.class);
  }
  
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Controls whether the link is disabled, disabled link doesn't link anywhere." 
	 */
  public void setDisabled(String disabled) throws JspException {
    this.disabled = ((Boolean)this.evaluateNotNull("disabled", disabled, Boolean.class)).booleanValue();
  }

  protected void init() {
    super.init();
    id = null;
    href = null;
    target = null;
    disabled = false;
  }
  
  
  protected String id;
  protected String href;
  protected String target;
  protected boolean disabled;
}




