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
package org.araneaframework.uilib.list;

import org.araneaframework.backend.util.BeanUtil;

/**
 * This class extends the functionalitiy of <code>TypeHelper</code> for
 * <code>BeanListWidget</code>s.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @see BeanListWidget
 * @since 1.2
 */
public class BeanTypeHelper extends TypeHelper {

  protected Class<?> beanType;

  private static final long serialVersionUID = 1L;

  public BeanTypeHelper(Class<?> beanType) {
    this.beanType = beanType;
  }

  // Configuration
  public Class<?> getFieldType(String fieldId) {
    Class<?> result = super.getFieldType(fieldId);
    if (result == null) {
      result = BeanUtil.getFieldType(beanType, fieldId);
    }
    return result;
  }
}