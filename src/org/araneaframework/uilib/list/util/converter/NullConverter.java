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

package org.araneaframework.uilib.list.util.converter;

import org.araneaframework.uilib.list.util.Converter;

/**
 * Converter that leaves <code>null</code> values unaltered and uses a another not-null <code>converter</code> for all
 * other cases.
 */
public class NullConverter<S, D> implements Converter<S, D> {

  protected Converter<S, D> notNullConverter;

  public NullConverter(Converter<S, D> notNullConverter) {
    this.notNullConverter = notNullConverter;
  }

  public D convert(S data) throws ConversionException {
    return data == null ? null : this.notNullConverter.convert(data);
  }

  public S reverseConvert(D data) throws ConversionException {
    return data == null ? null : this.notNullConverter.reverseConvert(data);
  }

  public Class<S> getSourceType() {
    return this.notNullConverter.getSourceType();
  }

  public Class<D> getDestinationType() {
    return this.notNullConverter.getDestinationType();
  }
}
