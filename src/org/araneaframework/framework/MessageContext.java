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

package org.araneaframework.framework;

import java.io.Serializable;

/**
 * A context for adding messages to a central pool of messages for later 
 * output with a consistent look. 
 * <p>A good example is {@link org.araneaframework.framework.filter.StandardMessagingFilterWidget}.
 * It registers a MessageContext in the environment and if a childservice needs to output a
 * message which should have a consistent look (location on the screen, styles etc.) it adds
 * the message via <code>showMessage(String,String)</code>.
 * </p>
 * <p>
 * A top level widget can use the MessageContext from the environment to output the accumulated
 * messages.
 * </p>
 * <p>
 * Permanent messages should stay in MessageContext until explicitly cleared, other messages should
 * be cleared by calling <code>clearMessages()</code> each time when implementing 
 * {@link org.araneaframework.Widget}'s <code>update()</code> is called.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface MessageContext extends Serializable {
  public static final String MESSAGE_KEY = "org.araneaframework.framework.MessageContext.MESSAGES";
  
  /**
   * A message type indicating its an error message. 
   */
  public static final String ERROR_TYPE = "error";
  
  /**
   * A message type indicating its an info message.
   */
  public static final String INFO_TYPE = "info";  
  
  /**
   * Shows a message with type type and contents message to this MessageContext.
   */
  public void showMessage(String type, String message);

  /**
   * Shows a permanent message that stays in this {@link org.araneaframework.framework.MessageContext} until cleared.
   */
  public void showPermanentMessage(String type, String message);

  /**
   * Shows an error message of type {@link #ERROR_TYPE}.
   */
  public void showErrorMessage(String message);

  /**
   * Shows an informative message of type {@link #INFO_TYPE}.
   */
  public void showInfoMessage(String message);
  
  /**
   * Removes all messages currently present in this MessageContext.
   * For non-permanent messages, this should always happen when implementing 
   * {@link org.araneaframework.Widget}'s <code>update()</code> is called.
   */
  public void clearMessages();

  
  /**
   * Clears the specific permanent message, under all message types where it might be present.
   * @param message to be removed from permanent messages
   */
  public void hidePermanentMessage(String message);
  
  /**
   * Clears the permanent messages present in this {@link org.araneaframework.framework.MessageContext}.
   */
  public void clearPermanentMessages();
  
  /**
   * Clears all messages present in this {@link org.araneaframework.framework.MessageContext}.
   */  
  public void clearAllMessages();
}
