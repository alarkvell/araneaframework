<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

	<jsp:directive.page import="org.araneaframework.http.util.ServletUtil"/>

	<!--  variables -->
	<c:set var="event" value="menuSelect"/>
	<c:set var="activeStyle" value="active"/>
	<c:set var="globalRegions" value="demo-messages,sidemenu,demo-content,demo-footer"/>

	<ui:widgetContext id="menu">
		<jsp:scriptlet>
			request.setAttribute("containerURL", ServletUtil.getInputData(request).getContainerURL());
		</jsp:scriptlet>

		<div id="leftcol">
		<ul id="menu2">
			<ui:updateRegion globalId="sidemenu" tag="div">
				<c:forEach items="${viewData.menu.subMenu}" var="topMenuItem">

					<c:if test="${topMenuItem.value.selected}">
						<c:forEach items="${topMenuItem.value.subMenu}" var="item">

							<li><c:if test="${item.value.selected}">
								<ui:eventLinkButton
									eventId="${event}"
									eventParam="${topMenuItem.value.label}.${item.value.label}"
									labelId="${item.value.label}"
									styleClass="${activeStyle}"
									globalUpdateRegions="${globalRegions}"/>

								<c:if test="${item.value.holder}">
									<ul>
										<c:forEach items="${item.value.subMenu}" var="subitem">
											<li><c:if test="${subitem.value.selected}">
												<ui:eventLinkButton
													eventId="${event}"
													eventParam="${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}"
													labelId="${subitem.value.label}"
													styleClass="${activeStyle}"
													globalUpdateRegions="${globalRegions}"/>
											</c:if>

											<c:if test="${not subitem.value.selected}">
												<ui:eventLinkButton
													eventId="${event}"
													eventParam="${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}"
													labelId="${subitem.value.label}"
													globalUpdateRegions="${globalRegions}"/>
											</c:if></li>
										</c:forEach>
									</ul>
								</c:if>

							</c:if>

							<c:if test="${not item.value.selected}">
								<ui:eventLinkButton
									eventId="${event}"
									eventParam="${topMenuItem.value.label}.${item.value.label}"
									labelId="${item.value.label}"
									globalUpdateRegions="${globalRegions}"/>
							</c:if></li>
						</c:forEach>
					</c:if>
				</c:forEach>
			</ui:updateRegion>
		</ul>
		</div>
	</ui:widgetContext>
</jsp:root>
