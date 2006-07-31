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

package org.araneaframework.tests.integration;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.araneaframework.mock.servlet.MockServlet;
import org.araneaframework.servlet.ServletServiceAdapterComponent;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * XXX: why is the whole test commented out?
 */
public class SmokeTests extends TestCase {
  /*private MockServlet servlet;
  private ServletServiceAdapterComponent adapter;
  
  private Map initedAdapters = new HashMap();
    
  private MockHttpServletRequest req;
  private MockHttpServletResponse resp;
  
  public SmokeTests(String name) {
    super(name);
  }
  
  public ServletServiceAdapterComponent initAdapter(String configFile) throws Exception {
    
    if (servlet == null ) {
      servlet  = new MockServlet();
    }
    
    ServletServiceAdapterComponent comp = 
      (ServletServiceAdapterComponent)initedAdapters.get(configFile);
    
    if (comp == null) {
      servlet.setConfFile(configFile);
      servlet.init(new MockServletConfig(new MockServletContext()));
      
      initedAdapters.put(configFile, comp);
      
      return servlet.getBuiltComponent();
    }
    
    return comp;
  }
  
  public void setUp() throws Exception {
    servlet  = new MockServlet();
    
    req = new MockHttpServletRequest();
    resp = new MockHttpServletResponse();
  }
  
  public void testSmoke() throws Exception {
    adapter = (ServletServiceAdapterComponent)initAdapter("smokeTest.xml");
    adapter.service(req, resp);
    //success if no exception thrown
    fail();
  }
  //
  
  public void testSerialization() throws Exception {
    adapter = (ServletServiceAdapterComponent)initAdapter("serializationTestsConf.xml");
    
    BeanFactory factory = servlet.getFactory();
    MockViewPortWidget widget = (MockViewPortWidget)factory.getBean("rootWidget");
    widget.setChild(new MockStandardWidget());
    adapter.service(req, resp);
    //success if no exception thrown
  }
  
  public void testRepeatedRequestCatching() throws Exception {
    String event = "theEvent";
    String widgetKey =StandardViewPortWidget.CHILD_KEY;
    
    adapter = (ServletServiceAdapterComponent)initAdapter("repeatedRequest.xml");
    // lets get reference to the bean doing the heavy-lifting
    // its a singleton, so we're cool
    BeanFactory factory = servlet.getFactory();
    MockViewPortWidget widget = (MockViewPortWidget)factory.getBean("rootWidget");
    
    req.addParameter(StandardWidget.EVENT_HANDLER_ID_KEY, event);
    req.addParameter(StandardWidgetContainerWidget.EVENT_PATH_KEY, widgetKey);
    
    // first request, transactionId will get intialized
    adapter.service(req, resp);
    // helper returns true on null transactionId
    
    MockEventfulStandardWidget child1 = 
      (MockEventfulStandardWidget)widget.getChildren().get(widgetKey);
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, ""+child1.getTransactionId());
    
    // second request with the valid transactionId
    adapter.service(req, resp);

    assertTrue(child1.getEventProcessed());
    child1.setEventProcessed(false);
    
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, ""+child1.getTransactionId());    
    adapter.service(req, resp);
    // transactionId used 2nd time, should not process the event
    assertFalse(child1.getEventProcessed());
  }
  
  public void testDestroyPropagates() throws Exception {
    adapter = (ServletServiceAdapterComponent)initAdapter("repeatedRequest.xml");
    
    MockViewPortWidget widget = (MockViewPortWidget)servlet.getFactory().getBean("rootWidget");
    // initializes everything
    adapter.service(req, resp);
    adapter._getComponent().destroy();
    
    MockEventfulStandardWidget child = (MockEventfulStandardWidget)widget.getChildren().get(StandardViewPortWidget.CHILD_KEY);
    assertTrue(child.getDestroyCalled());
  }
    
  public void testRequestRoutingComposite() throws Exception {
    
    String childKey = "aWidget";
    String event = "tehEvent";
    
    adapter = (ServletServiceAdapterComponent)initAdapter("simpleFilters.xml");
    
    MockViewPortWidget widget = (MockViewPortWidget)servlet.getFactory().getBean("rootWidget");
    MockStandardWidget childWidget = new MockStandardWidget();
    childWidget.addEventListener(event, childWidget.new MockEventListener());
    
    widget.addWidget(childKey, childWidget);
    
    req.addParameter(StandardWidget.EVENT_HANDLER_ID_KEY, event);
    req.addParameter(StandardWidgetContainerWidget.EVENT_PATH_KEY, childKey);    
    // initializes everything
    adapter.service(req, resp);
    adapter.service(req, resp);
    
    assertTrue(((MockStandardWidget)widget.getChildren().get(childKey)).getEventProcessed());
  }
  
  public static Test suite() {
    return new TestSuite(SmokeTests.class);
  }

  public static void main(String args[]) {
    junit.textui.TestRunner.run(suite());
  }
  */
}
