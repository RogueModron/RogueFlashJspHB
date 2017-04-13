package app.controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.beans.CardBean;
import app.daos.CardDao;
import app.daos.DeckDao;
import app.model.PersistenceManager;
import app.model.entities.Card;
import app.model.entities.Deck;

@WebServlet("/Card.go")
public class ControllerCard extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		CardBean cardBean = null;

		int cardId = getUnsignedIntParameter(request, "cardId");
		if (cardId > 0)
		{
			cardBean = getCard(cardId);
		}
		else
		{
			int deckId = getUnsignedIntParameter(request, "deckId");
			if (deckId > 0)
			{
				cardBean = getNewCard(deckId);
			}
		}

		if (cardBean != null)
		{
			cardBean.setTitle("Card");
			setPageBean(request, cardBean);

			forwardPath(request, response, "card.jsp");
		}
		else
		{
			forwardPath(request, response, "Decks.go");
		}
	}


	private CardBean getCard(int cardId)
	{
		CardDao cardDao = new CardDao(PersistenceManager.INSTANCE.getEM());
		Card card = cardDao.getCard(cardId);

		CardBean cardBean = null;
		if (card != null)
		{
			cardBean = new CardBean(
					card.getDeck().getId(),
					card.getId(),
					card.getSideA(),
					card.getSideB(),
					card.getNotes(),
					card.getTags(),
					!card.getIntanceSideBToA().isDisabled());
		}
		return cardBean;
	}

	private CardBean getNewCard(int deckId)
	{
		CardBean cardBean;
		EntityManager em = PersistenceManager.INSTANCE.getEM();
		DeckDao deckDao = new DeckDao(em);
		Deck deck = deckDao.getDeck(deckId);
		em.getTransaction().begin();
		Card card = deck.addCard();
		deckDao.persistDeck(deck);
		em.getTransaction().commit();

		cardBean = new CardBean(
				card.getDeck().getId(),
				card.getId(),
				card.getSideA(),
				card.getSideB(),
				card.getNotes(),
				card.getTags(),
				!card.getIntanceSideBToA().isDisabled());
		return cardBean;
	}
}
