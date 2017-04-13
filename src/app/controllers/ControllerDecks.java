package app.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.beans.DecksBean;

@WebServlet("/Decks.go")
public class ControllerDecks extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		DecksBean pageBean = new DecksBean();
		pageBean.setTitle("Decks");
		setPageBean(request, pageBean);

		forwardPath(request, response, "decks.jsp");
	}
}
