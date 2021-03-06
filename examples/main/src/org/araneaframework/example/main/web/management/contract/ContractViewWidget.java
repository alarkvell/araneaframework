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

package org.araneaframework.example.main.web.management.contract;

import org.araneaframework.example.main.web.management.company.CompanyViewWidget;

import org.araneaframework.example.main.web.management.person.PersonViewWidget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;

/**
 * This widget is for displaying a contract. It cancels current call only.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractViewWidget extends TemplateBaseWidget {

  private static final Log LOG = LogFactory.getLog(ContractViewWidget.class);

  private Long id = null;

  private ContractMO contract;

  /**
   * @param id Company's Id.
   */
  public ContractViewWidget(Long id) {
    this.id = id;
  }

  @Override
  protected void init() throws Exception {
    LOG.debug("TemplateContractViewWidget init called");
    setViewSelector("management/contract/contractView");

    this.contract = getGeneralDAO().getById(ContractMO.class, this.id);
    putViewData("contract", this.contract);
  }

  public void handleEventViewCompany() {
    getFlowCtx().start(new CompanyViewWidget(this.contract.getCompany().getId()));
  }

  public void handleEventViewPerson() {
    getFlowCtx().start(new PersonViewWidget(this.contract.getPerson().getId()));
  }

  public void handleEventReturn() {
    getFlowCtx().cancel();
  }
}
