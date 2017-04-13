var app = {};
app.utils = {};
app.components = {};

app.utils.checkDefault = function checkDefault(
		value,
		defaultValue) {
	if (typeof(value) === "undefined") {
		return defaultValue;
	} else {
		if (typeof(value) !== "string") {
			throw "Argument not valid";
		}
	}
	return value;
};

app.utils.encodeHtml = function encodeHtml(text) {
	return text
		.replace(/&/g, "&amp;")
		.replace(/>/g, "&gt;")
		.replace(/</g, "&lt;")
		.replace(/"/g, "&quot;")
		.replace(/'/g, "&#39;");
};

app.utils.encodeHtmlWithFormat = function encodeHtmlWithFormat(text) {
	return app.utils.encodeHtml(text)
		.replace(/&lt;b&gt;/g,   "<b>")
		.replace(/&lt;\/b&gt;/g, "</b>")
		.replace(/&lt;i&gt;/g,   "<i>")
		.replace(/&lt;\/i&gt;/g, "</i>")
		.replace(/&lt;u&gt;/,    "<u>")
		.replace(/&lt;\/u&gt;/,  "</u>")
		.replace(/\n/g,          "<br/>");
};

app.utils.getUrl = function getUrl(
		url,
		params) {
	var paramsUrl = "";
	if (params !== null) {
		if ($.isArray(params)) {
			paramsUrl = params.join("&");
		} else if (typeof(params) === "object") {
			paramsUrl = $.param(params);
		} else if (typeof(params) === "string") {
			paramsUrl = params;
		}
	}
	
	if (url.indexOf("?") < 0
			&& paramsUrl !== "") {
		paramsUrl = "?" + paramsUrl;
	}
	
	return url + paramsUrl;
};

app.utils.setValueObserver = function setValueObserver(
		jCtrl,
		callback,
		initEvent,
		checkEvent,
		valReference) {
	initEvent = app.utils.checkDefault(initEvent, "focus");
	checkEvent = app.utils.checkDefault(checkEvent, "blur");
	valReference = app.utils.checkDefault(valReference, "val");
	
	if (typeof(jCtrl[valReference]) !== "function") {
		throw "valReference must be a function";
	}
	
	var originalValue = null;
	
	jCtrl.bind(initEvent, function() {
		originalValue = jCtrl.val();
	});
	jCtrl.bind(checkEvent, function() {
		if (originalValue !== jCtrl[valReference]()) {
			callback();
		}
	});
};


app.components.ActionMenuComponent = function ActionMenuComponent(options) {
	var jActionMenu = $("#actionMenu");
	var jActionMenuButton = $("#openActionMenu");
	var jCloseActionMenuButton = $("#closeActionMenu");
	
	jActionMenuButton.click(function() {
		jActionMenu.toggle();
	});
	
	jCloseActionMenuButton.click(function() {
		jActionMenu.hide();
	});
	
	function configureButton(
			jButton,
			buttonAction) {
		if (typeof(buttonAction) !== "function") {
			jButton.hide();
			return false;
		} else {
			jButton.click(buttonAction);
			return true;
		}
	};
	
	var deleteConfigured = configureButton(
		$("#delete"),
		options.deleteAction
	);
	var tagConfigured = configureButton(
		$("#tag"),
		options.tagAction
	);
	var oneConfigured = deleteConfigured || tagConfigured;
	
	setActionMenu = function(enable) {
		if (enable === true
				&& oneConfigured) {
			jActionMenuButton.show();
		} else {
			jActionMenuButton.hide();
			jActionMenu.hide();
		}
	};
	
	return {
		setActionMenu: setActionMenu
	};
};

app.components.AppMenuComponent = function AppMenuComponent(options) {
	var deckId = 0;
	setDeckId = function(id) {
		deckId = id;
	}
	
	var cardId = 0;
	setCardId = function(id) {
		cardId = id;
	}
	
	var jAppMenu = $("#appMenu");
	var jAppMenuButton = $("#openAppMenu");
	var jCloseAppMenuButton = $("#closeAppMenu");
	
	jAppMenuButton.click(function() {
		jAppMenu.toggle();
	});
	
	jCloseAppMenuButton.click(function() {
		jAppMenu.hide();
	});
	
	function configureButton(
			jButton,
			buttonOn,
			buttonUrl,
			urlType) {
		if (buttonOn !== true
				|| typeof(buttonUrl) !== "string") {
			jButton.hide();
		} else {
			jButton.click(function() {
				var urlParams = null;
				if (urlType === "deck") {
					urlParams = [
						"deckId=" + deckId
					];
				} else if (urlType === "card") {
					urlParams = [
						"cardId=" + cardId
					];
				}
				document.location = app.utils.getUrl(
					buttonUrl,
					urlParams
				);
			});
		}
	}
	
	configureButton(
		$("#newDeck"),
		options.newDeckButton,
		options.newDeckUrl
	);
	configureButton(
		$("#editDeck"),
		options.editDeckButton,
		options.editDeckUrl,
		"deck"
	);
	configureButton(
		$("#decks"),
		options.decksButton,
		options.decksUrl
	);
	configureButton(
		$("#newCard"),
		options.newCardButton,
		options.newCardUrl,
		"deck"
	);
	configureButton(
		$("#editCard"),
		options.editCardButton,
		options.editCardUrl,
		"card"
	);
	configureButton(
		$("#cards"),
		options.cardsButton,
		options.cardsUrl,
		"deck"
	);
	configureButton(
		$("#review"),
		options.reviewButton,
		options.reviewUrl,
		"deck"
	);
	
	return {
		setDeckId: setDeckId,
		setCardId: setCardId
	};
};

app.components.FilterComponent = function FilterComponent(
		filterUrl,
		filterUrlParameters,
		openItemUrl,
		openItemUrlParameterName,
		fillTemplateCallback) {
	var self = {
		deleteItems: deleteItems,
		getSelectedItemsId: getSelectedItemsId
	};
	
	var MAX_RESULTS = 10;
	
	var totalFiltered = 0;
	var lastFilterText = "";
	
	var jTemplate = $("#itemTemplate");
	var templateHtml = jTemplate.html();
	jTemplate.remove();
	
	var jFilterText = $("#filterText");
	var jEraseFilterButton = $("#eraseFilter");
	var jExecuteFilterButton = $("#executeFilter");
	
	var jLoadButton = $("#loadItems");
	jLoadButton.hide();
	
	function appendItems(data) {
		if (totalFiltered === 0) {
			$(".itemContainerWrapper").remove();
		}
		totalFiltered = totalFiltered + data.length;
		
		for (var i = 0, l = data.length; i < l; i++) {
			var item = data[i];
			var html = fillTemplateCallback(
				templateHtml,
				item
			);
			var jItem = $(html);
			jLoadButton.before(jItem);
			initItemComponent(jItem);
		}
		
		if (data.length >= MAX_RESULTS) {
			jLoadButton.show();
		} else {
			jLoadButton.hide();
		}
	}
	
	function deleteItems(ids) {
		$(".itemId").each(function() {
			var jItemId = $(this);
			var id = jItemId.val();
			if ($.inArray(id, ids) >= 0) {
				jItemId.closest(".itemContainerWrapper").remove();
			}
		});
	}
	
	function getSelectedItems() {
		return $(".itemContainerWrapper .itemSelector.selected");
	}
	
	function getSelectedItemsId() {
		var ids = [];
		getSelectedItems().closest(".itemContainerWrapper").each(function(){
			ids.push($(this).find(".itemId").val());
		});
		return ids;
	}
	
	function initItemComponent(jItem) {
		jItem.click(function() {
			var jItem = $(this);
			
			var urlIdName = openItemUrlParameterName;
			if (typeof(urlIdName) !== "string") {
				urlIdName = "id";
			}
			var urlParams = [
				urlIdName + "=" + jItem.find(".itemId").val()
			];
			document.location = app.utils.getUrl(
				openItemUrl,
				urlParams
			);
		});
		
		jItem.find(".itemSelector").click(function(e) {
			e.stopPropagation();
			
			var jSelector = $(this);
			if (jSelector.hasClass("selected")) {
				jSelector.removeClass("selected");
				jItem.find(".fa-check").hide();
			} else {
				jSelector.addClass("selected");
				jItem.find(".fa-check").show();
			}
			
			triggerSelector();
		});
		
		jItem.find(".fa-check").hide();
	}
	
	function isOneItemSelected() {
		return getSelectedItems().length > 0;
	}
	
	function query(
			filterText,
			firstResult) {
		var data = {
			filterText: filterText,
			firstResult: firstResult,
			maxResults: MAX_RESULTS
		};
		data = $.extend(
			data,
			filterUrlParameters
		);
		$.ajax({
			data: data,
			dataType: "json",
			error: function(data) {
				//
			},
			success: function(data) {
				appendItems(data);
			},
			url: app.utils.getUrl(filterUrl)
		});
	}
	
	function triggerSelector() {
		var data = {
			selected: isOneItemSelected()
		};
		$(self).trigger(
			"selector",
			data
		);
	}
	
	jEraseFilterButton.click(function() {
		jFilterText.val("");
	});
	
	jExecuteFilterButton.click(function() {
		var filterText = jFilterText.val();
		
		totalFiltered = 0;
		lastFilterText = filterText;
		
		query(filterText, 0);
	});
	
	jLoadButton.click(function() {
		query(lastFilterText, totalFiltered);
	});
	
	return self;
};