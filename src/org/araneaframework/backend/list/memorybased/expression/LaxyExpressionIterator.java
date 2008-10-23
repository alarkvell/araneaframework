/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

package org.araneaframework.backend.list.memorybased.expression;

import java.util.Iterator;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionBuilder;
import org.araneaframework.core.Assert;

/**
 * Iterator that iterates over {@link Expression}s using an iterator over
 * {@link ExpressionBuilder} objects and <code>data</code>.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class LaxyExpressionIterator implements Iterator {

  private Iterator builderIterator;

  private Map data;

  public LaxyExpressionIterator(Iterator builderIterator, Map data) {
    Assert.notNullParam(this, builderIterator, "builderIterator");
    Assert.notNullParam(this, data, "data");
    this.builderIterator = builderIterator;
    this.data = data;
  }

  public boolean hasNext() {
    return builderIterator.hasNext();
  }

  public Object next() {
    ExpressionBuilder builder = (ExpressionBuilder) builderIterator.next();
    return builder.buildExpression(data);
  }

  public void remove() {
    throw new UnsupportedOperationException("Remove is not supported");
  }
}
