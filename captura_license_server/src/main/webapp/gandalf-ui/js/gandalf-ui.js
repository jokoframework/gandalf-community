/** Servers **/
var Servers = Backbone.Collection.extend({
	url : '/formServer/list'
});

var Server = Backbone.Model.extend({
	urlRoot : '/formServer',
	url : function() {
		if (this.id) {
			return this.urlRoot + '?id=' + this.id;
		} else {
			return this.urlRoot;
		}
	}
});

var ServerListView = Backbone.View.extend({
	el : '.page',
	render : function() {
		var that = this;
		var servers = new Servers();
		servers.fetch({
			success : function(servers) {
				var template = _.template($('#server-list-template').html(), {
					servers : servers.models
				});
				that.$el.html(template);
			}
		});
	}
});

var ServerEditView = Backbone.View.extend({
	el : '.page',
	events : {
		'submit .edit-server-form' : 'saveServer',
		'click .deleteServer' : 'deleteServer'
	},

	saveServer : function(ev) {
		var serverDetails = $(ev.currentTarget).serializeObject();
		var server = new Server();
		server.save(serverDetails, {
			success : function(server) {
				router.navigate('', {
					trigger : true
				});
			}
		});
		return false;
	},

	deleteServer : function(ev) {
		this.server.destroy({
			success : function() {
				console.log('destroyed');
				router.navigate('', {
					trigger : true
				});
			}
		});
		return false;
	},

	render : function(options) {
		var that = this;
		if (options.id) {
			that.server = new Server({
				id : options.id
			});
			that.server.fetch({
				success : function(server) {
					var template = _.template(
							$('#edit-server-template').html(), {
								server : server
							});
					that.$el.html(template);
				}
			});
		} else {
			var template = _.template($('#edit-server-template').html(), {
				server : null
			});
			that.$el.html(template);
		}
	}
});

// Servers' Views
var serverListView = new ServerListView();
var serverEditView = new ServerEditView();

/** Server's License **/
var ServerLicenses = Backbone.Collection.extend({
	urlRoot : '/formServer/license/list',
	url : function() {
		return this.urlRoot+ '?serverId='+  this.server.id;
	}
});

var ServerLicense = Backbone.Model.extend({
	urlRoot : '/formServer/license',
	url : function() {
		var url = this.urlRoot;
		if (this.id) {
			url = url + '?id=' + this.id;
		}
		return url;
	}
});

var ServerLicenseNewView = Backbone.View.extend({
	el : '.page',
	events : {
		'submit .new-server-license-form' : 'newServerLicense',
	},

	newServerLicense : function(ev) {
		var serverLicenseDetails = $(ev.currentTarget).serializeObject();
		serverLicenseDetails.defaultApplicationLicense = {maxUsers: serverLicenseDetails.maxUsers, 
				maxDevices : serverLicenseDetails.maxDevices,
				validDays : serverLicenseDetails.appValidDays};

		delete serverLicenseDetails.maxUsers;
		delete serverLicenseDetails.appValidDays;
		delete serverLicenseDetails.maxDevices;
		
		var serverLicense = new ServerLicense();
		serverLicense.save(serverLicenseDetails, {
			success : function(serverLicense) { 
				router.navigate('serverLicenses/' + serverLicenseDetails.serverId , {
					trigger : true
				});
			}
		});
		return false;
	},

	render : function(options) {
		var that = this;
		var template = _.template($('#new-server-license-template').html(), {
			server : options.server
		});
		that.$el.html(template);
	}
});

var ServerLicenseListView = Backbone.View.extend({
	el : '.page',
	events : {
		'click .deleteServerLicense' : 'deleteServerLicense'
	},
	
	deleteServerLicense : function(ev) {
		var serverLicenseId = $(ev.currentTarget).data('serverLicenseId');
		
		var serverLicense = new ServerLicense({
			id : serverLicenseId
		});
		
		var serverId = $(ev.currentTarget).data('serverId');
		serverLicense.destroy({
			success : function() {
				console.log('destroyed');
				router.navigate('none');
				router.navigate('serverLicenses/' + serverId, {
					trigger : true
				});
			}
		});
		return false;
	},
	
	render : function(options) {
		var that = this;
		var serverLicenses = new ServerLicenses();
		//FIXME
		serverLicenses.server = options.server;
		serverLicenses.fetch({
			success : function(serverLicenses) {
				var template = _.template($('#server-licenses-list-template').html(), {
					serverLicenses : serverLicenses.models,
					server : options.server
				});
				that.$el.html(template);
			}
		});
	}
});

var serverLicenseListView = new ServerLicenseListView();
var serverLicenseNewView  = new ServerLicenseNewView();

/** Application Licenses **/
var ApplicationLicenses = Backbone.Collection.extend({
	urlRoot : '/application/license/list',
	url : function() {
		return this.urlRoot+ '?formServerLicenseId='+  this.serverLicense.id;
	}
});

var ApplicationLicense = Backbone.Model.extend({
	urlRoot : '/application/license',
	url : function() {
		if (this.id) {
			return this.urlRoot + '?id=' + this.id;
		} else {
			return this.urlRoot;
		}
	}
});

var ApplicationLicenseListView = Backbone.View.extend({
	el : '.page',
	events : {
		'click .deleteApplicationLicense' : 'deleteApplicationLicense'
	},
	
	deleteApplicationLicense : function(ev) {
		var applicationLicenseId = $(ev.currentTarget).data('applicationLicenseId');
		
		var applicationLicense = new ApplicationLicense({
			id : applicationLicenseId
		});
		
		var serverLicenseId = $(ev.currentTarget).data('serverLicenseId');
		applicationLicense.destroy({
			success : function() {
				console.log('destroyed');
				router.navigate('none');
				router.navigate('applicationLicenses/' + serverLicenseId, {
					trigger : true
				});
			}
		});
		return false;
	},
	
	render : function(options) {
		var that = this;
		var applicationLicenses = new ApplicationLicenses();
		applicationLicenses.serverLicense = options.serverLicense;
		applicationLicenses.fetch({
			success : function(applicationLicenses) {
				var template = _.template($('#application-licenses-list-template').html(), {
					applicationLicenses : applicationLicenses.models,
					serverLicense : options.serverLicense
				});
				that.$el.html(template);
			}
		});
	}
});

var ApplicationLicenseNewView = Backbone.View.extend({
	el : '.page',
	events : {
		'submit .new-application-license-form' : 'newApplicationLicense',
	},

	newApplicationLicense : function(ev) {
		var applicationLicenseDetails = $(ev.currentTarget).serializeObject();
				
		var applicationLicense = new ApplicationLicense();
		applicationLicense.save(applicationLicenseDetails, {
			success : function(serverLicense) { 
				router.navigate('applicationLicenses/' + applicationLicenseDetails.formServerLicenseId , {
					trigger : true
				});
			}
		});
		return false;
	},

	render : function(options) {
		var that = this;
		var template = _.template($('#new-application-license-template').html(), {
			serverLicense : options.serverLicense
		});
		that.$el.html(template);
	}
});

var applicationLicenseListView = new ApplicationLicenseListView();
var applicationLicenseNewView = new ApplicationLicenseNewView();

/** Notifications */
var Notifications = Backbone.Collection.extend({
	urlRoot : '/notification/list',
	url : function() {
		if(this.serverId){
			return this.urlRoot + '?serverId='+  this.serverId;
		} else {
			return this.urlRoot;
		}
	}
});

var NotificationsListView = Backbone.View.extend({
	el : '.page',
	
	render : function(options) {
		var that = this;
		var notifications = new Notifications();
		notifications.serverId = options.serverId;
		notifications.fetch({
			success : function(notifications) {
				var template = _.template($('#notifications-list-template').html(), {
					notifications : notifications.models,
				});
				that.$el.html(template);
			}
		});
	}
});

var notificationsListView = new NotificationsListView();

/** Routes **/
var Router = Backbone.Router.extend({
	routes : {
		"" : "home",
		"editServer/:id" : "editServer",
		"newServer" : "newServer",
		"serverLicenses/:serverId" : "serverLicenses",
		"newServerLicense/:serverId" : "newServerLicense",
		"applicationLicenses/:serverLicenseId" : "applicationLicenses",
		"newApplicationLicense/:serverLicenseId" : "newApplicationLicense",
		"notifications/:serverId" : "notifications"
	}
});

var router = new Router;

router.on('route:home', function() {
	// render server list
	serverListView.render();
});

router.on('route:editServer', function(id) {
	serverEditView.render({
		id : id
	});
});

router.on('route:newServer', function() {
	serverEditView.render({});
});

router.on('route:serverLicenses', function(serverId) {
	var server = new Server({
		id : serverId
	});
	
	server.fetch({
		success : function(server) {
			serverLicenseListView.render({server: server.attributes});
		}
	});
});

router.on('route:newServerLicense', function(serverId){
	var server = new Server({
		id : serverId
	});
	
	server.fetch({
		success : function(server) {
			serverLicenseNewView.render({server: server.attributes});
		}
	});
});

router.on('route:applicationLicenses', function(serverLicenseId) {
	var serverLicense = new ServerLicense({
		id : serverLicenseId
	});
	
	serverLicense.fetch({
		success : function(serverLicense) {
			applicationLicenseListView.render({serverLicense: serverLicense.attributes});
		}
	});
});

router.on('route:newApplicationLicense', function(serverLicenseId){
	var serverLicense = new ServerLicense({
		id : serverLicenseId
	});
	
	serverLicense.fetch({
		success : function(serverLicense) {
			applicationLicenseNewView.render({serverLicense: serverLicense.attributes});
		}
	});
});

router.on('route:notifications', function(serverId) {
	notificationsListView.render({serverId: serverId});
});

Backbone.history.start();