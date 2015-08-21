angular.module("chatApp.controllers").controller("ChatCtrl", function($scope, ChatService) {
	$scope.messages = [];
	$scope.message = "";
	$scope.max = 140;

	$scope.addMessage = function() {
		ChatService.send($scope.message);
		$scope.message = "";
	};

	ChatService.receive().then(null, null, function(message) {
//		if (message.data) {
//			$scope.messages.push(message.data);
//		} else {
//			$scope.messages.push(message);
//		}
		$scope.messages.push(message);
	});
});