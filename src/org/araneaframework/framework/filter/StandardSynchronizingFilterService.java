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

package org.araneaframework.framework.filter;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.SynchronizingContext;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * The class guarantees a syncrhonized action method to the child service. A child can
 * check from the environment for the key SynchronizingContext.class to detect if the
 * StandardSynchronizingFilterService has been used as a filter and the action really is
 * syncrhonized.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardSynchronizingFilterService 
  extends BaseFilterService {
  
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(
        getEnvironment(), 
        SynchronizingContext.class, 
        new SynchronizingContext() {});
  }
  
  protected synchronized void action(
      Path path, 
      InputData input, 
      OutputData output) throws Exception {
    if (input.getGlobalData().containsKey("nosync")) {
      super.action(path, input, output);
    } else {
      synchronized (this) {
        super.action(path, input, output);
      }
    }
  }
}
