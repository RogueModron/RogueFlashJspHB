package app.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.beans.CardsBean;

@WebServlet("/Cards.go")
public class ControllerCards extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		int deckId = getUnsignedIntParameter(request, "deckId");

		if (deckId > 0)
		{
			CardsBean pageBean = new CardsBean(deckId);
			pageBean.setTitle("Cards");
			setPageBean(request, pageBean);

			forwardPath(request, response, "cards.jsp");
		}
		else
		{
			forwardPath(request, response, "Decks.go");
		}
	}
}
