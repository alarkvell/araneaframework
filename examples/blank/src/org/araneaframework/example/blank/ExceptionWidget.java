/*
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.example.blank;

import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.uilib.core.BaseUIWidget;

public class ExceptionWidget extends BaseUIWidget {
	@Override
  protected void init() throws Exception {
		throw new AraneaRuntimeException("Be not afraid! This exception was generated to let exception handler/renderer demonstrate its abilities.");
	}
}
