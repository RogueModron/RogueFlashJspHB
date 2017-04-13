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
import app.daos.DeckDao;
import app.model.PersistenceManager;
import app.model.beans.FindDecksResultBean;

@WebServlet("/AjaxFilterDecks.go")
public class ControllerAjaxFilterDecks extends AbstractController
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
		String filterText = getTrimmedStringParameter(request, "filterText");
		int firstResult = getUnsignedIntParameter(request, "firstResult");
		int maxResults = getUnsignedIntParameter(request, "maxResults");

		if (maxResults > 100)
		{
			maxResults = 100;
		}

		DeckDao deckDao = new DeckDao(PersistenceManager.INSTANCE.getEM());
		List<FindDecksResultBean> decks = deckDao.findDecks(
				filterText,
				firstResult,
				maxResults);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (FindDecksResultBean deck : decks)
		{
			JsonObject jo = Json.createObjectBuilder()
				.add("deckId", deck.getId())
				.add("description", deck.getDescription())
				.add("notes", deck.getNotes())
				.add("numberOfSides", deck.getNumberOfSides())
				.build();
			jab.add(jo);
		}
		return jab.build().toString();
	}
}
