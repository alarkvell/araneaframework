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

import java.util.Iterator;
import org.araneaframework.uilib.form.Constraint;


/**
 * This constraint implements "AND" Boolean logic (checks that all
 * contained constraits are satisfied).
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class AndConstraint extends CompositeConstraint {

  /**
	 * Checks that all contained constraits are satisfied.
   * @throws Exception 
	 */
  public void validateConstraint() throws Exception {
    for (Iterator i = constraints.iterator(); i.hasNext();) {
      Constraint constraint = (Constraint) i.next();
      constraint.validate();
      addErrors(constraint.getErrors());
      constraint.clearErrors();
    }
  }
}
