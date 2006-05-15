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

package org.araneaframework.mock.core;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.servlet.ServletAtomicResponseOutputExtension;
import org.araneaframework.servlet.ServletOutputData;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockAtomicRequestService extends MockEventfulStandardService {
  private boolean doRollback = false;
  
  public void action(Path path, InputData input, OutputData output) throws Exception {
    super.action(path, input, output);
    ServletAtomicResponseOutputExtension are = 
      (ServletAtomicResponseOutputExtension)output.narrow(ServletAtomicResponseOutputExtension.class);
    ServletOutputData out = (ServletOutputData)output;
    
    out.getResponse().getOutputStream().write(new byte[] {1});
    
    out.getResponse().getWriter().write("Hello, Word!");
    
    if (doRollback) {
      are.rollback();
    }
  }

  public boolean getDoRollback() {
    return doRollback;
  }

  public void setDoRollback(boolean doRollback) {
    this.doRollback = doRollback;
  }
}
