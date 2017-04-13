package app.controllers.ajax;

import java.io.IOException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
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
import app.model.beans.FindCardsResultBean;

@WebServlet("/AjaxFilterCards.go")
public class ControllerAjaxFilterCards extends AbstractController
{
	private static final long serialVersionUID = 1L;


	@Override
	protected void executeGet(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException
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

		String filterText = getTrimmedStringParameter(request, "filterText");
		int firstResult = getUnsignedIntParameter(request, "firstResult");
		int maxResults = getUnsignedIntParameter(request, "maxResults");

		if (maxResults > 100)
		{
			maxResults = 100;
		}

		CardDao cardDao = new CardDao(PersistenceManager.INSTANCE.getEM());
		List<FindCardsResultBean> cards = cardDao.findCards(
				deckId,
				filterText,
				firstResult,
				maxResults);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (FindCardsResultBean card : cards)
		{
			JsonObject jo = Json.createObjectBuilder()
				.add("cardId", card.getId())
				.add("sideA", card.getSideA())
				.add("sideB", card.getSideB())
				.add("notes", card.getNotes())
				.add("tags", card.getTags())
				.build();
			jab.add(jo);
		}
		return jab.build().toString();
	}
}
