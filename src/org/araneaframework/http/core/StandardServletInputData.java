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

package org.araneaframework.http.core;

import org.araneaframework.core.StandardPath;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.iterators.EnumerationIterator;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoCurrentOutputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpInputData;

/**
 * A ServletInputdata implementation which uses a {@link StandardPath} for determining the scope.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletInputData implements HttpInputData {

  private HttpServletRequest req;

  private Map extensions = new HashMap();

  private Map globalData = new HashMap();

  private Map scopedData = new HashMap();

  private boolean dataInited;

  private StringBuffer path;

  private LinkedList pathPrefixes = new LinkedList();

  private String servletPath;

  /**
   * Constructs a StandardServletInputData from the request.
   * 
   * @param request
   */
  public StandardServletInputData(HttpServletRequest request) {
    Assert.notNullParam(request, "request");

    this.servletPath = request.getServletPath();

    setRequest(request);
    extend(HttpServletRequest.class, this.req);
  }

  private void setRequest(HttpServletRequest request) {
    this.req = request;
    this.dataInited = false;

    this.path = new StringBuffer(req.getPathInfo() == null ? "" : req.getPathInfo());
    this.pathPrefixes = new LinkedList();
  }

  private void initData() {
    this.globalData.clear();
    this.scopedData.clear();

    Enumeration params = this.req.getParameterNames();

    while (params.hasMoreElements()) {
      String key = (String) params.nextElement();

      if (key.lastIndexOf(".") == -1) {
        // global data - no prefix data
        this.globalData.put(key, this.req.getParameter(key));
      } else {
        // scoped data
        String prefix = key.substring(0, key.lastIndexOf("."));
        String subKey = key.substring(key.lastIndexOf(".") + 1);

        Map map = (Map) this.scopedData.get(prefix);

        if (map == null) {
          map = new HashMap();
          this.scopedData.put(prefix, map);
        }

        map.put(subKey, this.req.getParameter(key));
      }
    }

    this.dataInited = true;
  }

  public Map getGlobalData() {
    if (!this.dataInited) {
      initData();
    }
    return Collections.unmodifiableMap(this.globalData);
  }

  public Map getScopedData(Path scope) {
    if (!this.dataInited) {
      initData();
    }
    Map result = (Map) scopedData.get(scope.toString());
    return result != null ? Collections.unmodifiableMap(result) : Collections.EMPTY_MAP;
  }

  public void extend(Class interfaceClass, Object implementation) {
    if (HttpServletRequest.class.equals(interfaceClass) && implementation != null) {
      setRequest((HttpServletRequest) implementation);
    }
    this.extensions.put(interfaceClass, implementation);
  }

  public Object narrow(Class interfaceClass) {
    Object extension = this.extensions.get(interfaceClass);
    if (extension == null) {
      throw new NoSuchNarrowableException(interfaceClass);
    }
    return extension;
  }

  public OutputData getOutputData() {
    OutputData output = (OutputData) this.req.getAttribute(OutputData.OUTPUT_DATA_KEY);
    if (output == null) {
      throw new NoCurrentOutputDataSetException("No OutputData set in the request.");
    } else {
      return output;
    }
  }

  public String getCharacterEncoding() {
    return this.req.getCharacterEncoding();
  }

  public String getContainerURL() {
    StringBuffer url = new StringBuffer();
    url.append(this.req.getScheme());
    url.append("://");
    url.append(this.req.getServerName());
    url.append(":");
    url.append(this.req.getServerPort());
    url.append(this.req.getContextPath());
    url.append(this.servletPath);
    return url.toString();
  }

  public String getContainerPath() {
    return this.servletPath;
  }

  public String getContextURL() {
    StringBuffer url = new StringBuffer();
    url.append(this.req.getScheme());
    url.append("://");
    url.append(this.req.getServerName());
    url.append(":");
    url.append(this.req.getServerPort());
    url.append(this.req.getContextPath());
    return url.toString();
  }

  public String getContextPath() {
    return this.req.getContextPath();
  }

  public String getRequestURL() {
    return this.req.getRequestURL().toString();
  }

  public String getContentType() {
    return this.req.getContentType();
  }

  public Locale getLocale() {
    return this.req.getLocale();
  }

  public Iterator getParameterNames() {
    return new EnumerationIterator(req.getParameterNames());
  }

  public String[] getParameterValues(String name) {
    return this.req.getParameterValues(name);
  }

  public String getPath() {
    return this.path.toString();
  }

  public void popPathPrefix() {
    this.path.insert(0, (String) pathPrefixes.removeLast());
  }

  public void pushPathPrefix(String pathPrefix) {
    Assert.notEmptyParam(pathPrefix, "pathPrefix");
    this.pathPrefixes.addLast(pathPrefix);
    this.path.delete(0, pathPrefix.length() - 1);
  }

  public void setCharacterEncoding(String encoding) {
    Assert.notEmptyParam(encoding, "encoding");

    try {
      req.setCharacterEncoding(encoding);
    } catch (UnsupportedEncodingException e) {
      ExceptionUtil.uncheckException(e);
    }
  }
}
