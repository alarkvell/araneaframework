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

package org.araneaframework.uilib.list.dataprovider;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;

/**
 * This class provides a basic list data provider implementation that may be used with SQL- or PL/SQL-based lists.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BackendListDataProvider<T> extends BaseListDataProvider<T> {

  private static final Log LOG = LogFactory.getLog(BackendListDataProvider.class);

  private Set<DataUpdateListener> dataUpdateListeners = new HashSet<DataUpdateListener>(1);

  public static final boolean USE_CACHE_BY_DEFAULT = false;

  protected ComparatorExpression orderExpr;

  protected Expression filterExpr;

  protected Long lastStart;

  protected Long lastCount;

  protected ListItemsData<T> lastItemRange;

  private boolean forceReload;

  protected boolean useCache = USE_CACHE_BY_DEFAULT;

  /**
   * Instantiates the backend list data provider and sets whether to use caching.
   * 
   * @param useCache whether to use caching.
   */
  public BackendListDataProvider(boolean useCache) {
    this.useCache = useCache;
  }

  /**
   * Instantiates the backend list data provider with cache disabled.
   */
  public BackendListDataProvider() {
  // empty
  }

  public void init() throws Exception {
  // for subclasses to implement if needed
  }

  public void destroy() throws Exception {
  // for subclasses to implement if needed
  }

  /**
   * Sets the filter of the list.
   */
  public void setFilterExpression(Expression filterExpr) {
    this.filterExpr = filterExpr;
    forceReload();
    notifyDataChangeListeners();
  }

  /**
   * Sets the order of the list.
   */
  public void setOrderExpression(ComparatorExpression orderExpr) {
    this.orderExpr = orderExpr;
    forceReload();
    notifyDataChangeListeners();
  }

  /**
   * Empty.
   */
  public void refreshData() throws Exception {
    forceReload();
    notifyDataChangeListeners();
  }

  /** @since 1.1 */
  protected void forceReload() {
    this.forceReload = true;
  }

  /**
   * Uses {@link ListDataProvider#getItemRange(Long, Long)}to retrieve the item.
   */
  public T getItem(Long index) throws Exception {
    return getItemRange(index, 1L).getItemRange().get(0);
  }

  /**
   * Returns the total item count.
   */
  public Long getItemCount() throws Exception {
    return getItemRange(0L, 1L).getTotalCount();
  }

  /**
   * Uses {@link ListDataProvider#getItemRange(Long, Long)}to retrieve all items.
   */
  public ListItemsData<T> getAllItems() throws Exception {
    return getItemRange(0L, null);
  }

  public ListItemsData<T> getItemRange(Long startIdx, Long count) throws Exception {
    boolean refresh = !this.useCache || this.forceReload;
    refresh = refresh || !ObjectUtils.equals(this.lastStart, startIdx);
    refresh = refresh || !ObjectUtils.equals(this.lastCount, count);

    if (refresh) {
      ListQuery query = new ListQuery();
      query.setListStructure(this.listStructure);
      query.setItemRangeStart(startIdx);
      query.setItemRangeCount(count);
      query.setFilterInfo(this.filterInfo);
      query.setOrderInfo(this.orderInfo);
      query.setFilterExpression(this.filterExpr);
      query.setOrderExpression(this.orderExpr);
      this.lastItemRange = getItemRange(query);

      if (LOG.isTraceEnabled()) {
        LOG.trace("Refreshing itemrange: startIdx=" + String.valueOf(startIdx) + ", count=" + String.valueOf(count));
      }
    }

    this.forceReload = false;
    this.lastStart = startIdx;
    this.lastCount = count;
    return this.lastItemRange;
  }

  /** @since 1.1 */
  protected void notifyDataChangeListeners() {
    for (DataUpdateListener listener : this.dataUpdateListeners) {
      listener.onDataUpdate();
    }
  }

  public void addDataUpdateListener(DataUpdateListener listener) {
    this.dataUpdateListeners.add(listener);
  }

  public void removeDataUpdateListener(DataUpdateListener listener) {
    this.dataUpdateListeners.remove(listener);
  }

  /**
   * This method should be overridden to return a range of items from the list data.
   */
  protected abstract ListItemsData<T> getItemRange(ListQuery query) throws Exception;
}
