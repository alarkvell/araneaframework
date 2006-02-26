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

package org.araneaframework.uilib.form.converter;

/**
 * Converts <code>String</code> to <code>Boolean</code> and back.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class StringToBooleanConverter extends BaseConverter {

  /**
	 * Converts <code>String</code> to <code>Boolean</code>.
	 */
  public Object convertNotNull(Object data) {
    return Boolean.valueOf((String) data);
  }

  /**
	 * Converts <code>Boolean</code> to <code>String</code>.
	 */
  public Object reverseConvertNotNull(Object data) {
    return ((Boolean) data).booleanValue() ? "true" : "false";
  }

  /**
	 * Returns <code>new StringToBooleanConverter()</code>.
	 */
  public BaseConverter newConverter() {
    return new StringToBooleanConverter();
  }
}
