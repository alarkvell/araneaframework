package org.araneaframework.example.common.framework;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirection servlet that should be mapped at "/".
 */
public class RedirectionServlet extends HttpServlet {
	@Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!request.getServletPath().trim().equals("/"))
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Accessing something besides '/' and mapped URLs!");
		else
			response.sendRedirect(getServletConfig().getInitParameter("webapp-root"));
	}
}
