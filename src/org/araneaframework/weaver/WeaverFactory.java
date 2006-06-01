/**
 * 
 */
package org.araneaframework.weaver;

import org.w3c.dom.Element;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public interface WeaverFactory {
  public WeaverElement buildElement(Element el);
  public WeaverElementBody buildBody(Element el);
}
