<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">
	<!-- This is a master page of Aranea framework template application. -->

&lt;!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"&gt;
<html xmlns="http://www.w3.org/1999/xhtml">

	<ui:widgetContext>

		<head>
			<title>Aranea<ui:entity code="mdash"/>Java Web Framework Construction and Integration Kit</title>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
			<jsp:include page="scripts.jsp"/>
		</head>

		<ui:body>
			<div id="cont1">
				<ui:updateRegion globalId="araneaGlobalClientHistoryNavigationUpdateRegion" tag="div">
					<ui:systemForm id="aranea-form" method="post">
						<ui:registerScrollHandler/>
						<ui:registerPopups/>
						<ui:registerOverlay/>

						<!-- Renders the menu on top of the screen -->
						<jsp:include page="/WEB-INF/jsp/mainlayout/menu.jsp"/>
						<div class="stripe1"><ui:nbsp/></div>

						<div id="wholder">

							<!-- Renders the side menu on left side of screen -->
							<jsp:include page="/WEB-INF/jsp/mainlayout/sidemenu.jsp"/>

							<div id="content">
								<ui:updateRegion globalId="demo-messages" tag="div">
									<div class="msg-info">
										<div>
											<div>
												<ui:messages type="info"/>
											</div>
										</div>
									</div>
									<ui:messages type="error" styleClass="msg-error"/>
								</ui:updateRegion>

								<!-- Renders the menu widget itself. As MenuWidget is subclass of StandardFlowContainerWidget, this
								means that actual widget rendered here is whatever widget is on top of call stack at the moment of
								rendering: -->

								<ui:updateRegion globalId="demo-content" tag="div">
									<ui:widgetInclude id="menu"/>
								</ui:updateRegion>
							</div>

							<div class="clear1"><ui:nbsp/></div>
						</div>
					</ui:systemForm>
					<ui:updateRegion globalId="demo-footer" tag="div">
						<ui:widgetInclude id="menu.footer"/>
					</ui:updateRegion>
				</ui:updateRegion>
			</div>
		</ui:body>

	</ui:widgetContext>

</html>
</jsp:root>
