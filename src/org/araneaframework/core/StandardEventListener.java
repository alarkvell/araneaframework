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

import org.araneaframework.InputData;

/**
 * An <code>EventListener</code> that also allows event parameters. Subclasses should implement
 * {@link #processEvent(String, String, InputData)} to provide their own event handling.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class StandardEventListener implements EventListener {

  /**
   * This method is marked final. Subclasses should implement {@link #processEvent(String, String, InputData)}.
   */
  public final void processEvent(String eventId, InputData input) throws Exception {
    String eventParameter = input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);
    processEvent(eventId, eventParameter, input);
  }

  /**
   * Event handling with a parameter. All implementations should handle each <code>eventId</code> separately.
   * 
   * @param eventId The ID of the event.
   * @param eventParam The parameter for the event.
   * @param input The request data of the event.
   * @throws Exception Any runtime exception that may occur.
   */
  public abstract void processEvent(String eventId, String eventParam, InputData input) throws Exception;
}
