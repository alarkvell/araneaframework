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

package org.araneaframework.uilib.form;

import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.MessageContext.MessageData;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * Form element validation error renderer which sends {@link FormElement} error messages to standard
 * {@link MessageContext}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class StandardFormElementValidationErrorRenderer implements FormElementValidationErrorRenderer {

  public static final StandardFormElementValidationErrorRenderer INSTANCE = new StandardFormElementValidationErrorRenderer();

  public void addError(FormElement<?, ?> element, MessageData messageData) {
    EnvironmentUtil.requireMessageContext(element.getEnvironment()).showMessage(MessageContext.ERROR_TYPE, messageData);
  }

  public void clearErrors(FormElement<?, ?> element) {
    EnvironmentUtil.requireMessageContext(element.getEnvironment()).hideMessagesData(MessageContext.ERROR_TYPE,
        element.getErrors());
  }

  public String getClientRenderText(FormElement<?, ?> element) {
    return "";
  }
}
