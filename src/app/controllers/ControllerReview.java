package app.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.beans.ReviewBean;
import app.daos.DeckDao;
import app.model.PersistenceManager;

@WebServlet("/Review.go")
public class ControllerReview extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		int deckId = getUnsignedIntParameter(request, "deckId");

		DeckDao deckDao = new DeckDao(PersistenceManager.INSTANCE.getEM());
		long numberOfCardInstancesToReview = deckDao.getNumberOfCardInstancesToReview(deckId);

		if (numberOfCardInstancesToReview > 0)
		{
			ReviewBean reviewBean = new ReviewBean(deckId);
			reviewBean.setTitle("Review");
			setPageBean(request, reviewBean);

			forwardPath(request, response, "review.jsp");
		}
		else
		{
			forwardPath(request, response, "Deck.go");
		}
	}
}
