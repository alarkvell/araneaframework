package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.TimeControl;

/**
 * Time input form element tag.
 * 
 * @author Marko Muts
 * @jsp.tag
 *   name = "timeInput"
 *   body-content = "JSP"
 *   description = "Form time input field (custom control), represents UiLib "TimeControl"."
 */
public class FormTimeInputHtmlTag extends BaseFormDateTimeInputHtmlTag {

  {
    baseStyleClass = "aranea-time";
  }
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("TimeControl");

    // Prepare    
    String name = this.getScopedFullFieldId();
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);

    Long timeInputSize = DEFAULT_TIME_INPUT_SIZE;

    // Write
    out
        .write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");
    this.writeTimeInput(out, name, name, viewModel.getSimpleValue(),
        localizedLabel, timeInputSize, viewModel.isDisabled(),
        accessKey);

    Integer minute = null, hour = null;
    try {
      if (viewModel.getSimpleValue() != null) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(viewModel.getCurrentSimpleDateTimeFormat().parse(viewModel.getSimpleValue()));

  	    hour = new Integer(calendar.get(Calendar.HOUR_OF_DAY));
   	    minute = new Integer(calendar.get(Calendar.MINUTE));
      }
    } catch (ParseException e) {
        // try to preserve the contents of selects anyway
    	String strHour =  ServletUtil.getRequest(getOutputData().getInputData()).getParameter(name+".select1");
    	if (strHour != null && !(strHour.trim().length() == 0))
     	  hour = Integer.valueOf(strHour.trim());
    	String strMinute = ServletUtil.getRequest(getOutputData().getInputData()).getParameter(name+".select2");
    	if (strMinute != null && !(strMinute.trim().length() == 0))
    	  minute = Integer.valueOf(strMinute);
    }
    writeHourSelect(out, name, systemFormId, viewModel.isDisabled(), hour);
    writeMinuteSelect(out, name, viewModel.isDisabled(), minute);

    out.write("</td></tr></table>\n");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected void writeMinuteSelect(Writer out, String name, boolean disabled, Integer minute) throws IOException {
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);
    out.write("<select name='" + name + ".select2' onChange=\""
        + fillXJSCallConstructor("fillTimeText", systemFormId, name, name + ".select1", name + ".select2")
        + ";" + ((!disabled && events && viewModel.isOnChangeEventRegistered()) ? JspWidgetCallUtil.getSubmitScriptForEvent() : "") + "\"");

    if (disabled)
      out.write(" disabled=\"true\"");
    
    if (!disabled &&  events && viewModel.isOnChangeEventRegistered()) {
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	String precondition = onChangePrecondition == null ? "return (document." + systemFormId + "['" + name +"'].value.length==5)" : onChangePrecondition;
    	event.setEventPrecondition(precondition);
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }

    out.write(">\n");
    
    StringBuffer sb = new StringBuffer().append("<script type=\"text/javascript\">");
    sb.append("addOptions('"+name+".select2',60,").append(minute != null ? minute.toString():"null").append(");");
    sb.append("</script>\n</select>\n");
    
    out.write(sb.toString());
  }

  protected void writeHourSelect(Writer out, String name, String systemFormId, boolean disabled, Integer hour) throws IOException {
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);
    out.write("<select name='" + name + ".select1' onChange=\""
        + fillXJSCallConstructor("fillTimeText", systemFormId, name, name + ".select1", name + ".select2")
        + ";" + ((!disabled && events && viewModel.isOnChangeEventRegistered()) ? JspWidgetCallUtil.getSubmitScriptForEvent() : "") + "\"");
    if (disabled)
      out.write(" disabled=\"true\"");

    if (!disabled &&  events && viewModel.isOnChangeEventRegistered()) {
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	String precondition = onChangePrecondition == null ? "return (document." + systemFormId + "['" + name +"'].value.length==5)" : onChangePrecondition;
    	event.setEventPrecondition(precondition);
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }
    out.write(">\n");
    
    StringBuffer sb = new StringBuffer().append("<script type=\"text/javascript\">");
    sb.append("addOptions('"+name+".select1',24,").append(hour != null ? hour.toString():"null").append(");");
    sb.append("</script>\n</select>\n");
    
    out.write(sb.toString());
  }

  /**
   * Writes out time input
   */
  protected void writeTimeInput(Writer out, 
      String id, 
      String name,
      String value, 
      String label, 
      Long size, 
      boolean disabled,
      String accessKey) throws Exception {
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);
    // Write input tag
    JspUtil.writeOpenStartTag(out, "input");
    if (!StringUtils.isBlank(id))
      JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "label", label);
    JspUtil.writeAttribute(out, "tabindex", tabindex);

    if (!disabled && events && viewModel.isOnChangeEventRegistered()) {
        JspUtil.writeAttribute(out, "onfocus", "saveValue(this)");
        String precondition  = onChangePrecondition == null ? "return isChanged('" + name + "');" : onChangePrecondition;
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	event.setEventPrecondition(precondition);
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }

    StringBuffer onBlur = new StringBuffer(fillXJSCallConstructor("fillTimeSelect", systemFormId, name, name +".select1", name + ".select2") + ";");
    if (!disabled && events && viewModel.isOnChangeEventRegistered())
    	onBlur.append(JspWidgetCallUtil.getSubmitScriptForEvent());
    JspUtil.writeAttribute(out, "onblur", onBlur.toString());

    if (!StringUtils.isBlank(accessKey))
      JspUtil.writeAttribute(out, "accesskey", accessKey);
    if (disabled) 
      JspUtil.writeAttribute(out, "disabled", "true");

    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }

  protected String fillXJSCallConstructor(String function, String formId, String timeInputEl, String hourSelectEl, String minuteSelectEl) {
    return FormTimeInputHtmlTag.staticFillXJSCall(function, formId, timeInputEl, hourSelectEl, minuteSelectEl);
  }
  
  public static final String staticFillXJSCall(String function, String formId, String timeInputEl, String hourSelectEl, String minuteSelectEl) {
    return function + "(document." + formId + ", '" + timeInputEl + "', '"  +  hourSelectEl + "', '" + minuteSelectEl + "')";
  }
}
