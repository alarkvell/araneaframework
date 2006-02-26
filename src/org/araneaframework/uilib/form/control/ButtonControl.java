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

package org.araneaframework.uilib.form.control;

import javax.servlet.http.HttpServletRequest;
import org.araneaframework.InputData;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;
import org.araneaframework.uilib.form.FormElement;


/**
 * This class represents a button.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class ButtonControl extends BaseControl {

  //*********************************************************************
  // FIELDS
  //*********************************************************************  
  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();


  /**
   * Returns true
   */
  public boolean isRead() {
    return true;
  }
  
	/**
   * @param onClickEventListener {@link OnClickEventListener} which is called when the control is clicked.
   * 
	 * @see StandardControlEventListenerAdapter#addOnClickEventListener(OnClickEventListener)
	 */
	public void addOnClickEventListener(OnClickEventListener onClickEventListener) {
		eventHelper.addOnClickEventListener(onClickEventListener);
	}  
	
  /**
   * Returns null.
   * @return null.
   */
  public String getRawValueType() {
    return null;
  }
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  protected void init() throws Exception {
    super.init();
    
    addGlobalEventListener(eventHelper);
  }
  
  /**
   * Empty method
   */
  protected void readFromRequest(String controlName, HttpServletRequest request) {
    // Button control is not interested in what is submitted
  }

  /**
   * Empty method
   */
  public void convertAndValidate() {
    // Button control is not interested in conversion and validation
  }

  /**
   * Does nothing
   */
  protected void prepareResponse() {
    // Button control does not have data
  }

  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * Represents a <code>ButtonControl</code> view model.
   * 
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
   * 
   */
  public class ViewModel extends BaseControl.ViewModel {

    private boolean hasOnClickEventListeners;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.hasOnClickEventListeners = ButtonControl.this.eventHelper.hasOnClickEventListeners();
    }
    
    /**
     * Returns whether any onClick events have been registered.
     * @return whether any onClick events have been registered.
     */
    public boolean isOnClickEventRegistered() {
      return hasOnClickEventListeners;
    }
  }  
}
