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

package org.araneaframework.example.main.web.release.contextMenu;

import org.araneaframework.example.main.web.release.model.ExampleData;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ClientViewWidget extends TemplateBaseWidget {

  private ExampleData.Client client;

  public ClientViewWidget(ExampleData.Client client) {
    this.client = client;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("release/contextMenu/clientView");
    BeanFormWidget<ExampleData.Client> form = new BeanFormWidget<ExampleData.Client>(ExampleData.Client.class,
        this.client);
    form.addBeanElement("sex", "common.sex", new TextControl(), true);
    form.addBeanElement("forename", "common.firstname", new TextControl(), true);
    form.addBeanElement("surname", "common.lastname", new TextControl(), true);
    form.addBeanElement("country", "common.Country", new TextControl(), true);
    addWidget("form", form);
  }

  public ExampleData.Client getClient() {
    return this.client;
  }

  public void handleEventReturn() {
    getFlowCtx().cancel();
  }
}
