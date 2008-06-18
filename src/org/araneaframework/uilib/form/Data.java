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

import org.araneaframework.uilib.util.Event;

/**
 * This class represents typed form element data. Type is used by the
 * {@link org.araneaframework.uilib.form.converter.ConverterFactory} to find the appropriate
 * {@link org.araneaframework.uilib.form.Converter} for converting {@link Data} held in 
 * {@link org.araneaframework.uilib.form.FormElement} to plain object type held in 
 * {@link org.araneaframework.uilib.form.Control}. Reverse converting happens much
 * the same, both object type in {@link org.araneaframework.uilib.form.Control} 
 * and supposed {@link org.araneaframework.uilib.form.FormElement} {@link Data} are
 * considered and appropriate {@link org.araneaframework.uilib.form.Converter} 
 * chosen.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class Data<T> implements java.io.Serializable, FormElementAware<Object,T> {
  protected String type;
  protected Class<T> typeClass;
  
  protected T value;  
  protected T markedBaseValue;
  
  protected FormElement<Object, T> feCtx;
  
  /**
   * Creates {@link Data} of type <code>type</code>.
   * @param type the type of {@link Data}
   */
  protected Data(Class typeClass, String type) {   
    this.type = type;
    this.typeClass = typeClass;
  }

  /**
   * Creates {@link Data} for holding objects of given <code>Class</code>.
   * <code>Type</code> is assumed to be simple class name of given Class.
   * @param typeClass the <code>Class</code> of {@link Data} values.
   */
  public Data(Class typeClass) {
    this.typeClass = typeClass;
    this.type = typeClass.toString().substring(
        typeClass.toString().lastIndexOf('.') + 1);
  }  

  /**
   * Returns {@link Data} value.
   * @return {@link Data} value.
   */
  public T getValue() {
    return value;
  }

  /**
   * Sets {@link Data} value.
   * @param value {@link Data} value.
   */
  public void setValue(T value) {
    setDataValue(value);
    setControlValue(value);
  }
  
  /**
   * Sets the value of this {@link Data} without modifying underlying {@link Control}.
   * This is used on {@link FormElement} conversion&mdash;which should not affect {@link Control} values
   * read from request. 
   *  
   * @since 1.0.12 */
  public void setDataValue(T value) {
    //XXX useless now?
//    if (value != null && !(typeClass.isAssignableFrom(value.getClass())))
//      throw new DataItemTypeViolatedException(getValueType(), value.getClass());

    this.value = value;
  }

  /** 
   * Sets the value of {@link Control} that is associated with {@link FormElement} which owns this {@link Data}.
   * @since 1.0.12 */
  public void setControlValue(final T value) {
    if (feCtx != null) {
      feCtx.addInitEvent(new Event() {
        public void run() {
          // TODO:this is dangerous in case Data value is set before FE is associated with Control
          if (feCtx.getControl() != null) {
            feCtx.getControl().setRawValue(feCtx.getConverter().reverseConvert(value));
          }
        }
      });
    }
  }

  /**
   * Returns {@link Data} type.
   * @return {@link Data} type.
   */
  public String getValueType() {
    return type;
  }
  
  @Override
  public String toString() {
    return "Data: [Type = " + getValueType() + "; Value = " + value + "]";
  }
  
  /**
   * Returns a new instance of this {@link Data}, value is not set.
   * @return a new instance of current {@link Data}, value is not set.
   */
  public Data<T> newData() {
    return new Data<T>(typeClass, type);
  }
  
  public static <V> Data<V> newInstance(Class<V> clazz){
    return new Data<V>(clazz);
  }
  
  /**
   * Marks the current value of the {@link Data} as the base state
   * that will be used to determine whether its state has changed in
   * {@link #isStateChanged()}. 
   */
  public void markBaseState() {
  	markedBaseValue = value;
  }
  
  /**
   * Restores the value of the {@link Data} from the marked base state.
   */
  public void restoreBaseState() {
    // TODO: maybe deep copy?
    setValue(markedBaseValue);
  }
  
  /**
   * Returns whether {@link Data} state (value) has changed after it was marked.
   * @return whether  {@link Data} state (value) has changed after it was marked.
   */
  public boolean isStateChanged() {  	
  	if (markedBaseValue == null && value == null) return false;
  	else
  		if (markedBaseValue == null || value == null) return true;
  		else return !markedBaseValue.equals(value);
  }

  public void setFormElementCtx(FormElementContext<Object, T> feCtx) {
    if (this.feCtx != feCtx) {
      this.feCtx = (FormElement<Object, T>)feCtx;
      setValue(getValue());
    }
  }
}
