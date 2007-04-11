<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<!-- Label -->
		<tui:componentHeader>
			<tui:componentName>Tutorial simple tree</tui:componentName>
        </tui:componentHeader>

		<tui:component>

			<p>Tree with events:</p>
			<ui:tree id="tree1"/>

			<p>Tree with actions:</p>
			<ui:tree id="tree2"/>

		</tui:component>

	</ui:widgetContext>

</jsp:root>