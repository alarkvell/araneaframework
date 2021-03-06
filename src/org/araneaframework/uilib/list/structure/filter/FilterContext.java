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
package org.araneaframework.uilib.list.structure.filter;

import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.ListContext;

/**
 * Filter context. Interface that is used by list filters during their
 * initialization to access current list and global filter configuration. 
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * 
 * @see ListContext
 */
public interface FilterContext extends ListContext {
	
	/**
	 * Returns the filter form.
	 * 
	 * @return the filter form.
	 */
	FormWidget getForm();
	
	/**
	 * Returns whether new filters should be strict.
	 * <p>
	 * E.g. when adding e GreaterThan filter, strict does not allow two values
	 * to be equal.
	 * 
	 * @return whether new filters should be strict.
	 */
	boolean isStrict();

}
