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

package org.araneaframework.example.main.web.demo.formlist;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DemoDisplayForm extends TemplateBaseWidget {

  private FormWidget displayForm = new FormWidget();

  /**
   * Builds the form with one checkbox, one textbox and a button.
   */
  @Override
  public void init() throws Exception {
    setViewSelector("demo/formlist/displayForm");

    this.displayForm.addElement("condDisplay", "displayForm.condition", new DisplayControl(), new BooleanData());
    this.displayForm.addElement("textDisplay", "displayForm.text", new DisplayControl(), new StringData());
    this.displayForm.addElement("valueDisplay", "displayForm.value", new DisplayControl(), new LongData());

    this.displayForm.setValueByFullName("condDisplay", true);
    this.displayForm.setValueByFullName("textDisplay", "Test string");
    this.displayForm.setValueByFullName("valueDisplay", 11L);

    addWidget("displayForm", this.displayForm);
  }
}
