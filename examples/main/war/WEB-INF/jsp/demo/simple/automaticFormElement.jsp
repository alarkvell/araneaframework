<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
  version="2.1">

  <ui:widgetContext>
    <ui:form id="form">

      <tui:componentHeader>
        <tui:componentName>Automatic FormElement Demo</tui:componentName>
      </tui:componentHeader>

      <tui:component>
      
        <tui:componentForm>

          <ui:row>

            <ui:formElement id="first">
              <ui:cell styleClass="name">
                <ui:label />
              </ui:cell>

              <ui:cell styleClass="data">
                <ui:automaticFormElement />
              </ui:cell>
            </ui:formElement>

          </ui:row>

        </tui:componentForm>

        <tui:componentActions>
          <ui:eventButton eventId="reverse" labelId="#Reverse"/>
        </tui:componentActions>
      </tui:component>
    </ui:form>

  </ui:widgetContext>
</jsp:root>
