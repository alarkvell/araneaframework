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

package org.araneaframework.servlet.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.log4j.Logger;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.servlet.ServletOverridableOutputData;

/**
 * A helper class for providing rollback and commit functionality on an OutputData.
 * If something has been written to the OutputData, <code>commit()</code> will flush
 * it, forcing any buffered output to be written out. <code>rollback()</code> will
 * discard the contents of the buffer.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class AtomicResponseHelper {
//*******************************************************************
  // CONSTANTS
  //*******************************************************************
  private static final Logger log = Logger.getLogger(AtomicResponseHelper.class);

  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private ResponseWrapper atomicWrapper;
  
  public AtomicResponseHelper(ServletOverridableOutputData outputData) {
    atomicWrapper = new ResponseWrapper(outputData.getResponse());
    outputData.setResponse(atomicWrapper);
  }
  
  
  //*******************************************************************
  // PRIVATE CLASSES
  //*******************************************************************
  /**
   * Wraps a HttpServletResponse to make it possible of resetting and commiting it.
   */
  private class ResponseWrapper extends HttpServletResponseWrapper {
    private boolean committed = false;
    private ServletOutputStream out;
    private PrintWriter writerOut;

    public ResponseWrapper(HttpServletResponse arg0) {
      super(arg0);
  
      resetStream();
    }
    

    private void resetStream() {
      out = new AraneaServletOutputStream();
    }
    
    /**
     * Constructs a new writer with the current OutputStream and HttpServletResponse.
     */
    private void resetWriter() throws UnsupportedEncodingException {
      if (getResponse().getCharacterEncoding() != null) { 
        writerOut = new PrintWriter(new OutputStreamWriter(out, getResponse().getCharacterEncoding()));
      }
      else {
        writerOut = new PrintWriter(new OutputStreamWriter(out));
      }
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
      if (committed)
        return getResponse().getOutputStream();
      
      return out;
    }
    
    public PrintWriter getWriter() throws IOException {
      if (committed)
        return getResponse().getWriter();
      
      if (writerOut == null)
        resetWriter();
      
      return writerOut;
    }
    
    /**
     * If the output has not been commited yet, commits it.
     * @throws AraneaRuntimeException if output has been commited already. 
     */
    public void commit() throws IOException {
      if (committed)
        throw new IllegalStateException("Cannot commit buffer - response is already committed");
      
      if (writerOut != null)
        writerOut.flush();
      out.flush();
      
      byte[] data = ((AraneaServletOutputStream) out).getData();
      
      log.debug("Committed data size: " + data.length);
      
      getResponse().getOutputStream().write(data);
      getResponse().getOutputStream().flush();
      
      committed = true;
    }
    
    /**
     * If the output has not been commited yet, clears the content of the underlying
     * buffer in the response without clearing headers or status code.
     */
    public void reset() {
      if (committed)
        throw new IllegalStateException("Cannot reset buffer - response is already committed");
      resetStream();
      try {
        resetWriter();
      }
      catch (UnsupportedEncodingException e) {
        throw new AraneaRuntimeException(e);
      }
    }
  }
  
  private static class AraneaServletOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream out;
    
    private AraneaServletOutputStream() {
      out = new ByteArrayOutputStream(20480);
    }
    
    public void write(int b) throws IOException {
      out.write(b);
    }
    public void write(byte[] b) throws IOException {
      out.write(b);
    }
    public void write(byte[] b, int offset, int len) throws IOException{
       out.write(b, offset, len);
    }
    public void flush() throws IOException{
      out.flush();
    }
        
    private byte[] getData() {
      return out.toByteArray();
    }
  }
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************  
  
  public void commit() throws Exception {
    atomicWrapper.commit();
  }
  
  public void rollback() throws Exception {
    atomicWrapper.reset();
  }
}
