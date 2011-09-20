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

package org.araneaframework.core.exception;

/**
 * Thrown to indicate that a service with given key (ID) does not exist in the set of (child) services.
 * <p>
 * Components and Widgets have corresponding exceptions, {@link NoSuchComponentException} and
 * {@link NoSuchWidgetException} respectively.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class NoSuchServiceException extends AraneaRuntimeException {

  /**
   * Creates a new exception for a service with given <tt>key</tt> (ID) not being found.
   * 
   * @param key The ID of a child-service that was not found.
   */
  public NoSuchServiceException(String key) {
    super("No such service '" + key + "'");
  }
}
