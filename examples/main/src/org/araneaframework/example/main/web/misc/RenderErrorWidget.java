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

package org.araneaframework.example.main.web.misc;

import org.araneaframework.OutputData;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.util.ServletUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class RenderErrorWidget extends TemplateBaseWidget {

  @Override
  protected void render(OutputData output) throws Exception {
    ServletUtil.include("/WEB-INF/jsp/error/InitErrorWidget/main.jsp", this, output);
    throw new AraneaRuntimeException("Error on render()!");
  }
}
