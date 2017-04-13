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
				<label class="filterLabel"
					for="filterText">
					Filter
				</label>
				<div class="inline">
					<input id="filterText"
						type="text"
						class="roundedLeft"
						placeholder="Filter" /><!--
				 --><button id="eraseFilter"
						class="borderNoneLeft">
						<span class="fa fa-times"></span>
					</button><!--
				 --><button id="executeFilter"
						class="borderNoneLeft roundedRight">
						<span class="fa fa-search"></span>
					</button>
				</div>
			</div>
			
			<div id="itemsList"
				style="height: 666px; overflow: auto; display: none">
				<input type="hidden" id="deckId" value="<c:out value='${PageBean.deckId}' />" />
				<template id="itemTemplate">
					<div class="itemContainerWrapper">
						<input type="hidden" class="itemId" value="{cardId}" />
						<div class="floatLeft itemContainer roundedAll">
							<div class="itemTopSection roundedTop">
								<div class="itemTitleContainer roundedTop">
									<span class="inline itemTitle roundedTop">
										{sideA}
									</span>
								</div>
							</div>
							<div class="itemTopSection roundedBottom">
								<div class="itemTitleContainer roundedBottom">
									<span class="inline itemTitle roundedBottom">
										{sideB}
									</span>
								</div>
							</div>
							<div class="floatLeft itemBottomSection roundedAll">
								<div class="floatLeft itemSelectorContainer">
									<button class="itemSelector roundedAll">
										<span class="fa-stack">
											<i class="fa fa-circle-thin fa-stack-2x"></i>
											<i class="fa fa-check fa-stack-1x"></i>
										</span>
									</button>
								</div>
								<div class="floatLeft itemNotesContainer left">
									<span class="itemNotes">
										{notes}
									</span>
								</div>
								<div class="floatLeft">
									<span class="">
										{tags}
									</span>
								</div>
							</div>
						</div>
						<div class="clearfix"></div>
					</div>
				</template>
				
				<div class="centered">
					<button id="loadItems"
						class="roundedAll">
						<span class="fa fa-paw"></span>
						Load More
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="../jspf/scripts.jspf" %>
	
<script type="text/javascript">
	$(document).ready(function() {
		var filterComponent = new app.components.FilterComponent(
			"<c:url value='AjaxFilterCards.go' />",
			{
				deckId: $("#deckId").val()
			},
			"<c:url value='Card.go' />",
			"cardId",
			function(
					templateHtml,
					item) {
				function formatSideText(text) {
					var parts = text.split("\n")
					var textToShow = parts[0];
					if (textToShow === "") {
						return "---";
					}
					return app.utils.encodeHtmlWithFormat(textToShow);
				}
				return templateHtml
					.replace("{cardId}", item.cardId)
					.replace("{sideA}", formatSideText(item.sideA))
					.replace("{sideB}", formatSideText(item.sideB))
					.replace("{notes}", app.utils.encodeHtmlWithFormat(item.notes))
					.replace("{tags}", app.utils.encodeHtml(item.tags));
			}
		);
		
		var appMenuOptions = {
			newDeckButton: true,
			newDeckUrl: "<c:url value='Deck.go' />",
			editDeckButton: true,
			editDeckUrl: "<c:url value='Deck.go' />",
			decksButton: true,
			decksUrl: "<c:url value='Decks.go' />",
			newCardButton: true,
			newCardUrl: "<c:url value='Card.go' />",
			reviewButton: true,
			reviewUrl: "<c:url value='Review.go' />",
		};
		var appMenuComponent = new app.components.AppMenuComponent(appMenuOptions);
		appMenuComponent.setDeckId($("#deckId").val());
		
		function deleteCards() {
			var ids = filterComponent.getSelectedItemsId();
			if (ids.length === 0) {
				return;
			}
			$.ajax({
				dataType: "json",
				error: function(data) {
					//
				},
				method: "DELETE",
				success: function(data) {
					filterComponent.deleteItems(ids);
					actionMenuComponent.setActionMenu(false);
				},
				url: app.utils.getUrl(
					"<c:url value='AjaxDeleteCards.go' />",
					{
						ids: ids.join(",")
					}
				)
			});
		}
		var actionMenuOptions = {
			deleteAction: deleteCards
		};
		var actionMenuComponent = new app.components.ActionMenuComponent(actionMenuOptions);
		actionMenuComponent.setActionMenu(false);
		
		$(filterComponent).on("selector", function(e, data) {
			actionMenuComponent.setActionMenu(data.selected);
		});
		
		$("#executeFilter").click();
		
		$("#itemsList").show();
		function resizeItemsList() {
			var jItemsList = $("#itemsList");
			
			var windowH = $(window).height();
			var itemsListH = jItemsList.offset().top
			
			var newItemsListH = windowH - itemsListH - 70;
			if (newItemsListH < 200) {
				newItemsListH = 200;
			};
			jItemsList.height(newItemsListH);
		}
		resizeItemsList();
		$(window).resize(resizeItemsList);
	});
</script>
</body>
</html>