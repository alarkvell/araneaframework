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

package org.araneaframework.core;

import java.io.Serializable;
import org.araneaframework.Environment;
import org.araneaframework.Service;

/**
 * A simple factory interface for creating services with help from Environment.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface ServiceFactory extends Serializable {

  /**
   * Returns a Service.
   * 
   * @return the Service
   */
  public Service buildService(Environment env);
}
