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

package org.araneaframework.backend.list.memorybased;

import java.io.Serializable;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;

/**
 * Expression that returns an <code>Object</code> when it is evaluated.
 */
public interface Expression extends Serializable {

  /**
   * Evaluates this <code>Expression</code> by returning its value. The returned value may also be <code>null</code>.
   * 
   * @param resolver Resolver that is used to evaluate <code>Variable</code> value.
   * @return The value of this <code>Expression</code>.
   * @throws ExpressionEvaluationException when the evaluating fails.
   */
  Object evaluate(VariableResolver resolver) throws ExpressionEvaluationException;
}
