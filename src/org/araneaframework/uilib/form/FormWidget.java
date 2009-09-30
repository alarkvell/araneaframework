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

package org.araneaframework.uilib.form;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.InvalidFormElementNameException;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.list.util.NestedFormUtil;
import org.araneaframework.uilib.util.NameUtil;


/**
 * This class represents a form element that can contain other form elements.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class FormWidget extends GenericFormElement implements FormContext {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  protected Map<String, GenericFormElement> elements = new LinkedHashMap<String, GenericFormElement>();

  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  @Override
  protected void init() throws Exception {
    super.init();
    
    for (Map.Entry<String, GenericFormElement> element : getElements().entrySet()) {
      addWidget(element.getKey(), element.getValue());
    }
  }
  
  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  @Override
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), FormContext.class, this);
  }

  @Override
  public void clearErrors() {
	  getMessageCtx().hideMessages(MessageContext.ERROR_TYPE, getErrors());
	  super.clearErrors();
	  for (Iterator<GenericFormElement> i = getElements().values().iterator(); i.hasNext();) {
		 i.next().clearErrors();
	  }
  }
  
  @Override
  public boolean isValid() {
    boolean result = super.isValid();
    
    for (Iterator<GenericFormElement> i = getElements().values().iterator(); i.hasNext();) {
      if (!result) 
        break;
      result &= i.next().isValid();   
    }
    
    return result;
  }
  
  /**
   * Returns a contained element by its name.
   * 
   * @param elementName contained element name
   * @return a contained element by its name.
   */
  public GenericFormElement getElement(String elementName) {
    if (elementName.indexOf('.') == -1)
      return elements.get(elementName);
    return getGenericElementByFullName(elementName);
  }

  /**
   * Adds a contained element with given id after the element with specified id.
   * 
   * @param id added element id
   * @param element added element
   * @param afterId element id after which contained element should be added
   */
  public void addElementAfter(String id, GenericFormElement element, String afterId) {
    Assert.notEmptyParam(id, "id");
    Assert.notEmptyParam(afterId, "afterId");
    Assert.notNullParam(element, "element");
    Assert.isTrue(id.indexOf(".") == -1, "addElementAfter() does not accept nested 'id' parameter..");

    FormWidget form = NestedFormUtil.getDeepestForm(afterId, this);
    
    // form is now the actual form to add element into
    form.addFlatElementAfter(id, element, NameUtil.getShortestSuffix(afterId));
  }

  /**
   * Adds a contained element with given id before the element with specified id.
   * Should be only used in RARE cases where internal order of elements matters for some reason.
   * 
   * @param id added element id
   * @param element added element
   * @param beforeId element id before which contained element should be added
   */
  public void addElementBefore(String id, GenericFormElement element, String beforeId) {
    Assert.notEmptyParam(id, "id");
    Assert.notEmptyParam(beforeId, "beforeId");
    Assert.notNullParam(element, "element");
    Assert.isTrue(id.indexOf(".") == -1, "addElementBefore() does not accept nested 'id' parameter.");
    
    FormWidget form = NestedFormUtil.getDeepestForm(beforeId, this);
    
    // form is now the actual form to add element into
    form.addFlatElementBefore(id, element, NameUtil.getShortestSuffix(beforeId));
  }

  private void addFlatElementAfter(String id, GenericFormElement element, String afterId) {
    Assert.isTrue(afterId.indexOf(".") == -1, "addFlatElementAfter() method does not accept nested 'afterId'");
    Map<String, GenericFormElement> newElements = new LinkedHashMap<String, GenericFormElement>();

    if (!getElements().containsKey(afterId))
      throw new AraneaRuntimeException("The element '" + afterId + "' does not exist!");

    for (Map.Entry<String, GenericFormElement> entry : elements.entrySet()) {

      newElements.put(entry.getKey(), entry.getValue());
      if (entry.getKey().equals(afterId)) {
        newElements.put(id, element);
      }
    }

    if (isInitialized())
      addWidget(id, element);

    elements = newElements;
  }

  private void addFlatElementBefore(String id, GenericFormElement element, String beforeId) {
	Assert.isTrue(beforeId.indexOf(".") == -1, "addFlatElementBefore() method does not accept nested 'afterId'");
    Map<String, GenericFormElement> newElements = new LinkedHashMap<String, GenericFormElement>();  

    if (!elements.containsKey(beforeId))
      throw new AraneaRuntimeException("The element '" + beforeId + "' does not exist!");

    for (Map.Entry<String, GenericFormElement> entry : elements.entrySet()) {

      if (entry.getKey().equals(beforeId))
        newElements.put(id, element);
      newElements.put(entry.getKey(), entry.getValue());
    }

    if (isInitialized())
      addWidget(id, element);

    elements = newElements;
  }
  
  /**
   * Adds a contained element.
   * 
   * @param element contained element.
   * @param id element id
   */
  public void addElement(String id, GenericFormElement element) {
    Assert.notEmptyParam(id, "id");
    Assert.notNullParam(element, "element");
    
    if (id.indexOf(".") != -1) {
      NestedFormUtil.addElement(this, id, element);
    } else {
      elements.put(id, element);

      if (isInitialized())
        addWidget(id, element);
    }
  }

  /**
   * Removes a contained element by its name.
   */
  public void removeElement(String id) {
    Assert.notEmptyParam(id, "id");

    elements.remove(id);
    
    if (isInitialized())
      removeWidget(id);
  }

  /**
   * Returns elements.
   * @return elements.
   */
  public Map<String, GenericFormElement> getElements() {
    return new LinkedHashMap<String, GenericFormElement>(elements);
  }

  /**
   * Calls {@link GenericFormElement#convert()} for all contained elements.
   */
  @Override
  protected void convertInternal()  throws Exception  {
    for (Iterator<GenericFormElement> i = elements.values().iterator(); i.hasNext();) {
      i.next().convert();
    }
  }

  /**
   * Controls that the constraints and all subcontrols are valid.
   */
  @Override
  protected boolean validateInternal() throws Exception  {
    for (Iterator<GenericFormElement> i = elements.values().iterator(); i.hasNext();)
      i.next().validate();  
    
    return super.validateInternal();
  }

  @Override
  public void markBaseState() {
    for (Iterator<GenericFormElement> i = elements.values().iterator(); i.hasNext();)
      i.next().markBaseState();
  }
  
  @Override
  public void restoreBaseState() {
    for (Iterator<GenericFormElement> i = elements.values().iterator(); i.hasNext();)
       i.next().restoreBaseState();
  }
  
  @Override
  public boolean isStateChanged() {
  	boolean result = false;
    for (Iterator<GenericFormElement> i = elements.values().iterator(); i.hasNext();)
    	result |= i.next().isStateChanged();
    return result;
  }   
  
  @Override
  public void setDisabled(boolean disabled) {
    for (Iterator<GenericFormElement> i = elements.values().iterator(); i.hasNext();)
      i.next().setDisabled(disabled);
  }
  
  @Override
  public boolean isDisabled() {
  	boolean result = false;
    for (Iterator<GenericFormElement> i = elements.values().iterator(); i.hasNext();)
    	result &= i.next().isDisabled();
    return result;
  }
  
  @Override
  public void accept(String id, FormElementVisitor visitor) {
    visitor.visit(id, this);

    visitor.pushContext(id, this);

    for (Map.Entry<String, GenericFormElement> entry : elements.entrySet()) {

      String elementId = entry.getKey();
      GenericFormElement element = entry.getValue();

      element.accept(elementId, visitor);
    }

    visitor.popContext();
  }
  

  //*********************************************************************
  //* ELEMENT CREATION AND ADDITION
  //*********************************************************************

  /**
   * Adds a new subform to this {@link FormWidget}.
   * @param id subform id.
   * 
   * @return created subform
   */
  public FormWidget addSubForm(String id) {
    Assert.notEmptyParam(id, "id");
    
  	FormWidget result = new FormWidget();
  	addElement(id, result);
    return result;
  }

  /**
   * This method makes a {@link FormElement} with given {@link Control} and {@link Data}.
   * 
   * @param labelId localized label id
   * @param control the type of control
   * @param data the type of data
   * @param initialValue initial value for data
   * @param mandatory whether the element must be filled in
   * @return {@link FormElement} with given configuration
   */
  public <C,D> FormElement<C,D> createElement(String labelId, Control<C> control, Data<D> data, D initialValue, boolean mandatory) {
    if (data != null)
      data.setValue(initialValue);
    return createElement(labelId, control, data, mandatory); 
  }

  
  /**
   * This method makes a {@link FormElement} with given {@link Control} and {@link Data}.
   * 
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @return {@link FormElement} with given configuration
   */
  public <C,D> FormElement<C,D> createElement(String labelId, Control<C> control, Data<D> data, boolean mandatory) {
    Assert.notNullParam(control, "control");
    
    FormElement<C,D> result = new FormElement<C,D>();
    
    result.setLabel(labelId);
    result.setMandatory(mandatory);    
    result.setControl(control);
    if (data != null) {
      result.setData(data);
    }
    return result;
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   */
  public <C,D> FormElement<C,D> addElement(String elementName, String labelId, Control<C> control, Data<D> data) {
    return this.addElement(elementName, labelId, control, data, false);
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   */
  public <C,D> FormElement<C,D> addElement(String elementName, String labelId, Control<C> control, Data<D> data, D initialValue) {
    return this.addElement(elementName, labelId, control, data, initialValue, false);
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   */
  public <C,D> FormElement<C,D> addElement(String elementName, String labelId, Control<C> control, Data<D> data, boolean mandatory) {
  	FormElement<C,D> result = createElement(labelId, control, data, mandatory);
    addElement(elementName, result);
    return result;
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   */
  public <C,D> FormElement<C,D> addElement(String elementName, String labelId, Control<C> control, Data<D> data, D initialValue, boolean mandatory) {
  	FormElement<C,D> result = createElement(labelId, control, data, initialValue, mandatory);
    addElement(elementName, result);
    return result;
  }  


  //*********************************************************************
  //* TRAVERSAL METHODS
  //*********************************************************************

  /**
   * Returns form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return form element specified by full name.
   */
  public GenericFormElement getGenericElementByFullName(String fullName) {
    Assert.notEmptyParam(fullName, "fullName");
    
    GenericFormElement result = null;

    String currentElementName = NameUtil.getNamePrefix(fullName);
    String nextElementNames = NameUtil.getNameSuffix(fullName);

    if ("".equals(nextElementNames)) {
      result = getElement(currentElementName);
    }
    else {
      FormWidget nextElement = (FormWidget) getElement(currentElementName);

      if (nextElement == null) return null;

      result = nextElement.getGenericElementByFullName(nextElementNames);
    }

    return result;
  }

  /**
   * Returns simple form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return simple form element specified by full name.
   */
  @SuppressWarnings("unchecked")
  public <T> FormElement<?, T> getElementByFullName(String fullName) {
    return (FormElement<?, T>) getGenericElementByFullName(fullName);
  }
  
  /**
   * Returns simple form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return simple form element specified by full name.
   */
  public FormWidget getSubFormByFullName(String fullName) {
    return (FormWidget) getGenericElementByFullName(fullName);
  }
  
  /**
   * Returns composite form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return composite form element specified by full name.
   */
  public Control<?> getControlByFullName(String fullName) {
  	FormElement<?, ?> el = getElementByFullName(fullName);
    return (el == null) ? null : el.getControl();
  }
  
  /**
   * Returns form element value specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return form element value specified by full name.
   */
  public Object getValueByFullName(String fullName) {
  	FormElement<?, ?> el = getElementByFullName(fullName);
    return (el == null) ? null : el.getValue();  	    
  }

  /**
   * Sets form element value specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @param value form element value specified by full name.
   */  
  public <T> void setValueByFullName(String fullName, T value) {
  	FormElement<?, T> el = getElementByFullName(fullName);
  	
  	if (el == null)
  		throw new InvalidFormElementNameException(fullName);
  	
  	el.getData().setValue(value);
  }
  
  @Override
  public void addError(String error) {
    super.addError(error);
    getMessageCtx().showErrorMessage(error);
  }

  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   */
  @Override
  public Object getViewModel() {
    return new ViewModel();
  }
  
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************  
  
  /**
   * Represents a composite form element view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends GenericFormElement.ViewModel {
    /**
     * Returns the <code>Map</code> with element views.
     * @return the <code>Map</code> with element views.
     */
    public Map<Object, Component> getElements() {
      return getChildren();
    } 
  }
}
