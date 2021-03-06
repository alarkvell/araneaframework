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

package org.araneaframework.example.main.web.demo.simple;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BooleanData;

/**
 * Simple bean form component. A form with one checkbox, one textbox, three kinds of different timeinputs (DateInput,
 * Timeinput and DateTimeInput), different primitive number inputs, and a button.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1.3
 */
public class SimpleBeanFormWidget extends TemplateBaseWidget {

  private BeanFormWidget<FormDataModel> simpleForm;

  /**
   * Builds the form.
   */
  @Override
  protected void init() throws Exception {
    setViewSelector("demo/simple/simpleBeanForm");

    // Creation of new bean form bound to the instance of FormDataModel class:
    simpleForm = new BeanFormWidget<FormDataModel>(FormDataModel.class, getDefaultValues());

    // Now that we have created a form, we will need to add form elements. form elements consist of four basic things -
    // label, Control that implements form element functionality and Data holding values that form element can have.
    // Note that the first sample with FormWidget's createElement method is not the way form elements are usually added
    // to the form, but rather emphasizes the fact that everything you add to FormWidget is a FormElement.
    //
    // addBeanElement(String elementName, String labelId, Control control, boolean mandatory);
    // 
    // Notice, we don't have to specify data type, because it can be read from the bean field. Also, the primitive
    // fields are always mandatory, because they cannot accept null values.

    this.simpleForm.addElement("caseSensitive", "demo.beanForm.caseSensitive", new CheckboxControl(), new BooleanData());
    this.simpleForm.setValueByFullName("caseSensitive", Boolean.TRUE);

    this.simpleForm.addBeanElement("searchString", "demo.beanForm.searchString", new TextControl());
    this.simpleForm.addBeanElement("createdDateTime", "demo.beanForm.createdDateTime", new DateTimeControl());
    this.simpleForm.addBeanElement("createdTime", "demo.beanForm.createdTime", new TimeControl());
    this.simpleForm.addBeanElement("createdDate", "demo.beanForm.createdDate", new DateControl());
    this.simpleForm.addBeanElement("length", "demo.beanForm.length", new FloatControl());

    // Primitive field mappings (notice the fields are mandatory, because they cannot accept null values - Aranea would 
    // throw an exception when trying to store null value in a primitive data type):
    this.simpleForm.addBeanElement("siblingsCount", "demo.beanForm.siblingsCount", new NumberControl(), true);
    this.simpleForm.addBeanElement("peopleCount", "demo.beanForm.peopleCount", new NumberControl(), true);
    this.simpleForm.addBeanElement("weight", "demo.beanForm.weight", new FloatControl(), true);
    this.simpleForm.addBeanElement("preciseWeight", "demo.beanForm.preciseWeight", new FloatControl(), true);

    // We use a simple option of not defining the button here. Instead, we just use a JSP tag that renders the button
    // and also invokes the event.

    // The usual, add the created widget to main widget.
    addWidget("simpleForm", this.simpleForm);
  }

  /**
   * A test action, invoked when button is pressed. It adds the values of form elements to message context, and they end
   * up at the top of user screen at the end of the request.
   */
  public void handleEventTestSimpleBeanForm() throws Exception {
    // If form is not invalid, do not try to show form element values (error messages are added automatically to the
    // message context though, user will not be without feedback)
    if (this.simpleForm.convertAndValidate()) {
      FormDataModel data = this.simpleForm.writeToBean();

      // We can display the result simply like that:
      showMsg("common.Checkbox", data.isCaseSensitive());
      showMsg("common.Textbox", data.getSearchString());
      showMsg("common.datetime", data.getCreatedDateTime());
      showMsg("common.time", data.getCreatedTime());
      showMsg("common.date", data.getCreatedDate());
      showMsg("common.float", data.getLength());
    }
  }

  private void showMsg(String label, Object value) {
    getMessageCtx().showInfoMessage("simpleForm.msg", t(label), value);
  }

  private FormDataModel getDefaultValues() {
    FormDataModel formData = new FormDataModel();
    formData.setCaseSensitive(true);
    formData.setSearchString("Where am I?");
    formData.setCreatedDateTime(new Date());
    formData.setCreatedDate(new Date());
    formData.setCreatedTime(new Date());
    formData.setLength(new BigDecimal("12345.6789"));
    formData.setSiblingsCount(5);
    formData.setPeopleCount(66500000000L);
    formData.setWeight(77.8F);
    formData.setPreciseWeight(77.8989);
    return formData;
  }

  /**
   * The data model for our sample form.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   */
  public class FormDataModel implements Serializable {

    private boolean caseSensitive;

    private String searchString;

    private Date createdDateTime;

    private Date createdDate;

    private Date createdTime;

    private BigDecimal length;

    private int siblingsCount;

    private long peopleCount;

    private float weight;

    private double preciseWeight;

    public boolean isCaseSensitive() {
      return this.caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
      this.caseSensitive = caseSensitive;
    }

    public String getSearchString() {
      return this.searchString;
    }

    public void setSearchString(String searchString) {
      this.searchString = searchString;
    }

    public Date getCreatedDateTime() {
      return this.createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
      this.createdDateTime = createdDateTime;
    }

    public Date getCreatedDate() {
      return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
    }

    public Date getCreatedTime() {
      return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
      this.createdTime = createdTime;
    }

    public BigDecimal getLength() {
      return this.length;
    }

    public void setLength(BigDecimal length) {
      this.length = length;
    }

    public int getSiblingsCount() {
      return this.siblingsCount;
    }

    public void setSiblingsCount(int siblingsCount) {
      this.siblingsCount = siblingsCount;
    }

    public long getPeopleCount() {
      return this.peopleCount;
    }

    public void setPeopleCount(long peopleCount) {
      this.peopleCount = peopleCount;
    }

    public float getWeight() {
      return this.weight;
    }

    public void setWeight(float weight) {
      this.weight = weight;
    }

    public double getPreciseWeight() {
      return this.preciseWeight;
    }

    public void setPreciseWeight(double preciseWeight) {
      this.preciseWeight = preciseWeight;
    }

  }

}
