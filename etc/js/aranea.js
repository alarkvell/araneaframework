function AraneaStore() {
  var objects = new Array();

  this.add = function(object) {
    var len = objects.length;
	objects[len] = object;
  }
  
  this.clear = function() {
  	objects = new Array();
  }
  
  this.length = function() {
    return objects.length;
  }

  this.getContents = function() {
    return objects;
  }
  
  this.forEach = function(f) {
    for(var i = 0; i < objects.length; i++) {
      f(objects[i]);
    }
  }
}

AraneaEventStore.prototype = new AraneaStore();
function AraneaEventStore() {
  var that = this;

  var processEvent = function(event) {
    if (typeof event != "function") {
      event;
    } else {
      event();
    }
  }

  this.execute = function() {
    that.forEach(processEvent);
    that.clear();
  }
}

// AraneaPage object is present on each page served by Aranea and contains common
// functionality for setting page related variables, events and functions.
function AraneaPage() {
  /** Variable that shows if page is active (form has not been submitted yet). */
  var pageActive = true;

  /** Variables holding different (un)load events that should be executed when page loads (body unload or alike) */
  var systemLoadEvents = new AraneaEventStore();
  var clientLoadEvents = new AraneaEventStore();
  var systemUnLoadEvents = new AraneaEventStore();

  loadEvents = new AraneaStore();
  loadEvents.add(systemLoadEvents);
  loadEvents.add(clientLoadEvents);
  
  unloadEvents = new AraneaStore();
  unloadEvents.add(systemUnLoadEvents);

  /** Return whether this page is active (has not been submitted yet). */
  this.isPageActive = function() {
    return pageActive;
  }

  /** Sets page status. */
  this.setPageActive = function(active) {
    if (typeof active == "boolean")
      pageActive = active;
  }

  this.addClientLoadEvent = function(event) {
  	clientLoadEvents.add(event);
	document.getElementsbytagnamen
  }
  
  this.addSystemLoadEvent = function(event) {
  	systemLoadEvents.add(event);
  } 
  
  this.onload = function() {
    loadEvents.forEach(function(eventHolder) {eventHolder.execute();});
  }

  this.onunload = function() {
    unloadEvents.forEach(function(eventHolder) {eventHolder.execute();});
  }
}

AraneaPage.prototype.submit = function(element, submitter) {
  submitter(element);
}

AraneaPage.prototype.standardSubmit = function(element) {
  var w = window.open('', 'debug', 'width=1000,height=800,scrollbars=yes');
  w.document.write(element);
  /*
  w.document.write('<br />');
  for (var i in element) {
  	w.document.write("property '" + i+ "'=" + element[i]);
	w.document.write('<br />');
  }
  */
  w.document.write("----------TRAVERSING---------------------------------");
  w.document.write('<br />');
  
	var i = 0;
  
  var pNode = element.parentNode;
  while (pNode) {
  	w.document.write(pNode.tagName);
	pNode = pNode.parentNode;
  	w.document.write('<br />');
  } 
  
  w.document.write('searching surrounding systemform');
  var t = new AraneaTraverser();
  var systemForm = t.findSurroundingSystemForm(element);
  
  w.document.write('teh systemform ' + systemForm+  '<br />');
  
  for (var i in systemForm) {
  	w.document.write("property '" + i+ "'=" + systemForm[i]);
	w.document.write('<br />');
  }
}
  
function AraneaTraverser() {
  this.findSurroundingSystemForm = function(element) {
    if (element.getAttributeNS("http://www.araneaframework.org/", "systemForm"))
	  return element;
	  
	var pNode = element.parentNode;
    while (pNode && pNode.length > 0) {
      if (pNode.getAttributeNS("http://www.araneaframework.org/", "systemForm"))
	    return pNode;

  	  pNode = pNode.parentNode;
    }
	return null;
  }
}
  
/*		systemForm, 
		widgetId, 
		eventId, 
		eventParam, */ 


// OLD

/*

function uiStandardSubmitEvent(systemForm, widgetId, eventId, eventParam, call, precondition) {
	if (!pageActive) return false;
	
	if (precondition && !precondition()) return false;
	
	standardParams = getStandardParameterObject(systemForm, widgetId, eventId, eventParam);

	call();
	
	return false;
}

function araneaSubmitEvent(systemForm, widgetId, eventId, eventParam, updateRegions) {
	systemForm.widgetEventPath.value = widgetId;
	systemForm.widgetEventHandler.value = eventId;	
	systemForm.widgetEventParameter.value = eventParam;
	uiSystemFormSubmit(systemForm, updateRegions);
}

function araEvent(standardParams, updateRegions) {
	standardParams.systemForm.widgetEventPath.value = standardParams.widgetId;
	standardParams.systemForm.widgetEventHandler.value = standardParams.eventId;
	standardParams.systemForm.widgetEventParameter.value = standardParams.eventParam;
	uiSystemFormSubmit(standardParams.systemForm, updateRegions);
}

function getStandardParameterObject(systemForm, widgetId, eventId, eventParam) {
  result = new Object();
  result.systemForm = systemForm;
  result.widgetId = widgetId;
  result.eventId = eventId;
  result.eventParam = eventParam;
  return result;
}


function uiStandardSubmitFormEvent(systemForm, formId, elementId, eventId, eventParam, validate, call, precondition) {	
	if (!pageActive) return false;
	
	if (validate) {
	  var valid = uiValidateForm(systemForm, formId);
	  if (!valid) return false;
	}

	if(precondition && !precondition()) return false;
	
	standardParams = getStandardParameterObject(systemForm, formId+"."+elementId, eventId, eventParam);

	call();
	
	return false;	
}*/


// new aranea page
_ap = new AraneaPage();