package org.araneaframework.tests.mock;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

public class MockResourceBundle extends ResourceBundle {

  protected Object handleGetObject(String key) {
    return key;
  }

  public Enumeration getKeys() {
    return null;
  }

}
