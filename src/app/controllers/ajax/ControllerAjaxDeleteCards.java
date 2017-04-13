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

@WebServlet("/AjaxDeleteCards.go")
public class ControllerAjaxDeleteCards extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeDelete(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
	{
		try
		{
			deleteCards(request);

			returnJson(response, "{}");
		}
		catch (Throwable t)
		{
			Log log = LogFactory.getLog(this.getClass());
			log.debug(t.getMessage());

			returnJsonError(response, "");
		}
	}

	private void deleteCards(HttpServletRequest request)
	{
		int[] ids = getUnsignedIntArrayParameter(request, "ids");
		if (ids.length == 0)
		{
			return;
		}

		EntityManager em = PersistenceManager.INSTANCE.getEM();
		CardDao cardsDao = new CardDao(em);
		EntityTransaction transaction = em.getTransaction();
		for (int id : ids)
		{
			try
			{
				transaction.begin();
				cardsDao.deleteCard(id);
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
}
