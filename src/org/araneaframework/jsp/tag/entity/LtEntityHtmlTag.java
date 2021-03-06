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

package org.araneaframework.jsp.tag.entity;

/**
 * Lower-than entity &lt; tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "lt"
 *   body-content = "empty"
 *   description = "HTML <i>&amp;lt;</i> entity."
 */
public class LtEntityHtmlTag extends EntityHtmlTag {
  public LtEntityHtmlTag() {
    super("lt");
  }
}
