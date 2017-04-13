package app.controllers.ajax;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
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
import app.model.entities.CardInstance;

@WebServlet("/AjaxGetCardToReview.go")
public class ControllerAjaxGetCardToReview extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{
			String jsonResult = buildJsonResult(request);
			returnJson(response, jsonResult);
		}
		catch (Throwable t)
		{
			Log log = LogFactory.getLog(this.getClass());
			log.debug(t.getMessage());

			returnJsonError(response, "");
		}
	}

	private String buildJsonResult(HttpServletRequest request)
	{
		int deckId = getUnsignedIntParameter(request, "deckId");
		if (deckId == 0)
		{
			return "{}";
		}

		CardDao cardDao = new CardDao(PersistenceManager.INSTANCE.getEM());
		CardInstance cardInstance = cardDao.getNextCardInstanceToReview(deckId);
		if (cardInstance == null)
		{
			return "{}";
		}

		Card card = cardInstance.getCard();

		JsonObject jo = Json.createObjectBuilder()
			.add("cardId", card.getId())
			.add("sideA", card.getSideA())
			.add("sideB", card.getSideB())
			.add("notes", card.getNotes())
			.add("tags", card.getTags())
			.add("cardInstanceId", cardInstance.getId())
			.add("sideAToB", cardInstance.isSideAToB())
			.build();
		return jo.toString();
	}
}
