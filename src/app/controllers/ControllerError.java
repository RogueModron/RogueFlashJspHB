package app.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebServlet("/Error.go")
public class ControllerError extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		logException(request);

		forwardPath(request, response, "error.jsp");
	}


	private void logException(HttpServletRequest request)
	{
		try
		{
			Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");
			if (t != null)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(System.lineSeparator());
				sb.append(t.toString());
				sb.append(System.lineSeparator());
				sb.append(t.getMessage());
				sb.append(System.lineSeparator());

				for (StackTraceElement stackTraceElement : t.getStackTrace())
				{
					sb.append("\t");
					sb.append(stackTraceElement.toString());
					sb.append(System.lineSeparator());
				}

				Log log = LogFactory.getLog(this.getClass());
				log.debug(sb.toString());
			}
		}
		catch (Throwable t)
		{
			//
		}
	}
}
