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

package org.araneaframework.backend.list.memorybased.expression.compare;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.Resolver;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;

public class EqualsExpression implements CompositeExpression {

	private static final long serialVersionUID = 1L;

	protected Expression expr1;

	protected Expression expr2;

	public EqualsExpression(Expression expr1, Expression expr2) {
		if (expr1 == null || expr2 == null) {
			throw new IllegalArgumentException("Operands must be provided");
		}
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	public Object evaluate(Resolver resolver)
			throws ExpressionEvaluationException {
		Object value1 = this.expr1.evaluate(resolver);
		Object value2 = this.expr2.evaluate(resolver);
		return new Boolean(value1 == null ? value2 == null : value1.equals(value2));
	}

	public Expression[] getChildren() {
		return new Expression[] { this.expr1, this.expr2 };
	}
}
