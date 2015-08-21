angular.module("alarm1.controllers").controller("Alarm1Ctrl", function($scope, $timeout, Alarm1Service) {
	
	$scope.sm2 = [];
	
	Alarm1Service.receive().then( null, null, function( data ) {
		$scope.sm2 = data;
	});	
	
}).directive('hello', [function(){
	return {
		restrict: 'EA', // E = Element, A = Attribute, C = Class, M = Comment
		templateUrl: 'categoryTree.html',
		link: function($scope, iElm, iAttrs, controller) {
			//console.log('iElem : ' + iElm);
			//console.log('iAttrs : ' + iAttrs);
			//$scope.name = iAttrs.name;
			//console.log('directive log exec');

		}
	};
}]);
