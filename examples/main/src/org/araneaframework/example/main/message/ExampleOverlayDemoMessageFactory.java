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

package org.araneaframework.example.main.message;

import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.example.main.web.MenuWidget;
import org.araneaframework.example.main.web.release.DemoModalDialogWidget;
import org.araneaframework.framework.MountContext.MessageFactory;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ExampleOverlayDemoMessageFactory implements MessageFactory {

  public Message buildMessage(String url, final String suffix, InputData input, OutputData output) {
    return new OverlayBroadcastMessage();
  }

  private class OverlayBroadcastMessage extends BroadcastMessage {

    @Override
    protected void execute(Component component) throws Exception {
      if (component instanceof MenuWidget) {
        ((MenuWidget) component).start(new DemoModalDialogWidget());
      }
    }
  }

}
