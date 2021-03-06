<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	version="2.1">
	<ui:widgetContext>
		<ui:form id="complexForm">

			<tui:componentHeader>
				<tui:componentName>
					<!-- The label, defined in DemoComplexForm class. -->
					<fmt:message key="${viewData.formLabel}"/>
				</tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<tui:componentForm>

					<ui:row>
						<ui:formElement id="beastSelection">
							<ui:cell width="30%"/>
							<ui:cell styleClass="name" width="20%">
								<ui:label styleClass="nowrap"/>
							</ui:cell>
							<ui:cell styleClass="inpt">
							<!--
								Note the updateRegions attribute - it specifies the regions that should be updated when event occurs
								(specifying this attribute also means that upon listener activation AJAX request is made instead of
								ordinary HTTP request.
								-->
								<ui:select updateRegions="ajaxBeasts"/>
							</ui:cell>
						</ui:formElement>
					</ui:row>

					<!--
						Here we define the update region. All tags inside the update region when AJAX request which update regions
						include "ajaxBeasts" is made.
					-->
					<ui:updateRegionRows id="ajaxBeasts">
	
						<!--
							A way to test whether form elements are present. As both selectedBeastDesc and concreteBeastControl are
							only added to the form if beast is selected, this needs to be done here.
						-->
						<c:if test="${not empty form.elements['concreteBeastControl']}">
							<ui:row>
								<ui:cell styleClass="wrap-centered" width="30%">
									<ui:textDisplay id="selectedBeastDesc" localizeText="true"/>
								</ui:cell>

								<ui:formElement id="concreteBeastControl">
									<ui:cell styleClass="name">
										<ui:label />
									</ui:cell>
									<ui:cell>
										<!-- Render MultiSelectControl with checkboxes. Instead ui:multiSelect could be used... -->
										<ui:checkboxMultiSelect type="vertical"/>
									</ui:cell>
								</ui:formElement>
							</ui:row>
						</c:if>

					</ui:updateRegionRows>

				</tui:componentForm>
			</tui:component>

		</ui:form>
	</ui:widgetContext>

</jsp:root>
