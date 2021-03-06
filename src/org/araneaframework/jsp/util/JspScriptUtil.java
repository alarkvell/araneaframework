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

package org.araneaframework.jsp.util;

import java.io.IOException;
import java.io.Writer;


/**
 * UiStd script utilities.
 * 
 * @author Oleg Mürk
 */
public abstract class JspScriptUtil {
  /**
   * Writes out event handling attribute that does nothing.
   */ 
  public static void writeEmptyEventAttribute(Writer out, String attributeName) throws IOException {
    JspUtil.writeOpenAttribute(out, attributeName);    
    out.write("javascript: return false;");
    JspUtil.writeCloseAttribute(out);
  }
  
  /**
   * Writes 'undefined' or object's string representation. 
   */
  public static void writeObject(Writer out, Object o) throws IOException {
    out.write(o == null ? "undefined" : o.toString());
  }
  
  public static void writeElementAttributeScript(Writer out, String elementId, String attribute, String value) throws IOException {
    out.write("<script type=\"text/javascript\">$('" + elementId
        + "').writeAttribute('" + attribute + "', '" + value + "');</script>");
  }
}
