package app.controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.beans.DeckBean;
import app.daos.DeckDao;
import app.model.PersistenceManager;
import app.model.entities.Deck;

@WebServlet("/Deck.go")
public class ControllerDeck extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		DeckBean deckBean = null;

		int deckId = getUnsignedIntParameter(request, "deckId");
		if (deckId > 0)
		{
			deckBean = getDeck(deckId);
		}
		else
		{
			deckBean = getNewDeck();
		}

		if (deckBean != null)
		{
			deckBean.setTitle("Deck");
			setPageBean(request, deckBean);

			forwardPath(request, response, "deck.jsp");
		}
		else
		{
			forwardPath(request, response, "Decks.go");
		}
	}


	private DeckBean getDeck(int deckId)
	{
		DeckDao deckDao = new DeckDao(PersistenceManager.INSTANCE.getEM());
		Deck deck = deckDao.getDeck(deckId);

		DeckBean deckBean = null;
		if (deck != null)
		{
			deckBean = new DeckBean(
					deck.getId(),
					deck.getDescription(),
					deck.getNotes());
		}
		return deckBean;
	}

	private DeckBean getNewDeck()
	{
		DeckBean deckBean;
		Deck deck = new Deck("");
		EntityManager em = PersistenceManager.INSTANCE.getEM();
		DeckDao deckDao = new DeckDao(em);
		em.getTransaction().begin();
		deckDao.persistDeck(deck);
		em.getTransaction().commit();

		deckBean = new DeckBean(
				deck.getId(),
				deck.getDescription(),
				deck.getNotes());
		return deckBean;
	}
}
