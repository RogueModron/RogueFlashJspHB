<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<title>RogueFlash - <c:out value="${PageBean.title}" /></title>
	
	<%@ include file="../jspf/headContent.jspf" %>
</head>
<body>

	<%@ include file="../jspf/noscript.jspf" %>
	<%@ include file="../jspf/menu.jspf" %>
	
	<div id="appScreen">
	
		<%@ include file="../jspf/pageHeader.jspf" %>
		
		<div class="pageBody">
			<div class="centered filter margin-bottom-2x">
				<input type="hidden" id="deckId" value="<c:out value='${PageBean.deckId}' />" />
				
				<label class="fieldLabel left"
					for="description">
					Description
				</label>
				<input id="description"
					type="text"
					class="field roundedAll"
					placeholder="Description"
					value="<c:out value='${PageBean.description}' />" />
					
				<label class="fieldLabel left"
					for="notes">
					Notes
				</label>
				<textarea id="notes"
					class="field roundedAll"
					placeholder="Notes"
					rows="5" ><%--
				--%><c:out value="${PageBean.notes}" /><%--
				--%></textarea>
			</div>
		</div>
	</div>
	
	<%@ include file="../jspf/scripts.jspf" %>
	
<script type="text/javascript">
	$(document).ready(function(){
		function updateDeck() {
			$.ajax({
				data: {
					deckId: $("#deckId").val(),
					description: $("#description").val(),
					notes: $("#notes").val()
				},
				dataType: "json",
				error: function(data) {
					//
				},
				method: "POST",
				success: function(data) {
					//
				},
				url: app.utils.getUrl("<c:url value='AjaxSaveDeck.go' />")
			});
		}
		app.utils.setValueObserver(
			$("#description"),
			updateDeck
		);
		app.utils.setValueObserver(
			$("#notes"),
			updateDeck
		);
		
		var appMenuOptions = {
			newDeckButton: true,
			newDeckUrl: "<c:url value='Deck.go' />",
			decksButton: true,
			decksUrl: "<c:url value='Decks.go' />",
			newCardButton: true,
			newCardUrl: "<c:url value='Card.go' />",
			cardsButton: true,
			cardsUrl: "<c:url value='Cards.go' />",
			reviewButton: true,
			reviewUrl: "<c:url value='Review.go' />"
		};
		var appMenuComponent = new app.components.AppMenuComponent(appMenuOptions);
		appMenuComponent.setDeckId($("#deckId").val());
		
		function deleteDeck() {
			$.ajax({
				data: {
					ids: $("#id").val()
				},
				dataType: "json",
				error: function(data) {
					//
				},
				method: "POST",
				success: function(data) {
					document.location = app.utils.getUrl("<c:url value='Decks.go' />");
				},
				url: app.utils.getUrl("<c:url value='AjaxDeleteDecks.go' />")
			});
		}
		var actionMenuOptions = {
			deleteAction: deleteDeck
		};
		var actionMenuComponent = new app.components.ActionMenuComponent(actionMenuOptions);
		actionMenuComponent.setActionMenu(true);
	});
</script>
</body>
</html>