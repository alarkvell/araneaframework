<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		<ui:form id="form">

			<tui:componentHeader>
				<tui:componentName>Filtered Form</tui:componentName>
			</tui:componentHeader>

			<!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
			<tui:component>
			
				<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">

					<!-- As we can insert rows now, we do just that. -->
					<ui:row>
						<ui:formElement id="filter">
							<ui:cell>
								<ui:label/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput/>
							</ui:cell>
						</ui:formElement>

						<ui:cell colspan="2"/>
					</ui:row>

					<!-- another row, and we just keep on going until all form elements have been described. -->
					<ui:row>
						<ui:formElement id="filtered">
							<ui:cell>
								<ui:label />
							</ui:cell>

							<ui:cell>
								<ui:textInput/>
							</ui:cell>
						</ui:formElement>

					</ui:row>

				</tui:componentForm>

				<!-- pure design tag -->
				<tui:componentActions>
					<ui:basicButton onclick="alert(Aranea.BrowserInfo.toString())" labelId="#DoitNOw"/>
				</tui:componentActions>
			</tui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
