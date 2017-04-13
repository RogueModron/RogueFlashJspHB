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
import app.daos.CardDao;
import app.model.PersistenceManager;
import app.model.entities.Card;

@WebServlet("/AjaxSaveCard.go")
public class ControllerAjaxSaveCard extends AbstractController
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
			saveCard(request);

			returnJson(response, "{}");
		}
		catch (Throwable t)
		{
			Log log = LogFactory.getLog(this.getClass());
			log.debug(t.getMessage());

			returnJsonError(response, "");
		}
	}

	private void saveCard(HttpServletRequest request)
	{
		int cardId = getUnsignedIntParameter(request, "cardId");

		EntityManager em = PersistenceManager.INSTANCE.getEM();
		CardDao cardDao = new CardDao(em);
		Card card = cardDao.getCard(cardId);
		if (card == null)
		{
			return;
		}

		String sideA = getTrimmedStringParameter(request, "sideA");
		String sideB = getTrimmedStringParameter(request, "sideB");
		String notes = getTrimmedStringParameter(request, "notes");
		String tags = getTrimmedStringParameter(request, "tags");
		boolean sideBToA = getBooleanParameter(request, "sideBToA");

		EntityTransaction transaction = em.getTransaction();
		try
		{
			transaction.begin();
			card.setSideA(sideA);
			card.setSideB(sideB);
			card.setNotes(notes);
			card.setTags(tags);
			card.getIntanceSideBToA().setDisabled(!sideBToA);
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
