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
package org.araneaframework.uilib.list.structure.filterform;

import java.io.Serializable;

import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.structure.ListFilter;

/**
 * Base interface for all Filters that can be added onto {@link FormWidget}.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public interface FormFilter extends ListFilter {
	/**
	 * Sets the form which the filter elements can be added.
	 * 
	 * @param form the form which the filter elements can be added.
	 */
	void setForm(FormWidget form);
}
