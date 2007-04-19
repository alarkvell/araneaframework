/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */

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

function AraneaEventStore() {
  var araneaEventStore = function() {
    var processEvent = function(event) {
      if (typeof event != "function") {
        event;
      } else {
        event();
      }
    }

    this.execute = function() {
      this.forEach(processEvent);
      this.clear();
    }
  }
  
  araneaEventStore.prototype = new AraneaStore();
  return new araneaEventStore();
}

/* AraneaPage object is present on each page served by Aranea and contains common
 * functionality for setting page related variables, events and functions. */
function AraneaPage() {
  /* URL of aranea dispatcher servlet serving current page. 
   * This is by default set by Aranea JSP ui:body tag. */ 
  var servletURL = null;
  this.getServletURL = function() { return servletURL; }
  this.setServletURL = function(url) { servletURL = new String(url); }

  /* If servlet URL is not enough for some purposes, encoding function should be overwritten. */
  this.encodeURL = function(url) { return url; }

  /* Indicates whether the page is completely loaded or not. Page is considered to 
   * be loaded when all system onload events have completed execution. */
  var loaded = false;
  this.isLoaded = function() { return loaded; }
  this.setLoaded = function(b) { if (typeof b == "boolean") { loaded = b; } }
  
  /* Logger that outputs javascript logging messages. */
  var dummyLogger = new function() { var dummy = function() {}; this.trace = dummy; this.debug = dummy; this.info = dummy; this.warn = dummy; this.error = dummy; this.fatal = dummy;};
  var firebugLogger = window.console ? new function() { this.trace = window.console.debug; this.debug = window.console.debug; this.info = window.console.info; this.warn = window.console.warn; this.error = window.console.error; this.fatal = window.console.error;} : dummyLogger;
  var logger = dummyLogger;
  this.setDummyLogger = function() { logger = dummyLogger; }
  this.setDefaultLogger = function() { 
    if (window['log4javascript/log4javascript.js'])
      logger = log4javascript.getDefaultLogger();
  }
  this.setFirebugLogger = function() { this.setLogger(firebugLogger); }
  this.setLogger = function(theLogger) { logger = theLogger; }
  this.getLogger = function() { return logger; }
  
  /* locale - should be used only for server-side reported locale */
  var locale = new AraneaLocale("", "");
  this.getLocale = function() { return locale; }
  this.setLocale = function(loc) { locale = loc; }

  /* Indicates whether some form on page is (being) submitted already
   * by traditional HTTP request. */
  var submitted = false;
  this.isSubmitted = function() { return submitted; }
  this.setSubmitted = function() { submitted = true; }
  
  var systemForm = null;
  this.getSystemForm = function() { return systemForm; }
  this.setSystemForm = function(_systemForm) { systemForm = _systemForm; }

  this.setSystemFormEncoding = function(encoding) {
    this.addSystemLoadEvent(function() {
      systemForm.enctype = encoding;
      systemForm.encoding = encoding; // IE
    });
  }

  this.getEventTarget = function(element) {
    return element.getAttribute('arn-trgtwdgt');
  }
  this.getEventId = function(element) {
    return element.getAttribute('arn-evntId');
  }
  this.getEventParam = function(element) {
    return element.getAttribute('arn-evntPar');
  }
  this.getEventUpdateRegions = function(element) {
    return element.getAttribute('arn-updrgns');
  }
  this.getEventPreCondition = function(element) {
    return element.getAttribute('arn-evntCond');
  }

  /** Timer that executes keepalive calls, if any. */
  var keepAliveTimers = new AraneaStore();

  /** Variables holding different (un)load events that should be executed when page loads -- on body (un)load or alike. */
  var systemLoadEvents = new AraneaEventStore();
  var clientLoadEvents = new AraneaEventStore();
  var systemUnLoadEvents = new AraneaEventStore();
  
  submitCallbacks = new Object();

  this.addSystemLoadEvent = function(event) { systemLoadEvents.add(event); }
  this.addClientLoadEvent = function(event) { clientLoadEvents.add(event); }
  this.addSystemUnLoadEvent = function(event) { systemUnLoadEvents.add(event); }

  this.onload = function() { systemLoadEvents.execute(); this.setLoaded(true); clientLoadEvents.execute(); }
  this.onunload = function() { systemUnLoadEvents.execute(); }
  
  // General callbacks executed before each form submit.
  this.addSubmitCallback = function(callback) {
    this.addSystemFormSubmitCallback('callbacks', callback);
  }

  // General callbacks executed before each submit of the specified system form.
  this.addSystemFormSubmitCallback = function(systemFormId, callback) {
    if (!submitCallbacks[systemFormId])
    submitCallbacks[systemFormId] = new AraneaEventStore();
    submitCallbacks[systemFormId].add(callback);
  }

  this.executeCallbacks = function(systemFormId) {
    if (submitCallbacks['callbacks'])
    submitCallbacks['callbacks'].execute();
    
  if (submitCallbacks[systemFormId])
    submitCallbacks[systemFormId].execute();
  }
  
  this.event = function(element) {
    if (this.isSubmitted() || !this.isLoaded())
    return false;
    
  var systemForm = this.getSystemForm();
    var preCondition = this.getEventPreCondition(element);

    if (preCondition) {
      var f = new Function("element", preCondition);
      if (!f(element)) {
        return false;
      }
    }

    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    return (this.findSubmitter(element, systemForm)).event(element);
  }

  /** 
   * This function can be overwritten to support additional
   * submit methods.
   */
  this.findSubmitter = function(element, systemForm) {
    var updateRegions = this.getEventUpdateRegions(element);

  if (updateRegions && updateRegions.length > 0)
    return new DefaultAraneaAJAXSubmitter(systemForm);
  else
    return new DefaultAraneaSubmitter(systemForm);
  }
  
  // another submit function, takes all params that are currently possible to use.
  // TODO: get rid of duplicated logic from: submit() and findSubmitter()
  this.event_6 = function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions) {
    if (this.isSubmitted() || !this.isLoaded())
    return false;

    if (eventPrecondition) {
      var f = new Function(eventPrecondition);
      if (!f()) {
        return false;
      }
    }
    
    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    if (eventUpdateRegions != null && eventUpdateRegions.length > 0) 
      return new DefaultAraneaAJAXSubmitter().event_5(systemForm, eventId, eventTarget, eventParam, eventUpdateRegions);
    else
      return new DefaultAraneaSubmitter().event_4(systemForm, eventId, eventTarget, eventParam);
  }

  this.getSubmitURL = function(topServiceId, threadServiceId, transactionId) {
    var url = this.encodeURL(this.getServletURL());
    url += '?transactionId=' + transactionId;
    if (topServiceId) 
      url += '&topServiceId=' + topServiceId;
    if (threadServiceId) 
      url += '&threadServiceId=' + threadServiceId;
    return url;
  }

  this.getActionSubmitURL = function(systemForm, actionId, actionTarget, actionParam, sync) {
    var url = this.getSubmitURL(systemForm.topServiceId.value, systemForm.threadServiceId.value, 'override');
    url += '&widgetActionPath=' + actionTarget;
    if (actionId)
      url += '&serviceActionHandler=' + actionId;
    if (actionParam)
      url += '&serviceActionParameter=' + actionParam;
    if (sync != undefined && !sync)
      url += '&sync=false';
    url += '&systemFormId=' + systemForm.id;
    return url;
  }

  this.action = function(element, actionId, actionTarget, actionParam, sync, actionCallback, options) {
    var systemForm = this.getSystemForm();
    return this.action_6(systemForm, actionId, actionTarget, actionParam, sync, actionCallback, options);
  }

  this.action_6 = function(systemForm, actionId, actionTarget, actionParam, sync, actionCallback, options) {
    if (window['prototype/prototype.js']) {
      options = Object.extend({method: 'get', onComplete: actionCallback}, options);
      var url = this.getActionSubmitURL(systemForm, actionId, actionTarget, actionParam, sync);
      return new Ajax.Request(url, options);
    } else {
      araneaPage().getLogger().warn("Prototype library not accessible, action call cannot be made.");
    }
  }

  this.debug = function(message) {
    this.getLogger().debug(message);
  }
  
  /** 
   * Provides preferred way of overriding AraneaPage object functions. 
   * @param functionName name of AraneaPage function that should be overridden. 
   * @param f replacement function 
   */
  this.override = function(functionName, f) {
    this.getLogger().info("AraneaPage." +functionName + " was overriden.");
    this[functionName] = f;
  }
  
  /** 
   * Adds keepalive function f that is executed periodically after time 
   * milliseconds has passed 
   */
  this.addKeepAlive = function(f, time) {
    keepAliveTimers.add(setInterval(f, time));
  }

  /** Clears/removes all registered keepalive functions. */
  this.clearKeepAlives = function() {
    keepAliveTimers.forEach(function(timer) {clearInterval(timer);});
  }
}

/* Returns a default keepalive function -- to make periodical requests to expiring thread
 * or top level services. */
AraneaPage.getDefaultKeepAlive = function(topServiceId, threadServiceId, keepAliveKey) {
  return function() {
    if (window['prototype/prototype.js']) {
      var url = araneaPage().getSubmitURL(topServiceId, threadServiceId, 'override');
      url += "&" + keepAliveKey + "=true";
      araneaPage().getLogger().debug("Sending async service keepalive request to URL '" + url +"'");
      var keepAlive = new Ajax.Request(
          url,
          { method: 'get' }
      );
    } else {
      araneaPage().getLogger().warn("Prototype library not accessible, service keepalive calls cannot be made.");
    }
  };
}

// Random request id generator. Sent only with AA ajax requests.
// Currently only purpose of it is easier debugging (identifying requests).
AraneaPage.getRandomRequestId = function() {
  return Math.round(100000*Math.random());
}

// Page initialization function, should be called upon page load.
AraneaPage.init = function() {
  araneaPage().addSystemLoadEvent(Behaviour.apply);
}
AraneaPage.findSystemForm = function() {
  araneaPage().setSystemForm($A(document.getElementsByTagName('form')).find(
    function(element) {
      return $(element).hasAttribute('arn-systemForm');
    }
  ));
}

function DefaultAraneaSubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
    // event information
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);
    
    return this.event_4(systemForm, eventId, widgetId, eventParam);
  }
}

DefaultAraneaSubmitter.prototype.event_4 = function(systemForm, eventId, widgetId, eventParam) {
  systemForm.widgetEventPath.value = widgetId ? widgetId : "";
  systemForm.widgetEventHandler.value = eventId ? eventId : "";
  systemForm.widgetEventParameter.value = eventParam ? eventParam : "";

  araneaPage().setSubmitted();

  systemForm.submit();

  return false;
}

function DefaultAraneaAJAXSubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
  // event information
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);
  var updateRegions = araneaPage().getEventUpdateRegions(element);

  return this.event_5(systemForm, eventId, widgetId, eventParam, updateRegions);
  }
}

DefaultAraneaAJAXSubmitter.prototype.event_5 = function(systemForm, eventId, widgetId, eventParam, updateRegions) {
  systemForm.widgetEventPath.value = widgetId ? widgetId : "";
  systemForm.widgetEventHandler.value = eventId ? eventId : "";
  systemForm.widgetEventParameter.value = eventParam ? eventParam : "";
  if (systemForm.transactionId) {
    systemForm.transactionId.value = "override";
  }

  $(systemForm.id).request({
    parameters: {
      updateRegions: eval("new Array(" + updateRegions + ");"),
      systemFormId: systemForm.id
    },
    onSuccess: function(transport) {
      araneaPage().getLogger().debug('Partial rendering: received successful response');
      Aranea.process(transport.responseText);
      AraneaPage.init();
      araneaPage().onload();
    },
    onFailure: function(transport) {
      araneaPage().getLogger().debug('Partial rendering: received erroneous response');
      Element.update(document.documentElement, transport.responseText);
    }
  });

  return false;
}

Text = Class.create();
Text.prototype = {
  initialize: function(text) {
    this.text = text;
  },

  readLine: function() {
    var i = this.text.indexOf("\n");
    var line;
    if (i == -1) {
      line = this.text;
      this.text = '';
    } else {
      line = this.text.substr(0, i);
      this.text = this.text.substr(i + 1);
    }
    return line;
  },

  readBytes: function(bytes) {
    var content = this.text.substr(0, bytes);
    this.text = this.text.substr(bytes);
    return content;
  },

  empty: function() {
    return this.text.length == 0;
  }
};

var Aranea = {
  regionHandlers: new Hash(),

  addRegionHandler: function(key, handler) {
    this.regionHandlers[key] = handler;
  },

  process: function(responseText) {
    var text = new Text(responseText);
    while (!text.empty()) {
      var key = text.readLine();
      if (this.regionHandlers[key]) {
        araneaPage().getLogger().debug('Region type: "' + key + '"');
        this.regionHandlers[key].process(text);
      } else {
        araneaPage().getLogger().error('Region type: "' + key + '" is unknown!');
        return;
      }
    }
  }
};

Aranea.TransactionIdRegionHandler = Class.create();
Aranea.TransactionIdRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var transactionId = text.readLine();
    araneaPage().getSystemForm().transactionId.value = transactionId;
  }
};
Aranea.addRegionHandler('transactionId', new Aranea.TransactionIdRegionHandler());

Aranea.DomRegionHandler = Class.create();
Aranea.DomRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var id = text.readLine();
    var length = text.readLine();
    var content = text.readBytes(length);
    $(id).replace(content);
  }
};
Aranea.addRegionHandler('dom', new Aranea.DomRegionHandler());

Aranea.DocumentRegionHandler = Class.create();
Aranea.DocumentRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var length = text.readLine();
    var content = text.readBytes(length);
    Element.update(document.documentElement, content);
  }
};
Aranea.addRegionHandler('document', new Aranea.DocumentRegionHandler());

Aranea.MessagesRegionHandler = Class.create();
Aranea.MessagesRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var messagesByType = text.readBytes(text.readLine()).evalJSON();
    this.updateRegions(messagesByType);
  },

  updateRegions: function(messagesByType) {
    $$('.aranea-messages').each((function(region) {
      if (region.hasAttribute('arn-msgs-type')) {
        var type = region.getAttribute('arn-msgs-type');
        if (messagesByType[type]) {
          var messages = messagesByType[type];
          if (messages.size() > 0) {
            this.showMessagesRegion(region, messages);
            return;
          }
        }
      } else {
        var messages = messagesByType.values().flatten();
        if (messages.size() > 0) {
          this.showMessagesRegion(region, messages);
          return;
        }
      }
      this.hideMessagesRegion(region);
    }).bind(this));
  },

  showMessagesRegion: function(region, messages) {
    this.findContentElement(region).update(this.buildRegionContent(messages));
    region.show();
    return;
  },

  hideMessagesRegion: function(region) {
    region.hide();
    this.findContentElement(region).update();
  },

  findContentElement: function(region) {
    return region;
  },

  buildRegionContent: function(messages) {
    return messages.invoke('escapeHTML').join('<br/>');
  }
};
Aranea.addRegionHandler('messages', new Aranea.MessagesRegionHandler());

/* Initialize new Aranea page.  */
/* Aranea page object is accessible in two ways -- _ap and araneaPage() */
_ap = new AraneaPage();
function araneaPage() { return _ap; }
_ap.addSystemLoadEvent(AraneaPage.init);
_ap.addSystemLoadEvent(AraneaPage.findSystemForm);

window['aranea.js'] = true;
