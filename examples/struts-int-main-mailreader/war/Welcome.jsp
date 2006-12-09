<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://araneaframework.org/tag-library/standard" prefix="ui" %>

<html>
<head>
    <title><bean:message key="index.title"/></title>
    <link rel="stylesheet" type="text/css" href="base.css"/>
</head>

<body>
<h3><bean:message key="index.heading"/></h3>
<ul>
    <li><html:link action="/EditRegistration"><bean:message
            key="index.registration"/></html:link></li>
    <li><html:link action="/Logon"><bean:message key="index.logon"/>
    </html:link></li>
</ul>

<h3>Language Options</h3>
<ul>
    <li><html:link action="/Locale?language=en">English</html:link></li>
    <li><html:link action="/Locale?language=ja" useLocalEncoding="true">
        Japanese</html:link></li>
    <li><html:link action="/Locale?language=ru" useLocalEncoding="true">
        Russian</html:link></li>
</ul>

<ui:strutsWidgetInclude id="list"/>

<hr/>

<p><html:img bundle="alternate" pageKey="struts.logo.path"
             altKey="struts.logo.alt"/></p>

<p><html:link action="/Tour"><bean:message key="index.tour"/></html:link></p>

</body>
</html>
