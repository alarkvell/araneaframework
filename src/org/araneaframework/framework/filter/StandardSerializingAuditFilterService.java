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

package org.araneaframework.framework.filter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Service;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.framework.FilterService;

/**
 * Serializes the the session during the request routing. This
 * filter helps to be aware of serializing issues during development. If the
 * session does not serialize, exception is thrown.
 * <br><br>
 * The serialized session can be output to a file by setting the xml session path. The path
 * must be valid & writable.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardSerializingAuditFilterService extends BaseService implements FilterService {
  private static final Logger log = Logger.getLogger(StandardSerializingAuditFilterService.class);
  
  private Relocatable.RelocatableService child;
  private String testXmlSessionPath;
  
  /**
   * Sets the path where to write the serialized class in xml format. The path must
   * be valid and writeable. Example: "/home/user/tmp".
   * @param testXmlSessionPath
   */
  public void setTestXmlSessionPath(String testXmlSessionPath) {
    this.testXmlSessionPath = testXmlSessionPath;
  }
  
  public void setChildService(Service child) {
    this.child = new RelocatableDecorator(child);
  }

  protected void init() throws Exception {
    child._getComponent().init(getEnvironment());
  }
  
  protected void destroy() throws Exception {
    child._getComponent().destroy();
  }
  
  protected void propagate(Message message) throws Exception {
    message.send(null, child);
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    child._getRelocatable().overrideEnvironment(getEnvironment());
    child._getService().action(path, input, output);    
    child._getRelocatable().overrideEnvironment(null);
    
    HttpSession sess = (HttpSession) getEnvironment().getEntry(HttpSession.class);
    
    byte[] serialized = SerializationUtils.serialize(child);
    log.debug("Serialized session size: " + serialized.length);
    
    if (testXmlSessionPath != null) {
      String dumpPath = testXmlSessionPath + "/" + sess.getId() + ".xml";
      log.debug("Dumping session XML to '" + dumpPath + "'");
      
      XStream xstream = new XStream(new DomDriver());
      PrintWriter writer = new PrintWriter(new FileWriter(dumpPath));
      xstream.toXML(child, writer);
      writer.close();
    }
  }
}
