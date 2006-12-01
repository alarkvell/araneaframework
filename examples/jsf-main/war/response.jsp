<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:ui="http://araneaframework.org/tag-library/standard_rt"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:h="http://java.sun.com/jsf/html"
    version="2.0">

    <f:view>
    <h:form id="responseform">
    <h:graphicImage id="waveImg" url="/wave.med.gif" />
    <h2>Hi, <h:outputText id="userLabel"
			   value="#{UserNameBean.userName}" /> </h2>
	 <h:commandButton id="back" value="Back" action="success" 
                           />
         <p>
    </h:form>
    </f:view>
</jsp:root>

