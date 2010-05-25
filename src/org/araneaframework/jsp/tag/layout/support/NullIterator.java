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

package org.araneaframework.jsp.tag.layout.support;

import org.apache.commons.collections.ResettableIterator;

/**
 * Dummy <i>resettable</i> iterator that walks infinite void consisting only of <code>null</code> elements.
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class NullIterator implements ResettableIterator {
	public void reset() {}

	public void remove() {}

	/** 
	 * Returns <code>true</code>.
	 * @return <code>true</code>
	 */
	public boolean hasNext() {
		return true;
	}

	/** 
	 * Returns <code>null</code>.
	 * @return <code>null</code>
	 */
	public Object next() {
		return null;
	}
}