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

package org.araneaframework.uilib.form.formlist;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.formlist.adapter.InMemoryFormRowHandlerDecorator;
import org.araneaframework.uilib.form.formlist.model.MapFormListModel;

/**
 * Helper that facilitates holding the editable list rows in memory without saving them to database. 
 * Useful when the editable list should be persisted only in the very end of user session. 
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class InMemoryFormListHelper implements Serializable {
	protected Map<Object, Object> tempKeys = new HashMap<Object, Object>();
	
	protected Map<Object, Object> added = new HashMap<Object, Object>();
	protected Map<Object, Object> updated = new HashMap<Object, Object>();
	protected Set<Object> deleted = new HashSet<Object>();
	
	protected Map<Object, Object> current = new LinkedHashMap<Object, Object>();
	
	protected BaseFormListWidget formList;
	
	/**
	 * Constructs the helper for given <i>formList</i>, filling it with initial values.
	 * 
	 * @param formList
	 * @param initialData initial row objects.
	 */
	public InMemoryFormListHelper(BaseFormListWidget formList, Collection<Object> initialData) {
    this.formList = formList;
    
    if (initialData != null) {
      for (Iterator<Object> i = initialData.iterator(); i.hasNext();) {
        Object row = i.next();
        current.put(formList.getFormRowHandler().getRowKey(row), row);
      }
    }
    
    formList.setFormRowHandler(
            new InMemoryFormRowHandlerDecorator(formList.getFormRowHandler(), this));
    
    formList.setModel(new MapFormListModel(current));
	}
	
	/**
	 * Adds a new row object assigning it temporary id.
	 */
	public void add(Object row) {
		added.put(getTempKey(row), row);		
		current.put(getTempKey(row), row);
	}
	
	/**
	 * Adds a new row object using a provided key.
	 */
	public void addWithKey(Object id, Object row) {
		added.put(id, row);		
		current.put(id, row);	  
	}
	
	/**
	 * Updates a row object by id.
	 */
	public void update(Object id, Object row) {
		if (deleted.contains(id))
			throw new RuntimeException("Cannot update row with id '" + id + "'!");
		
		if (added.containsKey(id))
			added.put(id, row);
		else
			updated.put(id, row);
		
		current.put(id, row);
	}
	
	/**
	 * Removes a row object by id.
	 */
	public void delete(Object id) {
		if (deleted.contains(id))
			throw new RuntimeException("Cannot delete row with id '" + id + "'!");
		
		if (added.containsKey(id))
			added.remove(id);
		else {
			updated.remove(id);		
			deleted.add(id);
		}
		
		current.remove(id);
	}
	
	protected static class RowWrapper implements Serializable {
		private Object row;
		
		public RowWrapper(Object row) {
			this.row = row;
		}
		
		public Object getRow() {
			return this.row;
		}
		
		@Override
    public String toString() {
			return "tempId@" + Integer.toHexString(System.identityHashCode(getRow()));
		}
    
    @Override
    public boolean equals(Object obj) {
      Assert.notNullParam(obj, "obj");
      Assert.isInstanceOfParam(RowWrapper.class, obj, "obj");
      
      RowWrapper that = (RowWrapper) obj;
      return that.getRow() == this.getRow();
    }
    
    @Override
    public int hashCode() {
      return System.identityHashCode(getRow());
    }
	}
  
  protected static class RowTempId implements Serializable {
  }
	
	/**
	 * Returns a temporary key for the object (if object is new, assigns a new temporary key). 
	 */
	public Object getTempKey(Object row) {
    Object rowWrapper = new RowWrapper(row);
    
    if (tempKeys.containsKey(rowWrapper))
      return tempKeys.get(rowWrapper);
    
    Object result = new RowTempId();
    tempKeys.put(rowWrapper, result);
    
		return result;
	}
	
	/**
	 * Returns row objects added during the session.
	 */
	public Map<Object, Object> getAdded() {
		return this.added;
	}
	
	/**
	 * Returns row objects deleted during the session.
	 */
	public Set<Object> getDeleted() {
		return this.deleted;
	}
	
	/**
	 * Returns row objects updated during the session.
	 */
	public Map<Object, Object> getUpdated() {
		return this.updated;
	}
	
	/**
	 * Returns current row objects. 
	 */
	public Map<Object, Object> getCurrent() {
		return this.current;
	}
}
