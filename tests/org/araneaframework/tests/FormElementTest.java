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

package org.araneaframework.tests;

import junit.framework.TestCase;
import org.araneaframework.servlet.core.StandardServletInputData;
import org.araneaframework.tests.mock.MockEnviroment;
import org.araneaframework.uilib.widgets.forms.FormElement;
import org.araneaframework.uilib.widgets.forms.controls.StringArrayRequestControl;
import org.araneaframework.uilib.widgets.forms.controls.TextControl;
import org.araneaframework.uilib.widgets.forms.converters.StringToLongConverter;
import org.araneaframework.uilib.widgets.forms.data.LongData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class FormElementTest extends TestCase {
  /**
   * Tests that {@link TextControl} return <code>null</code> on empty request.
   * @throws Exception
   */
  public void testDataCycling() throws Exception {
    MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
    emptyRequest.addParameter("myTextBox", "");

    FormElement sfe = new FormElement();
    
    sfe._getComponent().init(new MockEnviroment());
    
    TextControl tb = new TextControl();
    tb.setMandatory(true);
    
    sfe.setControl(tb);
    sfe.setData(new LongData());
    sfe.setConverter(new StringToLongConverter());
    
    sfe._getWidget().update(new StandardServletInputData(emptyRequest));
    sfe.convertAndValidate();
    
    sfe.getData().setValue(new Long(110));
    
    sfe._getWidget().process();
    
    assertEquals("The textbox must have the data item value!", ((StringArrayRequestControl.ViewModel) sfe.getControl()._getViewable().getViewModel()).getSimpleValue(), "110");
    
    sfe._getComponent().destroy();
  } 
}
