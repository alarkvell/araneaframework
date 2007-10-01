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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseComponent;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ClassLoaderUtil;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * Enriches the environment with an implementation of the
 * {@link org.araneaframework.framework.LocalizationContext}. Children can use it and thus
 * provide Locale specific content. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardLocalizationFilterService extends BaseFilterService implements LocalizationContext {
  private static final Log log = LogFactory.getLog(StandardLocalizationFilterService.class);
  private String resourceBundleName;
  private Locale currentLocale;
  private List localeChangeListeners;

  /**
   * Set the name of the language, it must be a <b>valid ISO Language Code</b>. See the
   * language name in {@link Locale}.
   */
  public void setLanguageName(String languageName) {
    Assert.notNullParam(languageName, "languageName");
    
    setLocale(new Locale(languageName, ""));
  }
  
  /**
   * Sets the name of the resource bundle. 
   * @param resourceBundleName
   */
  public void setResourceBundleName(String resourceBundleName) {
    this.resourceBundleName = resourceBundleName;
  }

  public Locale getLocale() {
    return currentLocale;
  }

  public void setLocale(Locale currentLocale) {
    Assert.notNullParam(currentLocale, "currentLocale");
    
    if (!currentLocale.equals(getLocale())) {
      if (log.isDebugEnabled()) {
        log.debug("Current locale switched to: '" + currentLocale + "'.");
      }
      Locale old = getLocale();
      this.currentLocale = currentLocale;
      notifyLocaleChangeListeners(old, getLocale());
    }
  }
  
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), LocalizationContext.class, this);
  }
  
  public ResourceBundle getResourceBundle() {
    return getResourceBundle(currentLocale);
  }

  protected void init() throws Exception {
    childService._getComponent().init(getScope(), getChildEnvironment());
  }
  
  /** 
   * Gets a resource bundle using the specified resource bundle name and current locale
   * and the ClassLoaders provided by the ClassLoaderUtil.
   */
  public ResourceBundle getResourceBundle(Locale locale) {
    Assert.notNullParam(locale, "locale");
    
	  List loaders = ClassLoaderUtil.getClassLoaders();
	  
	  for (Iterator iter = loaders.iterator(); iter.hasNext();) {
		ClassLoader loader = (ClassLoader) iter.next();
		try {
			return ResourceBundle.getBundle(resourceBundleName, locale, loader);
		}
		catch(MissingResourceException e) {
			if (!iter.hasNext())
				throw e;
		}
	  }
     throw new MissingResourceException("No resource bundle for the specified base name can be found",
    		 							getClass().getName(), "");
  }

  public String localize(String key) {
    return getResourceBundle().getString(key);
  }
  
  public String getMessage(String code, Object[] args) {
    String message = localize(code);        
    return MessageFormat.format(message, args);
  }
  
  public String getMessage(String code, Object[] args, String defaultMessage) {
    String message = null;
    try {
      message = localize(code);
    }
    catch (MissingResourceException e) {
      message = defaultMessage;
    }
    
    return MessageFormat.format(message, args);
  }
  
  /** @since 1.1 */
  public void addLocaleChangeListener(final LocaleChangeListener listener) {
    if (listener == null)
      return;

    if (localeChangeListeners == null)
      localeChangeListeners = new ArrayList();

    ComponentUtil.addListenerComponent(listener, new LocaleChangeListenerDestroyerComponent(listener));
    localeChangeListeners.add(listener);
  }

  /** @since 1.1 */
  public boolean removeLocaleChangeListener(LocaleChangeListener listener) {
    if (listener == null || localeChangeListeners == null)
    return false;

    return localeChangeListeners.remove(listener);
  }

  /** @since 1.1 */
  protected void notifyLocaleChangeListeners(Locale oldLocale, Locale newLocale) {
    if (localeChangeListeners != null) {
      for (Iterator i = localeChangeListeners.iterator(); i.hasNext();) {
        LocaleChangeListener listener = (LocaleChangeListener) i.next();
        listener.onLocaleChange(oldLocale, newLocale);
      }
    }
  }
  
  private static final class LocaleChangeListenerDestroyerComponent extends BaseComponent {
    private final LocaleChangeListener listener;

    private LocaleChangeListenerDestroyerComponent(LocaleChangeListener listener) {
      this.listener = listener;
    }

    protected void destroy() throws Exception {
      LocalizationContext context = (LocalizationContext) getEnvironment().getEntry(LocalizationContext.class);
      if (context != null) {
        context.removeLocaleChangeListener(listener);
      }
    }
  }
}
