package org.araneaframework.example.main.web.jsf.helloDuke;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.jsf.guessNumber.GuessNumberWidget;
import org.araneaframework.integration.jsf.JsfWidget;

public class TripleDukeWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("jsf/helloduke/tripleduke");
		addWidget("helloDuke1", new HelloDukeWidget());
		addWidget("helloDuke2", new GuessNumberWidget());
		addWidget("helloDuke3", new JsfWidget("/jsf/helloduke/greeting2.jsp"));
	}
}
