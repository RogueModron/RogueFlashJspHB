package app.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.daos.DeckDao;
import app.model.PersistenceManager;

@WebServlet("/Start.go")
public class ControllerStart extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		DeckDao deckDao = new DeckDao(PersistenceManager.INSTANCE.getEM());
		if (deckDao.checkDecksExistance())
		{
			redirectPath(response, "Decks.go");
		}
		else
		{
			redirectPath(response, "Deck.go");
		}
	}
}
