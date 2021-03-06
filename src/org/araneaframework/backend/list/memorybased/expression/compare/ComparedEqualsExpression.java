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

package org.araneaframework.backend.list.memorybased.expression.compare;

import java.util.Comparator;
import org.araneaframework.backend.list.memorybased.Expression;

@SuppressWarnings("unchecked")
public class ComparedEqualsExpression extends ComparableExpression {

  public ComparedEqualsExpression(Expression expr1, Expression expr2, Comparator comparator) {
    super(expr1, expr2, comparator);
  }

  public ComparedEqualsExpression(Expression expr1, Expression expr2) {
    super(expr1, expr2);
  }

  @Override
  protected boolean doEvaluate(Object value1, Object value2) {
    return this.comparator.compare(value1, value2) == 0;
  }
}
