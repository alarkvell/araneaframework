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

package org.araneaframework.backend.list.helper.builder.compexpr;

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.CompExprToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.CompositeComparatorExpression;

public abstract class CompositeCompExprToSqlExprTranslator implements CompExprToSqlExprTranslator {

  public final SqlExpression translate(ComparatorExpression expr, CompExprToSqlExprBuilder builder) {

    ComparatorExpression[] children = ((CompositeComparatorExpression) expr).getChildren();
    SqlExpression[] sqlChildren = new SqlExpression[children.length];

    for (int i = 0; i < children.length; i++) {
      sqlChildren[i] = builder.buildSqlExpression(children[i]);
    }

    return translateParent(expr, sqlChildren);
  }

  protected abstract SqlExpression translateParent(ComparatorExpression expr, SqlExpression[] sqlChildren);

}
