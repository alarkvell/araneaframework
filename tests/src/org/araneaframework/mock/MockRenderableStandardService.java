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

package org.araneaframework.mock;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * 
 * JEV: afraid this is no more needed.
 *
 */
public class MockRenderableStandardService extends BaseApplicationService {
  private boolean renderCalled = false;
  private boolean actionCalled = false;
  
  //public Renderable.Interface _getRenderable() {
  //  return new RenderableInterface();
  //}

  public void action(Path path, InputData input, OutputData output) throws Exception {
    actionCalled = true;
  }
  
  //private class RenderableInterface implements Renderable.Interface {
  //  public void render(org.araneaframework.OutputData output) throws Exception {
  //    MockRenderableStandardService.this.renderCalled = true;
  //  }
  //}

  public boolean getRenderCalled() {
    return renderCalled;
  }

  public boolean getActionCalled() {
    return actionCalled;
  } 
}
