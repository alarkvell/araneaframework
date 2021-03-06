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

package org.araneaframework.uilib.form.converter;

import java.math.BigDecimal;
import org.araneaframework.uilib.form.Converter;

/**
 * Converter that enables conversion of {@link BigDecimal} to {@link Double} and
 * vice versa.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1.3
 */
public class BigDecimalToDoubleConverter extends BaseConverter<BigDecimal, Double> {

  /**
   * Returns a <code>new BigDecimalToDoubleConverter()</code>.
   */
  @Override
  public Converter<BigDecimal, Double> newConverter() {
    return new BigDecimalToDoubleConverter();
  }

  /**
   * Converts <code>BigDecimal</code> to <code>Double</code>.
   */
  @Override
  protected Double convertNotNull(BigDecimal data) {
    return new Double(data.toString());
  }

  /**
   * Converts <code>Double</code> to <code>BigDecimal</code>.
   */
  @Override
  protected BigDecimal reverseConvertNotNull(Double data) {
    return new BigDecimal(data.toString());
  }

}
