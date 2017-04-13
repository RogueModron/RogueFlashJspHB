package app.controllers.ajax;

import java.io.IOException;
import java.time.OffsetDateTime;

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
import app.model.entities.CardInstance;
import app.model.entities.CardPlan;
import app.model.entities.CardReview;
import app.model.planner.Planner;
import app.model.planner.PlannerResult;
import app.model.planner.ReviewValues;

@WebServlet("/AjaxSaveReview.go")
public class ControllerAjaxSaveReview extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executePost(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{
			saveReview(request);

			returnJson(response, "{}");
		}
		catch (Throwable t)
		{
			Log log = LogFactory.getLog(this.getClass());
			log.debug(t.getMessage());

			returnJsonError(response, "");
		}
	}

	private void saveReview(HttpServletRequest request)
	{
		EntityManager em = PersistenceManager.INSTANCE.getEM();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		try
		{
			CardDao cardDao = new CardDao(em);
			int cardInstanceId = getUnsignedIntParameter(request, "cardInstanceId");
			CardInstance cardInstance = cardDao.getCardInstance(cardInstanceId);
			if (cardInstance == null)
			{
				return;
			}

			OffsetDateTime now = OffsetDateTime.now();
			CardPlan plan = cardInstance.getPlan();
			if (plan.getNextDate() != null
					&& now.isBefore(plan.getNextDate()))
			{
				return;
			}

			int value = getUnsignedIntParameter(request, "value");
			ReviewValues reviewValue = ReviewValues.getFromOrdinal(value);
			if (reviewValue == null)
			{
				return;
			}

			PlannerResult plannerResult = Planner.planNext(
					reviewValue,
					now,
					plan.getLastDate());

			plan.setLastDate(now);
			plan.setLastDays(plannerResult.getPassedDays());
			plan.setNextDate(plannerResult.getNextDate());
			plan.setNextDays(plannerResult.getDaysNext());

			CardReview review = cardInstance.addReview();
			review.setDateTime(now);
			review.setValue(reviewValue);

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
