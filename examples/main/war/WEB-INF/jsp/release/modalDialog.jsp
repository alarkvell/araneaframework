<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<!-- Component starts here -->
	<ui:widgetContext>
		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName><fmt:message key="modalDialog.title"/></tui:componentName>
			</tui:componentHeader>

			<!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
			<tui:component>
				<c:if test="${!widget.nested}">
					<p><fmt:message key="modalDialog.intro"/></p>
				</c:if>

				<p><fmt:message key="modalDialog.howto"/></p>

				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">

					<!-- As we can insert rows now, we do just that. -->
					<ui:row>

						<!-- ... we can insert cells too! As we defined componentForm rowClass 
							to be cols4  we should insert 4 cells here... -->

						<ui:cell>
							<ui:label id="checkbox1" />
						</ui:cell>

						<ui:cell>
							<ui:formElement id="checkbox1">
								<!-- will draw a checkbox tied to form element with id "checkbox1" -->
								<ui:checkbox/>
							</ui:formElement>
						</ui:cell>

						<ui:cell>
							<ui:label id="textbox1" />
						</ui:cell>

						<ui:cell>
							<ui:textInput id="textbox1"/>
						</ui:cell>
					</ui:row>

					<!-- another row, and we just keep on going until all form elements have been described. -->
					<ui:row>
						<ui:formElement id="dateTime">
							<ui:cell>
								<!-- "dateTime" label -->
								<ui:label />
							</ui:cell>

							<ui:cell>
								<!-- "dateTime" input field -->
								<ui:dateTimeInput/>
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="number">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:floatInput />
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<ui:row>
						<ui:formElement id="date">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:dateInput />
							</ui:cell>
						</ui:formElement>

						<ui:formElement id="upload">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:fileUpload />
							</ui:cell>
						</ui:formElement>
					</ui:row>

				</tui:componentForm>

				<!-- pure design tag -->
				<tui:componentActions>
					<ui:eventButton eventId="nextFlow" labelId="modalDialog.startNext"/>

					<c:if test="${not widget.overlay}">
						<ui:eventButton eventId="nextFlowOverlay" labelId="modalDialog.startNext.overlay"/>
					</c:if>

					<ui:button id="button"/>

					<c:if test="${widget.nested}">
						<ui:eventButton eventId="return" labelId="modalDialog.previous"/>

						<c:if test="${widget.overlay}">
							<ui:eventButton eventId="close" labelId="modalDialog.closeOverlay"/>
							<ui:formEscapeKeyboardHandler handler="function(){Aranea.ModalBox.closeWithAjax.defer('close','${widgetId}');}"/>
						</c:if>
					</c:if>

				</tui:componentActions>
			</tui:component>

		</ui:form>
	</ui:widgetContext>

</jsp:root>
