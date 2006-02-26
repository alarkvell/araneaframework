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

package org.araneaframework.example.main.web.person;

import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.template.framework.context.PassthroughCallContextHandler;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.structure.filter.column.SimpleColumnFilter;


/**
 * This widget is for listing persons.
 * It returns selected person's Id or cancels current call.
 * It also allows a user to add or remove persons if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class PersonListWidget extends BaseWidget {
	
	private static final long serialVersionUID = 1L;

	protected static final Logger log = Logger.getLogger(PersonListWidget.class);
	
	private boolean editMode = false;
	
	private ListWidget list;
	
	public PersonListWidget() {
		super();
	}
	
	/**
	 * @param editMode whether to allow add or remove persons.
	 */
	public PersonListWidget(boolean editMode) {
		super();
		this.editMode = editMode;
	}
	
	protected void init() throws Exception {
		super.init();
		setViewSelector("person/personList");
		
		log.debug("TemplatePersonListWidget init called");    
		addGlobalEventListener(new ProxyEventListener(this));
		
		this.list = initList();
		addWidget("personList", this.list);
		
		putViewData("allowAdd", new Boolean(this.editMode));    
		putViewData("allowRemove", new Boolean(this.editMode));
	}
	
	protected ListWidget initList() throws Exception {
		BeanListWidget temp = new BeanListWidget(PersonMO.class);
		temp.setListDataProvider(new TemplatePersonListDataProvider());
		temp.addBeanColumn("id", "#Id", false);
		temp.addBeanColumn("name", "#First name", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addBeanColumn("surname", "#Last name", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addBeanColumn("phone", "#Phone no", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addBeanColumn("birthdate", "#Birthdate", true, new SimpleColumnFilter.Equals(), new DateControl());
		return temp;
	}
	
	protected void refreshList() throws Exception {  	
		this.list.getListDataProvider().refreshData();
	}
	
	public void handleEventAdd(String eventParameter) throws Exception {
		log.debug("Event 'add' received!");
		if (!this.editMode) {
			throw new RuntimeException("Event 'add' shoud be called only in edit mode");
		}
		getFlowCtx().start(new PersonEditWidget(), null, new FlowContext.Handler() {
			private static final long serialVersionUID = 1L;
			
			public void onFinish(Object returnValue) throws Exception {
				log.debug("Person added with Id of " + returnValue + " sucessfully");    
				refreshList();
			}
			public void onCancel() throws Exception {
				// ignore call cancel
			}
		});
	}
	
	public void handleEventRemove(String eventParameter) throws Exception {
		log.debug("Event 'remove' received!");
		if (!this.editMode) {
			throw new RuntimeException("Event 'remove' shoud be called only in edit mode");
		}
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
		getGeneralDAO().remove(PersonMO.class, id);
		refreshList();
		log.debug("Person with Id of " + id + " removed sucessfully");
	}
	
	public void handleEventSelect(String eventParameter) throws Exception {
		log.debug("Event 'select' received!");
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
		log.debug("Person selected with Id of " + id);
		PersonViewWidget newFlow = new PersonViewWidget(id);
		getFlowCtx().start(newFlow, null, null);
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
		log.debug("Event 'cancel' received!");
		getFlowCtx().cancel();
	}  
	
	private class TemplatePersonListDataProvider extends MemoryBasedListDataProvider {
		private static final long serialVersionUID = 1L;
		
		protected TemplatePersonListDataProvider() {
			super(PersonMO.class);
		}
		public List loadData() throws Exception {		
			return getGeneralDAO().getAll(PersonMO.class);
		}  	
	}
}
