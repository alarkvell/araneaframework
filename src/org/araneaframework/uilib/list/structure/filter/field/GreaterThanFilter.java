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

package org.araneaframework.uilib.list.structure.filter.field;

import java.util.Comparator;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.util.Event;

public abstract class GreaterThanFilter extends BaseFieldFilter {

  private Comparator<?> comparator;

  public static GreaterThanFilter getInstance(final FilterContext ctx, final String fieldId, final String valueId) {
    final GreaterThanFilter filter;
    if (ctx.isStrict()) {
      filter = new Strict();
    } else {
      filter = new NonStrict();
    }
    filter.setFieldId(fieldId);
    filter.setValueId(valueId);
    ctx.addInitEvent(new Event() {

      public void run() {
        filter.setComparator(ctx.getFieldComparator(fieldId));
      }
    });
    return filter;
  }

  public static GreaterThanFilter getConstantInstance(FilterContext ctx, String fieldId, String valueId, Object value) {
    GreaterThanFilter filter = getInstance(ctx, fieldId, valueId);
    filter.setValue(value);
    return filter;
  }

  public static void addToForm(FilterContext ctx, String id, FormElement<?, ?> element) {
    ctx.getForm().addElement(id, element);
  }

  public static void addToForm(FilterContext ctx, String id, Control<?> control) {
    addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, control));
  }

  public static void addToForm(FilterContext ctx, String id) {
    addToForm(ctx, id, FilterFormUtil.createElement(ctx, id));
  }

  public Comparator<?> getComparator() {
    return this.comparator;
  }

  public void setComparator(Comparator<?> comparator) {
    this.comparator = comparator;
  }

  static class Strict extends GreaterThanFilter {

    public Expression buildExpression(Map<String, Object> filterInfo) {
      if (!isActive(filterInfo)) {
        return null;
      }
      return ExpressionUtil.gt(buildVariableExpression(), buildValueExpression(filterInfo), getComparator());
    }
  }

  static class NonStrict extends GreaterThanFilter {

    public Expression buildExpression(Map<String, Object> filterInfo) {
      if (!isActive(filterInfo)) {
        return null;
      }
      return ExpressionUtil.ge(buildVariableExpression(), buildValueExpression(filterInfo), getComparator());
    }
  }
}
