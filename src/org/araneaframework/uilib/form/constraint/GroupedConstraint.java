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

package org.araneaframework.uilib.form.constraint;

import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.framework.MessageContext.MessageData;
import org.araneaframework.uilib.form.Constraint;

/**
 * Constraint that will be applied if the group of constraints is active. To check, if the group is active, the
 * {@link ConstraintGroupHelper} is used. In addition, the latter also gives a more convenient way to create this
 * grouped constraint (see {@link ConstraintGroupHelper#createGroupedConstraint(Constraint, String)}).
 * 
 * @author Ilja Livenson (ilja@webmedia.ee)
 * @see ConstraintGroupHelper
 */
public class GroupedConstraint extends BaseConstraint {

  private ConstraintGroupHelper conditionalConstraintHelper;

  private Constraint constraint;

  private String group;

  /**
   * Creates a new grouped constraint. The <code>helper</code> should be common (the same instance) to all constraints
   * as it is used to check whether the group is active. The group must be registered in the <code>helper</code>
   * beforehand.
   * <p>
   * Usually, one should use a more convenient way to add constraints to a group by using
   * {@link ConstraintGroupHelper#createGroupedConstraint(Constraint, String)}.
   * 
   * @param helper The helper for grouped constraints (required).
   * @param constraint The constraint belonging to that group (required).
   * @param group The name of the group (optional).
   * @see ConstraintGroupHelper#createGroupedConstraint(Constraint, String)
   */
  public GroupedConstraint(ConstraintGroupHelper helper, Constraint constraint, String group) {
    this.conditionalConstraintHelper = helper;
    this.constraint = constraint;
    this.group = group;
  }

  /**
   * Validates the constraint, if the group is active. If it is not active, the validation succeeds.
   */
  @Override
  protected void validateConstraint() throws Exception {
    if (this.conditionalConstraintHelper.isGroupActive(this.group)) {
      this.constraint.validate();
    }
  }

  /**
   * Provides the <code>ConstraintGroupHelper</code> used by this group constraint to handle group names.
   * 
   * @return The <code>ConstraintGroupHelper</code> used by this group constraint.
   */
  public ConstraintGroupHelper getConditionalConstraintHelper() {
    return this.conditionalConstraintHelper;
  }

  /**
   * Sets the <code>ConstraintGroupHelper</code> to be used by this group constraint to handle group names.
   * 
   * @param conditionalConstraintHelper A new <code>ConstraintGroupHelper</code>.
   */
  public void setConditionalConstraintHelper(ConstraintGroupHelper conditionalConstraintHelper) {
    this.conditionalConstraintHelper = conditionalConstraintHelper;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.constraint.setEnvironment(environment);
  }

  @Override
  public void setCustomErrorMessage(String customErrorMessage) {
    this.constraint.setCustomErrorMessage(customErrorMessage);
  }

  @Override
  public void clearErrors() {
    this.constraint.clearErrors();
  }

  @Override
  public Set<MessageData> getErrors() {
    return this.constraint.getErrors();
  }

  @Override
  public boolean isValid() {
    return this.constraint instanceof BaseConstraint && ((BaseConstraint) this.constraint).isValid() || getErrors().isEmpty();
  }
}
