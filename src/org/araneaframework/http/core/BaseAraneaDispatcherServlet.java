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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.AraneaVersion;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.http.ServletServiceAdapterComponent;

/**
 * Aranea's main servlet. Routes GET & POST methods to the root component's
 * <code>service(HttpServletRequest, HttpServletResponse)</code> method.
 * <p>
 * Enriches the environment with objects of ServletContext and ServletConfig. They can be accessed later from the
 * environment under the keys ServletContext.class and ServletConfig.class respectively.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class BaseAraneaDispatcherServlet extends HttpServlet {

  private static final Log LOG = LogFactory.getLog(BaseAraneaDispatcherServlet.class);

  private ServletServiceAdapterComponent serviceAdapter;

  @Override
  public void init() throws ServletException {
    this.serviceAdapter = buildRootComponent();
    buildAlternateRootComponents();

    Environment env = new StandardEnvironment(null, getServletEnvironmentMap());
    try {
      this.serviceAdapter._getComponent().init(null, env);
    } catch (Exception e) {
      LOG.info("Unable to start " + AraneaVersion.getTitle() + " " + AraneaVersion.getVersion(), e);
      throw new ServletException(e.getMessage(), e);
    }

    LOG.info(AraneaVersion.getTitle() + " " + AraneaVersion.getVersion() + " started");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      getServiceAdapter(req).service(req, resp);
    } catch (Exception e) {
      throw new ServletException(e.getMessage(), e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      getServiceAdapter(req).service(req, resp);
    } catch (Exception e) {
      throw new ServletException(e.getMessage(), e);
    }
  }

  /**
   * Returns {@link ServletServiceAdapterComponent} that should service the given request. Default implementation
   * returns component built by {@link BaseAraneaDispatcherServlet#buildRootComponent()}.
   * 
   * @param req The current request data.
   * @since 1.0.7
   * @return ServletServiceAdapterComponent that should service the given request
   */
  protected ServletServiceAdapterComponent getServiceAdapter(HttpServletRequest req) {
    return this.serviceAdapter;
  }

  /**
   * Builds the default (and in most cases only) root component of this servlet. All the requests (GET & POST) through
   * some root component, when no alternate root components are defined, requests will always go through the root
   * component built here.
   * 
   * @return default root component
   */
  protected abstract ServletServiceAdapterComponent buildRootComponent();

  /**
   * Should build all alternate root component hierarchies, if applicable. Base implementation does not build any. If
   * this is overridden, also {@link BaseAraneaDispatcherServlet#getServiceAdapter(HttpServletRequest)} should be
   * overridden to choose the correct component hierarchy for processing the request.
   * 
   * @since 1.0.7
   */
  protected void buildAlternateRootComponents() {}

  protected Map<Class<?>, Object> getEnvironmentEntries() {
    return Collections.emptyMap();
  }

  /**
   * @return map with same entries as {@link BaseAraneaDispatcherServlet#getEnvironmentEntries()} plus servlet container
   *         specific entries (ServletContext, ServletConfig)
   * @since 1.0.7
   */
  protected Map<Class<?>, Object> getServletEnvironmentMap() {
    Map<Class<?>, Object> entries = new HashMap<Class<?>, Object>();
    entries.put(ServletContext.class, getServletContext());
    entries.put(ServletConfig.class, getServletConfig());
    entries.putAll(getEnvironmentEntries());
    return entries;
  }
}
