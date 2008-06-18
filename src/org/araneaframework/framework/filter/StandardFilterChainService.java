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

import java.util.List;
import java.util.ListIterator;
import org.araneaframework.framework.FilterService;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardFilterChainService extends BaseFilterService {
  private List<FilterService> filterChain;  
  
  public void setFilterChain(List<FilterService> filterChain) {
    this.filterChain = filterChain;
  }
  
  @Override
  protected void init() throws Exception {      
    if (filterChain != null)
      for (ListIterator<FilterService> i = filterChain.listIterator(filterChain.size()); i.hasPrevious();) {
        FilterService filter = i.previous();
        
        filter.setChildService(childService);
        childService = filter;
      }
    
    filterChain = null;
    
    super.init();       
  }
}
