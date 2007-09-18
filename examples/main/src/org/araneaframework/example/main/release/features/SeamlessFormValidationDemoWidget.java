package org.araneaframework.example.main.release.features;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.data.DateData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SeamlessFormValidationDemoWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("release/features/seamlessFormValidation/demo");
		addWidget("form1", buildFormWidget());
		FormWidget f2 = buildFormWidget();
		f2.enableBackgroundValidation();
		addWidget("form2", f2);
	}

	private FormWidget buildFormWidget() {
		FormWidget form = new FormWidget();
		form.addElement("futureDate", "seamless.appointmentdate", new DateControl(), new DateData(), true);
		return form;
	}
}