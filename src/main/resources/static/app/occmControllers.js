/**
 * 
 */

(function(){

	angular.module("occm")
	.controller("occmController", function($scope, websokService) {

		console.log("occmController");
		var subscriber = {};
		
		subscriber = websokService.subscribe("/topic/route");
		subscriber.listener.promise.then(null, null, function(message) {
			//console.log(message);
			
			message.localtime = new Date();
			
//			console.log(message.id);
//			if(message.id == 1)
//				$scope.firstMessage = message;
			
			$scope.messages.push(message);
			if($scope.messages.length > 50000)
				$scope.messages.shift();
	
	    });
		
		$scope.messages = [];
		$scope.firstMessage = {};
		//$scope.message = "";
		//$scope.max = 140;
		

	});
	

})(angular);