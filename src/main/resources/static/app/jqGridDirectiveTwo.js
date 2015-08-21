
(function(){

	angular.module("jqGridTwo")
	.controller("AlaramGridController", function($scope, websokService) {

		var id = 0;
		var subscriber = {};

		$scope.data = [];
		$scope.maxlength = 10;
		
		$scope.config = {
				datatype: "local",
				//data: mydata,
				height: 250,
				width: 900,
				colModel: [
				           { label: 'ID', name: 'id', width: 75, hidden: true, key:true },
				           { label: 'SYSTEM', name: 'system', width: 70 },
				           { label: 'Upper', name: 'upper', width: 70  },
				           { label: 'Lower', name: 'lower', width: 70  },
				           { label: 'Item', name: 'item', width: 70 },
				           { label: 'Grade', name: 'date', width: 100 },
				           { label: 'Time', name: 'alarm', width: 100 },
				           { label: 'Log', name: 'log', width: 0 }
				           ],
	           viewrecords: true, // show the current page, data rang and total records on the toolbar
	           caption: "Load jqGrid through Javascript Array",
	           loadComplete: function(){},
	           afterInsertRow: function(){},
	           maxlength:10,
		};

//		subscriber = websokService.subscribe("/topic/realtimeAlarm");
		subscriber = websokService.subscribe("/topic/smRealtimeAlarm");

		subscriber.listener.promise.then(null, null, function(responseData) {
//			showAlarm(responseData.data);
			
			console.log('tttime :: ' + responseData.date);
			
			if ($scope.data.length == 10) {
				$scope.data.shift();
			}
			$scope.data.push(responseData);
		});
		
		
		
	})
	.directive('alaramGrid', function () {
		return {
			restrict: 'E',
			scope: {
				config: '=',
				data: '=',
			},
			link: function (scope, element, attrs) {
				var table;

				scope.$watch('config', function (newValue) {
					element.children().empty();
					table = angular.element('<table></table>');	
					element.append(table);
					$(table).jqGrid(newValue);
				});

				scope.$watch('data', function (newValue, oldValue) {
					var i;
//					console.log('oldValue :: ' + JSON.stringify(oldValue));
//					console.log('newValue :: ' + JSON.stringify(newValue));
					
					for (i = oldValue.length - 1; i >= 0; i--) {
						$(table).jqGrid('delRowData', i);
					}
					for (i = 0; i < newValue.length; i++) {
						$(table).jqGrid('addRowData', i, newValue[i]);
					}
				}, true);
			}
		};
	})
	// =========================================================================
	// jqGrid는 virtical oriented column 형식이 지원이 안됌
	// 1. 동적 열 추가를 할 수 없어 기존 테이블을 지우고 다시 그려야함... 하지만 다시 그릴경우 스크롤이 맨 앞으로가..이것에 대한 처리를 해야함...
	// 2. Max 열을 만들고 데이터를 업데이트 시키는 방안으로 처리 하도록 구성

	.controller("realtimeStaticGridController", function($scope, websokService) {

		var id = 0;
		var subscriber = {};

//		$scope.maxlength = 20;
//		$scope.currentPos = 0;
		
		$scope.data = [];
		var h1, h2, h3, h4, h5, h6, h7;
		
		var widthMaxLen = 10;
		var tmpArr128 = [];
//		var verticalMaxLen = 7;
		
		var protoRealTimeAlarm = {w0:"", w1:"", w2:"", w3:"", w4:"", w5:"", w6:"", w7:"", w8:"", w9:""};
		var makeRealTimeAlarm = function(i, type, respData, sourceObj) {
//			var tmpObj = Object.create(protoRealTimeAlarm);
			sourceObj['w'+i] = respData[type];
			return sourceObj;
		}
		
		h1 = Object.create(protoRealTimeAlarm);
		h2 = Object.create(protoRealTimeAlarm);
		h3 = Object.create(protoRealTimeAlarm);
		h4 = Object.create(protoRealTimeAlarm);
		h5 = Object.create(protoRealTimeAlarm);
		h6 = Object.create(protoRealTimeAlarm);
		h7 = Object.create(protoRealTimeAlarm);
		
		subscriber = websokService.subscribe("/topic/resourceMonitor");
//		subscriber = websokService.subscribe("/topic/smRealtimeAlarm");
		
		subscriber.listener.promise.then(null, null, function(responseData) {		
			
//			if ($scope.data.length == 10) {
//				$scope.data.shift();
//			}
//			$scope.data.push(responseData);
			
			var i;
			
//			showAlarm(message);	
			if (tmpArr128.length == 10) {
				tmpArr128.shift();				
			}
			
			tmpArr128.push(responseData);
			
			var tmpArr129 = [];
			for (i=0; i<tmpArr128.length; i++) {
				h1 = makeRealTimeAlarm(i,'cpuMax', tmpArr128[i], h1);
				h2 = makeRealTimeAlarm(i,'cpuAverage', tmpArr128[i], h2);
				h3 = makeRealTimeAlarm(i,'ramMax', tmpArr128[i], h3);
				h4 = makeRealTimeAlarm(i,'ramAverage', tmpArr128[i], h4);
				h5 = makeRealTimeAlarm(i,'diskMax', tmpArr128[i], h5);
				h6 = makeRealTimeAlarm(i,'diskAverage', tmpArr128[i], h6);
				h7 = makeRealTimeAlarm(i,'timestamp', tmpArr128[i], h7);
			}
			tmpArr129.push(h1);
			tmpArr129.push(h2);
			tmpArr129.push(h3);
			tmpArr129.push(h4);
			tmpArr129.push(h5);
			tmpArr129.push(h6);
			tmpArr129.push(h7);			
		
		});

	
		$scope.config = {
			datatype: "local",
				height: 250,
				width: 900,
				colModel: [
				           { label:'', name: '', width: 75, hidden: true, key:true },
				           { label:'w0', name: 'w0', width: 70 },
				           { label:'w1', name: 'w1', width: 70 },
				           { label:'w2', name: 'w2', width: 70 },
				           { label:'w3', name: 'w3', width: 70 },
				           { label:'w4', name: 'w4', width: 70 },
				           { label:'w5', name: 'w5', width: 70 },
				           { label:'w6', name: 'w6', width: 70 },
				           { label:'w7', name: 'w7', width: 70 },
				           { label:'w8', name: 'w8', width: 70 },
				           { label:'w9', name: 'w9', width: 0 }
			   ],
//	           colModel: [
//	                      { label:'', name: '', width: 75, hidden: true, key:true },
//	                      { label:'cpuM', name: 'cpuMax', width: 70 },
//	                      { label:'cpuA', name: 'cpuAverage', width: 70 },
//	                      { label:'ramM', name: 'ramMax', width: 70 },
//	                      { label:'ramA', name: 'ramAverage', width: 70 },
//	                      { label:'diskM', name: 'diskMax', width: 70 },
//	                      { label:'diskA', name: 'diskAverage', width: 100 },
//	                      { label:'time', name: 'timestamp', width: 0 }
//	                      ],
				viewrecords: false, // show the current page, data rang and total records on the toolbar
				shrinkToFit: false,
				caption: "Realtime Static Grid",
				loadComplete: function(){},
				afterInsertRow: function(){}
				//maxlength: 10
				//currentPos: $scope.currentPos,
		};
	})
	// directive 에서 받을 수 없는 인자는 $scope
	.directive( 'realtimeStaticGrid', function () {
		return {
			restrict: 'E',
			scope: {
				config : '=',
				data : '=',
			},
			link: function( scope, element, attrs ){

				scope.$watch('config', function (newValue) {
					element.children().empty();
					table = angular.element('<table></table>');	
					element.append(table);
					$(table).jqGrid(newValue);
				});

				scope.$watchCollection('data', function (newValue, oldValue) {
					
//					console.log("----------------------------------------------------------------");
					// data = [[], [], [], [], [], [], []]
//					console.log('oldValue2 :: ' + oldValue);
//					console.log('newValue2 :: ' + newValue);
					
					var i;
					for (i = oldValue.length - 1; i >= 0; i--) {
						$(table).jqGrid('delRowData', i);
					}
					for (i = 0; i < newValue.length; i++) {
						$(table).jqGrid('addRowData', i, newValue[i]);
					}
				});

			}
		};
	})
	; // angular.module("oamApp.jqGrid") end

})(angular);