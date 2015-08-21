//(function(angular, SockJS, Stomp, _, undefined){
	

angular.module("systemMoni.services").service("SystemMoniService", function($q, $timeout) {

	var service = {}, 
	listener = $q.defer(), 
		socket = { client: null, stomp: null }, 
		messageIds = [];

//	service.RECONNECT_TIMEOUT = 30000;
	service.RECONNECT_TIMEOUT = 30;
	service.SOCKET_URL = "/sysmoni";
	service.ALL_SYSTEM_MONI = "/topic/all";
	service.CHANGE_SYSTEM_MONI = "/topic/systemMonitoring";
	service.MASKED = "/topic/masked";

	service.receive = function() {
		return listener.promise;
	};

	service.send = function(url, message) {
		var id = Math.floor(Math.random() * 1000000);
		socket.stomp.send( url, { priority: 9 }, JSON.stringify( { message: message, id: id } )	);
	};
	
	var close = function () {
		socket.stomp.close();
	}
	
	var reconnect = function() {
		$timeout( function() {
			initialize();
		}, this.RECONNECT_TIMEOUT);
	};

	var getMessage = function(data) {
		var message = JSON.parse(data), out = {};
		out.items = message.items;
		
//		out.time = new Date(message.time);
//		if (_.contains(messageIds, message.id)) {
//			out.self = true;
//			messageIds = _.remove(messageIds, message.id);
//		}
		return out;
	};
	
	var getAll = function(data) {
		var message = JSON.parse(data), out = {};
//		out.uppers = message.uppers;
//		out.lowers = message.lowers;
//		out.items = message.items;
		
//		out.time = new Date(message.time);
//		if (_.contains(messageIds, message.id)) {
//			out.self = true;
//			messageIds = _.remove(messageIds, message.id);
//		}
		out.data = message;
		return out;
	};
	var getAlarm1 = function(data) {
		var message = JSON.parse(data), out = {};
		out.gubun = "1";
		out.data = message;
		return out;
	};
	var getGraph1 = function(data) {
		var message = JSON.parse(data), out = {};
		out.gubun = "3";
		out.data = message;
		return out;
	};

	var startListener = function() {
		socket.stomp.subscribe(service.ALL_SYSTEM_MONI, function(data) {
			listener.notify(getAll(data.body));
		});
		socket.stomp.subscribe(service.CHANGE_SYSTEM_MONI, function(data) {
			listener.notify(getAll(data.body));
		});
		socket.stomp.subscribe(service.MASKED, function(data) {
			listener.notify(getAll(data.body));
		});
		socket.stomp.subscribe("/topic/smRealtimeAlarmTwo", function(data) {
			listener.notify(getAlarm1(data.body));
		});
		socket.stomp.subscribe("/topic/smResourceGraph", function(data) {
			listener.notify(getGraph1(data.body));
		});
		socket.stomp.subscribe("/topic/message", function(data) {
			listener.notify(getAll(data.body));
		});
	};

	var initialize = function() {
//		if (!socket.client) {
			socket.client = new SockJS(service.SOCKET_URL);
			socket.stomp = Stomp.over(socket.client);
			socket.stomp.connect({}, startListener);
			socket.stomp.onclose = reconnect;
//		}
		
	};

	initialize();
	return service;
});

//})(angular, SockJS, Stomp, _);