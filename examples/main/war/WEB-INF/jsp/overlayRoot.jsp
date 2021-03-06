<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">
	<ui:systemForm id="aranea-overlay-form" method="post">
		<ui:messages type="error" styleClass="msg-error"/>
		<ui:messages type="warning" styleClass="msg-warning"/>
		<div class="msg-info">
			<div>
				<div>
					<ui:messages type="info"/>
				</div>
			</div>
		</div>
		<ui:widgetInclude id="c"/>
	</ui:systemForm>
</jsp:root>
