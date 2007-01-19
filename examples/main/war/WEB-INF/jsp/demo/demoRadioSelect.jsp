<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName>RadioSelectDemo</tui:componentName>
		</tui:componentHeader>
		
		<ui:form id="form">
			<tui:component>

				<tui:componentForm>
					<ui:row>
						<ui:formElement id="select">
							<ui:cell>
								<ui:label/>
							</ui:cell>
							<ui:cell>
								<ui:radioSelect/>
							</ui:cell>
						</ui:formElement>
					</ui:row>
				</tui:componentForm>
				
				<tui:componentActions>
					<ui:eventButton eventId="test" labelId="#Test"/>
				</tui:componentActions>

			</tui:component>
		</ui:form>
	</ui:widgetContext>
</jsp:root>
