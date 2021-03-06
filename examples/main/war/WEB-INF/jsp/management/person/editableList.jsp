<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<!-- Main example's person editable list JSP - examples/main/war/WEB-INF/jsp/editableList.jsp -->
	<ui:widgetContext>

		<!-- Start the list context ... -->
		<ui:list id="list">

			<!-- and continue with announcement that this list is editable -->
			<ui:formList>

				<tui:componentHeader>
					<tui:componentName>
						<fmt:message key="persons.editable.title"/>
						<!-- We use the widget read-only property to resolve whether it uses memory- or DB-based data fetching: -->
						(<fmt:message key="${widget.memoryBased ? 'persons.memorybased' : 'persons.DBbased'}"/>)
					</tui:componentName>
				</tui:componentHeader>

				<tui:component>
					<tui:componentList>

						<tui:componentListHeader/>

							<!-- List filter for editable list is exactly the same as filter for ordinary lists -->
							<ui:listFilter>
								<ui:row styleClass="filter">
									<ui:cell/>

									<ui:formElement id="name">
										<ui:cell>
											<ui:textInput/>
										</ui:cell>
									</ui:formElement>

									<ui:formElement id="surname">
										<ui:cell>
											<ui:textInput/>
										</ui:cell>
									</ui:formElement>

									<ui:formElement id="phone">
										<ui:cell>
											<ui:textInput/>
										</ui:cell>
									</ui:formElement>

									<ui:cell>
										<ui:dateInput id="birthdate_start"/>
										<br/>
										<ui:dateInput id="birthdate_end"/>
									</ui:cell>

									<ui:cell>
										<ui:floatInput id="salary_start" styleClass="min"/>
										<br/>
										<ui:floatInput id="salary_end" styleClass="min"/>
									</ui:cell>

									<ui:cell>
										<ui:listFilterButton/>
									</ui:cell>
								</ui:row>
							</ui:listFilter>

							<!-- Editable list rows. This tag usage is similar to ui:listRows;
									 but it makes available some extra variables -->
							<ui:formListRows>
								<ui:row>
									<ui:cell>
										<!-- Default variable name for accessing the row object is "row" as in
												 ordinary lists. This could have been changed by specifying "var" 
												 attribute for ui:formListRows tag. -->
										<c:out value="${row.id}"/>
									</ui:cell>

									<c:choose>
										<!-- formRow variable is of class FormRow.ViewModel.
												 It holds the properties of form used to present current row object. -->
										<c:when test="${formRow.open}">
											<!-- When formRow is "open", render the fields as inputs. -->

											<ui:cell>
												<ui:textInput id="name"/>
											</ui:cell>

											<ui:cell>
												<ui:textInput id="surname"/>
											</ui:cell>

											<ui:cell>
												<ui:textInput id="phone"/>
											</ui:cell>

											<ui:formElement id="birthdate">
												<ui:cell>
													<ui:dateInput/>
												</ui:cell>
											</ui:formElement>

											<ui:formElement id="salary">
												<ui:cell>
													<ui:floatInput styleClass="w40"/>
												</ui:cell>
										 </ui:formElement>

										</c:when>
										<c:otherwise>

											<!-- When formRow is "closed", render the fields as displays. -->
											<ui:cell>
												<c:out value="${row.name}"/>
											</ui:cell>

											<ui:cell>
												<c:out value="${row.surname}"/>
											</ui:cell>

											<ui:cell>
												<c:out value="${row.phone}"/>
											</ui:cell>

											<ui:cell>
												<fmt:formatDate value="${row.birthdate}" pattern="dd.MM.yyyy"/>
											</ui:cell>

											<ui:cell>
												<c:out value="${row.salary}"/>
											</ui:cell>

										</c:otherwise>
									</c:choose>

									<ui:cell>
										<ui:attribute name="width" value="0"/>

										<!--Depending on formRow's current status, change button opens the formRow for editing or saves the
												formRow already opened. Buttons title should also be set accordingly. -->
										<c:set var="altEditText" value="${formRow.open ? 'persons.save' : 'persons.edit'}"/>

										<!-- EditSave event is produced by buttons created with  FormListUtil.addEditSaveButtonToRowForm() -->
										<ui:linkButton id="editSave" showLabel="false">
											<tui:image code="buttonChange" alt="${altEditText}" title="${altEditText}"/>
										</ui:linkButton>

										<!-- Delete event is produced by buttons created with FormListUtil.addDeleteButtonToRowForm() -->
										<ui:linkButton id="delete" showLabel="false">
											<tui:image code="buttonDelete" alt="persons.remove" title="persons.remove"/>
										</ui:linkButton>

									</ui:cell>
								</ui:row>
							</ui:formListRows>
			
						<!-- Finally the empty form for addition of new objects. -->
						<ui:formListAddForm>
							<ui:row>
								<!-- columns, usage is the same as when rendering a form widget -->
								<ui:cell/>

								<ui:formElement id="name">
									<ui:cell styleClass="center">
										<ui:textInput/>
									</ui:cell>
								</ui:formElement>

								<ui:formElement id="surname">
									<ui:cell>
										<ui:textInput/>
									</ui:cell>
								</ui:formElement>

								<ui:formElement id="phone">
									<ui:cell>
										<ui:textInput/>
									</ui:cell>
								</ui:formElement>

								<ui:formElement id="birthdate">
									<ui:cell>
										<ui:dateInput/>
									</ui:cell>
								</ui:formElement>

								<ui:formElement id="salary">
									<ui:cell>
											 <ui:floatInput styleClass="w40"/>
									</ui:cell>
								</ui:formElement>

								<ui:cell>
									<ui:attribute name="width" value="0"/>
									<ui:linkButton id="add" showLabel="false"><tui:image code="add" title="button.add"/></ui:linkButton>
								</ui:cell>
							</ui:row>
						</ui:formListAddForm>

					</tui:componentList>

					<!-- Sequence -->
					<tui:componentListFooter/>

				</tui:component>

			</ui:formList>
		</ui:list>

	</ui:widgetContext>
</jsp:root>