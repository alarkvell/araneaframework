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

package org.araneaframework.http;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Component;

/**
 * A service adapter which by having the servlet's <code>service</code> method, translates a
 * servlet's request into a service call. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public interface ServletServiceAdapterComponent extends Component, Serializable {
  public void service(HttpServletRequest request, HttpServletResponse response);
}
