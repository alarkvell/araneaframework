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

package org.araneaframework.backend.list.memorybased;

import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.util.GeneralBeanMapper;
import org.araneaframework.backend.util.RecursiveBeanMapper;

/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class BeanVariableResolver implements VariableResolver {

	private static final long serialVersionUID = 1L;

	private GeneralBeanMapper mapper;

	private Object bean;

	public BeanVariableResolver(Class clazz) {
		this.mapper = new RecursiveBeanMapper(clazz, true);
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Object resolve(Variable variable) {
		return this.mapper.getBeanFieldValue(this.bean, variable.getName());
	}
}
