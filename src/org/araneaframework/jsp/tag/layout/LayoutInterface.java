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

package org.araneaframework.jsp.tag.layout;


/**
 * Interface for layout tags, defines no behaviour, only keys under which layout attributes
 * can be located from the context. Implementing classes should provide the attributes to inner
 * tags under these keys and should have some notion of rows and cells (typically layout tag
 * is just an HTML table). 
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public interface LayoutInterface {
  /** Key under which the layout tags can be found from the <code>PageContext</code>. */
  public static final String KEY = "org.araneaframework.jsp.tag.layout.LayoutInterface.KEY";
  
  /** Key under which the row's styleclasses defined by surrounding layout can be found from the <code>PageContext</code>. */
  public static final String ROWCLASS_KEY = "org.araneaframework.jsp.tag.layout.LayoutInterface.ROWCLASS_KEY";

  /** Key under which the cell's styleclasses defined by surrounding layout can be found from the <code>PageContext</code>. */
  public static final String CELLCLASS_KEY = "org.araneaframework.jsp.tag.layout.LayoutInterface.CELLCLASS_KEY";
}
