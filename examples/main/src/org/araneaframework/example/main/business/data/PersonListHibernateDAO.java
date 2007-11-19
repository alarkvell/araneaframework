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

package org.araneaframework.example.main.business.data;

import org.araneaframework.backend.list.hibernate.ListHqlHelper;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.example.main.business.model.PersonMO;
import org.hibernate.SessionFactory;


/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class PersonListHibernateDAO {

	protected SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public ListItemsData getItems(ListQuery request) {
		ListHqlHelper helper = new ListHqlHelper(sessionFactory, request);
		helper.setSimpleSqlQuery(PersonMO.class);
		return helper.execute();
	}
}
