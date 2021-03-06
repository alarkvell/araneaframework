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

package org.araneaframework.uilib.util;

import java.util.Collection;
import java.util.List;
import org.araneaframework.uilib.support.SelectGroup;

/**
 * The base specification for containers that deal with display items.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface DisplayItemContainer<T> {

  /**
   * Adds a display item to the element.
   * 
   * @param item A non-<code>null</code> item to be added.
   */
  void addItem(T item);

  /**
   * Adds items from <code>Collection</code> to this <code>DisplayItemContainer</code>.
   * 
   * @param items A non-<code>null</code> <code>Collection</code> of display items.
   */
  void addItems(Collection<T> items);

  /**
   * Clears the currently held display items.
   */
  void clearItems();

  /**
   * Returns a list of all groups that this <code>DisplayItemContainer</code> currently holds. The returned groups must
   * also provide all the values that belong to that group.
   * 
   * @return A list of all contained display item groups.
   * @since 2.0
   */
  List<SelectGroup<T>> getGroups();

  /**
   * Returns a list of display items that this <code>DisplayItemContainer</code> currently holds.
   * 
   * @return A list of all contained display items.
   * @since 2.0
   */
  List<T> getAllItems();

  /**
   * Returns a list of display items that this <code>DisplayItemContainer</code> currently holds and that are enabled
   * for selection. When this container does not distinguish enabled/disabled display items, this method will always
   * return the same result as {@link #getAllItems()}.
   * 
   * @return A list of enabled display items.
   * @since 2.0
   */
  List<T> getEnabledItems();

  /**
   * Returns a list of display items that this <code>DisplayItemContainer</code> currently holds and that are disabled
   * from being selected. When this container does not distinguish enabled/disabled display items, this method will
   * always return an empty list.
   * 
   * @return A list of disabled display items.
   * @since 2.0
   */
  List<T> getDisabledItems();

  /**
   * Returns the index of the display item with the specified value.
   * 
   * @param value display item value.
   * @return The index of the display item with the specified value.
   * @deprecated Just don't use it. The index is meaningless.
   */
  @Deprecated
  int getValueIndex(String value);

  /**
   * Returns the type of the underlying beans that this container holds.
   * 
   * @return The type of the underlying beans.
   * @since 2.0
   */
  Class<T> getItemType();

  /**
   * When the underlying <code>DisplayItemContainer</code> uses a bean property to resolve its label, the method returns
   * the property name. Otherwise, returns <code>null</code>.
   * 
   * @return The property of a display item to resolve its label, or <code>null</code>.
   * @return 2.0
   */
  String getItemLabelProperty();

  /**
   * When the underlying <code>DisplayItemContainer</code uses a bean property to resolve its value, the method
   * returns the property name. Otherwise, returns <code>null</code>.
   * 
   * @return The property of a display item to resolve its value, or <code>null</code>.
   * @return 2.0
   */
  String getItemValueProperty();

}
