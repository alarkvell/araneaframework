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

package org.araneaframework.uilib.form.constraint;

import java.util.Collection;
import java.util.Iterator;
import org.araneaframework.uilib.form.Constraint;


/**
 * This constraint implements "AND" Boolean logic (checks that all
 * contained constraits are satisfied). It is eager by default, but can
 * be set to act lazily, (note that subconstraints produce error messages
 * as they are being validated, unless some custom error message has been set,
 * it makes often sense to process all subconstraints). 
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class AndConstraint extends BaseCompositeConstraint {
  private boolean lazy = false;
  
  public AndConstraint() {}
  
  /**
   * @since 1.0.9
   */
  public AndConstraint(Constraint constraint) {
    super(constraint);
  }
  
  /**
   * @param constraints Collection&lt;Constraint&gt;
   * @since 1.0.9
   */
  public AndConstraint(Collection constraints) {
    super(constraints);
  }

  /**
   * Checks that all contained constraits are satisfied.
   */
  public void validateConstraint() throws Exception {
    for (Iterator i = constraints.iterator(); i.hasNext();) {
      Constraint constraint = (Constraint) i.next();
      boolean valid = constraint.validate();
      addErrors(constraint.getErrors());
      constraint.clearErrors();
      if (!valid && this.lazy)
        break;
    }
  }
  
  /**
   * Sets whether this {@link AndConstraint} acts lazily, default is <code>false</code>.
   * @param lazy
   * @return this {@link AndConstraint}
   */
  public AndConstraint setLazy(boolean lazy) {
    this.lazy = lazy;
    return this;
  }
}
