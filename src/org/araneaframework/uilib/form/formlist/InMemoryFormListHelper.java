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
 */

package org.araneaframework.uilib.form.formlist;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.formlist.adapter.InMemoryFormRowHandlerDecorator;
import org.araneaframework.uilib.form.formlist.model.MapFormListModel;

/**
 * Helper that facilitates holding the editable list rows in memory without
 * saving them to database. Useful when the editable list should be persisted
 * only in the very end of user session.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class InMemoryFormListHelper implements Serializable {

  private static final long serialVersionUID = 1L;

  protected Map tempKeys = new HashMap();

  protected Map added = new HashMap();

  protected Map updated = new HashMap();

  protected Set deleted = new HashSet();

  protected Map current = new LinkedMap();

  protected BaseFormListWidget formList;

  /**
   * Constructs the helper for given <i>formList</i>, filling it with initial
   * values.
   * 
   * @param formList
   * @param initialData initial row objects.
   */
  public InMemoryFormListHelper(BaseFormListWidget formList,
      Collection initialData) {
    this.formList = formList;
    if (initialData != null) {
      for (Iterator i = initialData.iterator(); i.hasNext();) {
        Object row = i.next();
        this.current.put(formList.getFormRowHandler().getRowKey(row), row);
      }
    }
    formList.setFormRowHandler(new InMemoryFormRowHandlerDecorator(
        formList.getFormRowHandler(), this));
    formList.setModel(new MapFormListModel(this.current));
  }

  /**
   * Adds a new row object assigning it temporary id.
   */
  public void add(Object row) {
    this.added.put(getTempKey(row), row);
    this.current.put(getTempKey(row), row);
  }

  /**
   * Adds a new row object using a provided key.
   */
  public void addWithKey(Object id, Object row) {
    this.added.put(id, row);
    this.current.put(id, row);
  }

  /**
   * Updates a row object by id.
   */
  public void update(Object id, Object row) {
    if (this.deleted.contains(id)) {
      throw new RuntimeException("Cannot update row with id '" + id + "'!");
    }
    if (this.added.containsKey(id)) {
      this.added.put(id, row);
    } else {
      this.updated.put(id, row);
    }
    this.current.put(id, row);
  }

  /**
   * Removes a row object by id.
   */
  public void delete(Object id) {
    if (this.deleted.contains(id)) {
      throw new RuntimeException("Cannot delete row with id '" + id + "'!");
    }
    if (this.added.containsKey(id)) {
      this.added.remove(id);
    } else {
      this.updated.remove(id);
      this.deleted.add(id);
    }
    this.current.remove(id);
  }

  protected static class RowWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object row;

    public RowWrapper(Object row) {
      this.row = row;
    }

    public Object getRow() {
      return this.row;
    }

    public String toString() {
      return "tempId@" + Integer.toHexString(System.identityHashCode(getRow()));
    }

    public boolean equals(Object obj) {
      Assert.notNullParam(obj, "obj");
      Assert.isInstanceOfParam(RowWrapper.class, obj, "obj");
      RowWrapper that = (RowWrapper) obj;
      return that.getRow() == this.getRow();
    }

    public int hashCode() {
      return System.identityHashCode(getRow());
    }
  }

  protected static class RowTempId implements Serializable {

    private static final long serialVersionUID = 1L;
  }

  /**
   * Returns a temporary key for the object (if object is new, assigns a new
   * temporary key).
   */
  public Object getTempKey(Object row) {
    Object rowWrapper = new RowWrapper(row);
    if (this.tempKeys.containsKey(rowWrapper)) {
      return this.tempKeys.get(rowWrapper);
    }
    Object result = new RowTempId();
    this.tempKeys.put(rowWrapper, result);
    return result;
  }

  /**
   * Returns row objects added during the session.
   */
  public Map getAdded() {
    return this.added;
  }

  /**
   * Returns row objects deleted during the session.
   */
  public Set getDeleted() {
    return this.deleted;
  }

  /**
   * Returns row objects updated during the session.
   */
  public Map getUpdated() {
    return this.updated;
  }

  /**
   * Returns current row objects.
   */
  public Map getCurrent() {
    return this.current;
  }
}
