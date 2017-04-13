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
				<input type="hidden" id="cardId" value="<c:out value='${PageBean.cardId}' />" />
				
				<label class="fieldLabel left"
					for="sideA">
					Side A
				</label>
				<textarea id="sideA"
					class="field roundedAll"
					placeholder="Side A"
					rows="5" ><%--
				--%><c:out value="${PageBean.sideA}" /><%--
				--%></textarea>
				
				<label class="fieldLabel left"
					for="sideB">
					Side B
				</label>
				<textarea id="sideB"
					class="field roundedAll"
					placeholder="Side B"
					rows="5" ><%--
				--%><c:out value="${PageBean.sideB}" /><%--
				--%></textarea>
				
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
				
				<label class="fieldLabel left"
					for="tags">
					Tags
				</label>
				<input id="tags"
					type="text"
					class="field roundedAll"
					placeholder="Tags"
					value="<c:out value='${PageBean.tags}' />" />
					
				<label class="fieldLabel left"
					for="sideBToA">
					Side B to A
				</label>
				<input id="sideBToA"
					type="checkbox"
					class="fieldCheckBox"
					value="true"
					<c:if test="${PageBean.sideBToA}" >checked</c:if> />
			</div>
		</div>
	</div>
	
	<%@ include file="../jspf/scripts.jspf" %>
	
<script type="text/javascript">
	$(document).ready(function(){
		function updateCard() {
			$.ajax({
				data: {
					cardId: $("#cardId").val(),
					sideA: $("#sideA").val(),
					sideB: $("#sideB").val(),
					notes: $("#notes").val(),
					tags: $("#tags").val(),
					sideBToA: $("#sideBToA").is(":checked")
				},
				dataType: "json",
				error: function(data) {
					//
				},
				method: "POST",
				success: function(data) {
					//
				},
				url: app.utils.getUrl("<c:url value='AjaxSaveCard.go' />")
			});
		}
		app.utils.setValueObserver(
			$("#sideA"),
			updateCard
		);
		app.utils.setValueObserver(
			$("#sideB"),
			updateCard
		);
		app.utils.setValueObserver(
			$("#notes"),
			updateCard
		);
		app.utils.setValueObserver(
			$("#tags"),
			updateCard
		);
		$("#sideBToA").click(updateCard);
		
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
		
		function deleteCard() {
			$.ajax({
				data: {
					ids: $("#cardId").val()
				},
				dataType: "json",
				error: function(data) {
					//
				},
				method: "POST",
				success: function(data) {
					document.location = app.utils.getUrl("<c:url value='Cards.go' />");
				},
				url: app.utils.getUrl("<c:url value='AjaxDeleteCards.go' />")
			});
		}
		var actionMenuOptions = {
			deleteAction: deleteCard
		};
		var actionMenuComponent = new app.components.ActionMenuComponent(actionMenuOptions);
		actionMenuComponent.setActionMenu(true);
	});
</script>
</body>
</html>