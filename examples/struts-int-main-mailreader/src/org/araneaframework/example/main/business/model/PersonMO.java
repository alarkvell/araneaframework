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

package org.araneaframework.example.main.business.model;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>

 * @hibernate.class table="person" lazy="false"
 */
public class PersonMO implements GeneralMO {
	
	  private static final long serialVersionUID = 1L;
  private Long id;
	private String name;
	private String surname;
	private String phone;
	private Date birthdate;
	private BigDecimal salary;
	
	/**
	 * @hibernate.id column="id" generator-class="increment"
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @hibernate.property not-null="true"
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @hibernate.property
	 */	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	/**
	 * @hibernate.property
	 */
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @hibernate.property type="date"
	 */
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * @hibernate.property
	 */	
	public BigDecimal getSalary() {
		return salary;
	}
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
}
