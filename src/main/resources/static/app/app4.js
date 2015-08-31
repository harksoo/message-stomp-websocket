angular.module('websocketClientApp').controller('WsCtrl', function ($scope, $timeout) {
//	STARTOFCONTROLLER
	$scope.message = '';
	$scope.ws;
	$scope.echoMessages = [];

	$scope.init = function() {
		$scope.ws = new WebSocket('ws://localhost:8080/echo');
		$scope.ws.onopen = function() {
			console.log('websocket opened');
		};
		$scope.ws.onmessage = function(message) {
//			console.log(message);
//			console.log('receive message : ' + message.data);
//			$scope.echoMessages.unshift(message.data);
//			$timeout(function() {
//				$scope.$apply('echoMessages');
//			})
			
			if ( $scope.echoMessages.length == 10 ) {
				$scope.echoMessages.shift();
			}
			$scope.echoMessages.push(message);
		};
		$scope.ws.onclose = function(event) {
			console.log(event);
			console.log('websocket closed');
		};
	};

	$scope.send = function() {
		$scope.ws.send($scope.message);
	};

	$scope.init();
//	ENDOFCONTROLLER
});