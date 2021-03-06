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

import java.math.BigInteger;
import org.araneaframework.uilib.form.Converter;


/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class BigIntegerToIntegerConverter extends BaseConverter<BigInteger, Integer> {

  /**
   * Returns a <code>new BigIntegerToIntegerConverter()</code>.
   */
  @Override
  public Converter<BigInteger, Integer> newConverter() {
    return new BigIntegerToIntegerConverter();
  }

  /**
   * Converts <code>BigInteger</code> to <code>Integer</code>.
   */
  @Override
  protected Integer convertNotNull(BigInteger data) {
    return data.intValue();
  }

  /**
   * Converts <code>Integer</code> to <code>BigInteger</code>.
   */
  @Override
  protected BigInteger reverseConvertNotNull(Integer data) {
    return new BigInteger(data.toString());
  }

}
