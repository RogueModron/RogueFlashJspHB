package app.controllers.ajax;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import app.controllers.AbstractController;
import app.daos.DeckDao;
import app.model.PersistenceManager;
import app.model.entities.Deck;

@WebServlet("/AjaxSaveDeck.go")
public class ControllerAjaxSaveDeck extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executePost(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		try
		{
			saveDeck(request);

			returnJson(response, "{}");
		}
		catch (Throwable t)
		{
			Log log = LogFactory.getLog(this.getClass());
			log.debug(t.getMessage());

			returnJsonError(response, "");
		}
	}

	private void saveDeck(HttpServletRequest request)
	{
		int deckId = getUnsignedIntParameter(request, "deckId");

		EntityManager em = PersistenceManager.INSTANCE.getEM();
		DeckDao deckDao = new DeckDao(em);
		Deck deck = deckDao.getDeck(deckId);
		if (deck == null)
		{
			return;
		}

		String description = getTrimmedStringParameter(request, "description");
		String notes = getTrimmedStringParameter(request, "notes");

		EntityTransaction transaction = em.getTransaction();
		try
		{
			transaction.begin();
			deck.setDescription(description);
			deck.setNotes(notes);
			transaction.commit();
		}
		finally
		{
			if (transaction != null
					&& transaction.isActive())
			{
				transaction.rollback();
			}
		}
	}
}
