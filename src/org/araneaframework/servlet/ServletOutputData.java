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

package org.araneaframework.servlet;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;

/**
 * An OutputData with the HttpServletRequest and HttpServletResponse meant to be used
 * in the context of servlets.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public interface ServletOutputData extends OutputData, Serializable {
  
  /**
   * Returns the HttpServletRequest of this OutputData.
   */
  public HttpServletRequest getRequest();
  
  /**
   * Returns the HttpServletResponse of this OutputData.
   */  
  public HttpServletResponse getResponse();
  
  /**
   * Redirects the user agent to the provided URL. 
   * XXX: If one needs URL rewriting one should encode the URL before redirecting.
   */
  public void redirect(Environment environment, String url) throws Exception;
}
