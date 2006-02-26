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

package org.araneaframework.uilib.list.util.converter;

import org.araneaframework.uilib.list.util.Converter;

/**
 * Not-null converter for converting <code>Boolean</code> values into
 * <code>String</code> values and vice-versa.
 */
public class BooleanToLongConverter implements Converter {
	private static final long serialVersionUID = 1L;

	public static final Converter ONE_ZERO_CONVERTER = new BooleanToLongConverter(new Long(1), new Long(0));

	protected Long trueValue;
	protected Long falseValue;

	public BooleanToLongConverter(Long trueValue, Long falseValue) {
		if (trueValue == null || falseValue == null) {
			throw new ConvertionException("Target values can not be null");
		}
		if (trueValue == falseValue || trueValue.equals(falseValue)) {
			throw new ConvertionException("Target values can not be the same");
		}
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}

	public Object convert(Object data) throws ConvertionException {
		return convert((Boolean) data);
	}

	public Long convert(Boolean data) throws ConvertionException {
		if (data == null) {
			throw new ConvertionException("Data can not be null");
		}
		return Boolean.TRUE.equals(data) ? this.trueValue : this.falseValue;
	}

	public Object reverseConvert(Object data) throws ConvertionException {
		if (data == null) {
			throw new ConvertionException("Data can not be null");
		}
		if (this.trueValue.equals(data)) {
			return Boolean.TRUE;
		}
		if (this.falseValue.equals(data)) {
			return Boolean.FALSE;
		}
		throw new ConvertionException("Data " + data + " not supported");
	}

	public Class getSourceType() {
		return Boolean.class;
	}

	public Class getDestinationType() {
		return Long.class;
	}
}
