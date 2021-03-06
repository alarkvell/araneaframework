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

package org.araneaframework.example.main.web.demo.list;

import org.araneaframework.example.main.web.management.company.CompanyListWidget;

import org.araneaframework.example.main.web.management.person.PersonListWidget;

import org.araneaframework.example.main.TemplateBaseWidget;

/**
 * Widget that shows both person and company lists, demonstrating basic reusing of widgets in different flows.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class MultiListWidget extends TemplateBaseWidget {

  @Override
  protected void init() throws Exception {
    addWidget("personListWdgt", new PersonListWidget(true));
    addWidget("companyListWdgt", new CompanyListWidget());
    setViewSelector("demo/list/multiList");
  }

  // note how we define no events in this widget - all events that our included components receive are defined by
  // themselves.
}
