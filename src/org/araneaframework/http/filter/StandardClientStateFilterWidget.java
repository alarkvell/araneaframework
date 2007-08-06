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

package org.araneaframework.http.filter;

import java.io.Serializable;
import java.util.Arrays;
import net.iharder.base64.Base64;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.framework.FilterWidget;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.util.EncodingUtil;

/**
 * A filter providing saving the state on the client side. On every render
 * the descendent of the filter is serialized and written out to the client.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author "Jevgeni Kabanov" <ekabanov@webmedia.ee>
 */
public class StandardClientStateFilterWidget extends BaseFilterWidget implements FilterWidget {
  private static final Log log = LogFactory.getLog(StandardClientStateFilterWidget.class);

  /**
   * Global parameter key for the client state form input.
   */
  public static final String CLIENT_STATE = "araClientState";

  /**
   * Global parameter key for the version of the client state form input.
   */
  public static final String CLIENT_STATE_VERSION = "araClientStateVersion";

  private Buffer digestSet = new CircularFifoBuffer(10);

  private boolean compress = false;

  private void refreshClientState(InputData input) throws Exception {
    if (childWidget == null) {
      String state = (String)input.getGlobalData().get(CLIENT_STATE);

      byte[] lastDigest = Base64.decode((String)input.getGlobalData().get(CLIENT_STATE_VERSION)+"");

      if (!digestSet.contains(new Digest(lastDigest)))
        throw new SecurityException("Invalid session digest!");	
      if (!EncodingUtil.checkDigest(state.getBytes(), lastDigest))
        throw new SecurityException("Invalid session state!");

      childWidget = (RelocatableWidget)EncodingUtil.decodeObjectBase64(state, compress);
      ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(getEnvironment());
    }
  }

  protected void update(InputData input) throws Exception {
    refreshClientState(input);
    super.update(input);
  }

  protected void render(OutputData output) throws Exception {
    refreshClientState(output.getInputData());

    ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(null);

    String base64 = EncodingUtil.encodeObjectBase64(this.childWidget, compress);

    log.debug("Serialized client state size: " + base64.length());

    byte[] lastDigest = EncodingUtil.buildDigest(base64.getBytes());

    String clientStateVersion = Base64.encodeBytes(lastDigest, Base64.DONT_BREAK_LINES);
    digestSet.add(new Digest(lastDigest));

    ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(getEnvironment());

    SystemFormContext systemFormContext = (SystemFormContext) getEnvironment().requireEntry(SystemFormContext.class);
    systemFormContext.addField(CLIENT_STATE, base64);
    systemFormContext.addField(CLIENT_STATE_VERSION, clientStateVersion);
 
    super.render(output);

    childWidget = null;
  }

  /**
   * Sets the child to childWidget.
   */
  public void setChildWidget(Widget childWidget) {
    this.childWidget = new RelocatableDecorator(childWidget);
  }

  private static class Digest implements Serializable {
    private byte[] digest;

    public Digest(byte[] digest) {
      this.digest = digest;
    }

    public byte[] getDigest() {
      return digest;
    }

    public boolean equals(Object obj) {
      return Arrays.equals(digest, ((Digest) obj).getDigest());
    }

    public int hashCode() {
      int result = 37;
      for (int i = 0; i < digest.length; i++) {
        result += 11 * digest[i];
      }
      return result;			
    }
  }

  /**
   * If true, the serialized state will also be GZIP'ed.
   * @param compress
   */
  public void setCompress(boolean compress) {
    this.compress = compress;
  }
}
