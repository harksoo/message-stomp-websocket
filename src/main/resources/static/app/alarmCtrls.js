angular.module("alarm.controllers").controller("AlarmCtrl", function($scope, $timeout, AlarmService) {
	$scope.messages = [];
	$scope.message = "";
	
	$scope.count = 0;
	$scope.startTime = 0;
	$scope.endTime = 0;
	$scope.alarmList = [];
	$scope.processTimeList = [];
	
//	$timeout(function(){
//		SystemMoniService.send('/app/getAll', 'getAll');
//	}, 500);
	
	
	// 알람발생시  받아옴
	var d, st, et, sst, eet, 
		cnt = 0,
		procTime = {}
		;
	AlarmService.receive().then( null, null, function( data ) {
		
		if ( cnt == 0 ) {
			d = new Date();
			//시작시간
			st = d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds() + '.' + d.getMilliseconds();
			sst = d.getTime();
			$scope.startTime = st;
			procTime.startTime = st;
		}
		
		if ( $scope.alarmList.length === 10 ) {
			$scope.alarmList.shift();
		}
		$scope.alarmList.push( data );
				
//		console.log( '$scope.processTimeList[0].length >>>>>' +  $scope.processTimeList.length );
		cnt++; 
		$scope.count = cnt;
		if ( cnt == 100 ) {
			
			if ( $scope.processTimeList.length === 10 ) {
				$scope.processTimeList.shift();
			}
			
			d = new Date();
			//시작시간
			et = d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds() + '.' + d.getMilliseconds();
			eet = d.getTime();
			$scope.endTime = et;
//			$scope.handleTime = (eet-sst)/1000;
			
			procTime.endTime = et;
			procTime.handleTime = (eet-sst)/1000;
			procTime.count = cnt;
			$scope.processTimeList.push( procTime );
			
			procTime = {};
			cnt = 0; 
		}
	});	
	
});
