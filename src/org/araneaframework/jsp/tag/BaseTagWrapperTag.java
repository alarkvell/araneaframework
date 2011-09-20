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

package org.araneaframework.jsp.tag;

import java.io.Writer;
import javax.servlet.jsp.JspException;

/**
 * Tag wrapper tag.
 * 
 * @author Oleg Mürk
 */
public abstract class BaseTagWrapperTag extends BaseTag {

  protected ContainedTagInterface tag;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get wrapped tag
    this.tag = getTag();

    // Prepare
    this.registerSubtag(this.tag);

    // Configure
    configureTag(this.tag);

    // Execute start tag
    executeStartSubtag(this.tag);

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    executeEndSubtag(this.tag);

    // Complete
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /**
   * Callback: get tag
   */
  protected abstract ContainedTagInterface getTag() throws JspException;

  /**
   * Callback: configure tag
   */
  protected abstract void configureTag(ContainedTagInterface tag) throws JspException;
}
