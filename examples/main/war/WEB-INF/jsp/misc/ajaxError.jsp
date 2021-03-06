<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	When the Error button is clicked, a failing AJAX request is made.

	<ui:updateRegion id="ajaxErrorDemo">
		<ui:newLine/>
		<ui:eventButton labelId="#First" eventId="first" updateRegions="ajaxErrorDemo"/>
		<ui:eventButton labelId="#Second" eventId="second" updateRegions="ajaxErrorDemo"/>
		<ui:newLine/>
		<ui:eventLinkButton labelId="#Error on event" eventId="error" updateRegions="ajaxErrorDemo"/>
		<ui:newLine/>
		<ui:eventLinkButton labelId="#Error on render" eventId="renderError" updateRegions="ajaxErrorDemo"/>
	</ui:updateRegion>
</jsp:root>
