package app.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.beans.SessionBean;


public abstract class AbstractController extends HttpServlet
{
	private static final long serialVersionUID = 1L;


	public AbstractController()
	{
		super();
	}


	@Override
	protected final void doDelete(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		executeDelete(request, response);
	}

	@Override
	protected final void doGet(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		executeGet(request, response);
	}

	@Override
	protected final void doHead(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		throw new ServletException("Forbidden");
	}

	@Override
	protected final void doOptions(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		throw new ServletException("Forbidden");
	}

	@Override
	protected final void doPost(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		executePost(request, response);
	}

	@Override
	protected final void doPut(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		throw new ServletException("Forbidden");
	}

	@Override
	protected final void doTrace(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		throw new ServletException("Forbidden");
	}


	protected void executeDelete(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		throw new ServletException("Forbidden");
	}

	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		throw new ServletException("Forbidden");
	}

	protected void executePost(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		throw new ServletException("Forbidden");
	}


	protected void forwardPath(
			HttpServletRequest request,
			HttpServletResponse response,
			String path
			) throws ServletException, IOException
	{
		String finalPath = path;
		if (path.endsWith(".jsp"))
		{
			finalPath = "WEB-INF/jsp/" + path;
		}
		RequestDispatcher rd = request.getRequestDispatcher(response.encodeURL(finalPath));
		rd.forward(request, response);
	}

	protected void redirectPath(
			HttpServletResponse response,
			String path
			) throws IOException
	{
		response.sendRedirect(response.encodeURL(path));
	}

	protected void returnJson(
			HttpServletResponse response,
			String json
			) throws IOException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	protected void returnJsonError(
			HttpServletResponse response,
			String json
			) throws IOException
	{
		returnJson(response, json);
		response.setStatus(500);
	}


	protected void setSessionBean(
			HttpServletRequest request,
			SessionBean sessionBean)
	{
		request.getSession().setAttribute("SessionBean", sessionBean);
	}

	protected SessionBean getSessionBean(HttpServletRequest request) throws ServletException
	{
		SessionBean sessionBean = (SessionBean) request.getSession().getAttribute("SessionBean");
		if (sessionBean == null)
		{
			throw new ServletException("No session");
		}
		return sessionBean;
	}

	protected void invalidateSession(HttpServletRequest request) throws ServletException
	{
		request.getSession().invalidate();
		setSessionBean(request, null);
	}


	protected void setPageBean(
			HttpServletRequest request,
			Object pageBean)
	{
		request.setAttribute("PageBean", pageBean);
	}


	protected boolean getBooleanParameter(
			HttpServletRequest request,
			String parameter)
	{
		return Boolean.parseBoolean(request.getParameter(parameter));
	}

	protected String getTrimmedStringParameter(
			HttpServletRequest request,
			String parameter)
	{
		String string = request.getParameter(parameter);
		if (string == null)
		{
			return "";
		}
		return string.trim();
	}

	protected int[] getUnsignedIntArrayParameter(
			HttpServletRequest request,
			String parameter)
	{
		String value = request.getParameter(parameter);
		String[] stringValues = value.split(",");

		int numberOfElements = stringValues.length;
		if (numberOfElements == 0)
		{
			return new int[0];
		}

		try
		{
			int[] result = new int[numberOfElements];
			for (int i = 0, l = numberOfElements; i < l; i++)
			{
				result[i] = Integer.parseUnsignedInt(stringValues[i]);
			}
			return result;
		}
		catch (NumberFormatException e)
		{
			return new int[0];
		}
	}

	protected int getUnsignedIntParameter(
			HttpServletRequest request,
			String parameter)
	{
		try
		{
			return Integer.parseUnsignedInt(request.getParameter(parameter));
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}
}