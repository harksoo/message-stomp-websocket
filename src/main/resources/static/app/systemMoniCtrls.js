angular.module("systemMoni.controllers").controller("SystemMoniCtrl", function($scope, $timeout, SystemMoniService, defaultOptions) {
//	$scope.messages = [];
//	$scope.message = "";
//	$scope.max = 140;

	$scope.upper = [];
	$scope.lower = [];
	$scope.item = [];
	$scope.sm2 = [];

	$scope.alarmList = [];
	$scope.alarmList2 = [[]];
	$scope.data = [];
	//최소 2개 이상은 있어야 하는가?
	$scope.chartData = {labels:[10, 20], series:[[10, 20]]};

	var chart = {};

	$timeout(function(){
		SystemMoniService.send( '/app/getAll', 'getAll' );
	}, 100);

	// 알람발생시  받아옴
	SystemMoniService.receive().then( null, null, function( data ) {
//		$scope.messages.push(data);

		if (data.gubun == "1") {

			if ($scope.alarmList.length == 10) {
				$scope.alarmList.shift();
				$scope.alarmList2[0].shift();
			}
			$scope.alarmList.push(data.data);
			$scope.alarmList2[0].push(data.data);

		} else if (data.gubun == "3") {

			var d = data.data;
			if ($scope.chartData.labels.length == 10) {
				$scope.chartData.labels.shift();
				$scope.chartData.series[0].shift();
			}
			$scope.chartData.labels.push(d.timestamp);		
			$scope.chartData.series[0].push(d.cpuAverage);		

			chart.update($scope.chartData);

		} else {
			$scope.sm2 = data.data;
		}
	});	

	chart = new Chartist.Line('#chart1', {
		labels: $scope.chartData.labels,
		series: $scope.chartData.series
	}, defaultOptions);

	$scope.itemMask = function(){
		SystemMoniService.send('/app/mask', 'mask');
	};

//	$scope.itemUnMask = function(){
//	SystemMoniService.send('/app/getAll', 'getAll');
//	};

	$scope.getMask = function( g, m ) {				

		if ( m == "1" || m == "2" ) {				
			return "M";		
		} else {							
			return "";		
		}
	};

	$scope.getGrade = function( g, m ) {				
		if (m == "1") {
			//회색 M
			return "bgcolor4";
		} else if (g == "0") {
			return "bgcolor0";
		} else if (g == "1") {
			return "bgcolor1";
		} else if (g == "2") {
			return "bgcolor2";
		} else if (g == "3") {
			return "bgcolor3";
		}		
	};

	$scope.getUpperColor = function(uppValue) {	

		var count = 0;
		var maxVal = 0;

		angular.forEach(uppValue, function(lower) {

			angular.forEach(lower.value, function(item) {
				//if (item.mask == "1") continue;
				if (item.mask != "1") {
					maxVal = parseInt(item.grade) > maxVal ?  parseInt(item.grade) : maxVal;
				}	

			});
		});

		if (maxVal == 0) {
			return "bgcolor0";
		} else if (maxVal == 1) {
			return "bgcolor1";
		} else if (maxVal == 2) {
			return "bgcolor2";
		} else if (maxVal == 3) {
			return "bgcolor3";
		}		
	};	

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
}]).value("defaultOptions", {
	// Options for X-Axis
	axisX: {
		// The offset of the labels to the chart area
		offset: 30,
		// Position where labels are placed. Can be set to `start` or `end` where `start` is equivalent to left or top on vertical axis and `end` is equivalent to right or bottom on horizontal axis.
		position: 'end',
		// Allows you to correct label positioning on this axis by positive or negative x and y offset.
		labelOffset: {
			x: 0,
			y: 0
		},
		// If labels should be shown or not
		showLabel: true,
		// If the axis grid should be drawn or not
		showGrid: true,
		// Interpolation function that allows you to intercept the value from the axis label
		labelInterpolationFnc: Chartist.noop,
		// Set the axis type to be used to project values on this axis. If not defined, Chartist.StepAxis will be used for the X-Axis, where the ticks option will be set to the labels in the data and the stretch option will be set to the global fullWidth option. This type can be changed to any axis constructor available (e.g. Chartist.FixedScaleAxis), where all axis options should be present here.
		type: undefined
	},
	// Options for Y-Axis
	axisY: {
		// The offset of the labels to the chart area
		offset: 40,
		// Position where labels are placed. Can be set to `start` or `end` where `start` is equivalent to left or top on vertical axis and `end` is equivalent to right or bottom on horizontal axis.
		position: 'start',
		// Allows you to correct label positioning on this axis by positive or negative x and y offset.
		labelOffset: {
			x: 0,
			y: 0
		},
		// If labels should be shown or not
		showLabel: true,
		// If the axis grid should be drawn or not
		showGrid: true,
		// Interpolation function that allows you to intercept the value from the axis label
		labelInterpolationFnc: Chartist.noop,
		// Set the axis type to be used to project values on this axis. If not defined, Chartist.AutoScaleAxis will be used for the Y-Axis, where the high and low options will be set to the global high and low options. This type can be changed to any axis constructor available (e.g. Chartist.FixedScaleAxis), where all axis options should be present here.
		type: undefined,
		// This value specifies the minimum height in pixel of the scale steps
		scaleMinSpace: 50,
		// Use only integer values (whole numbers) for the scale steps
		onlyInteger: true
	},
	// Specify a fixed width for the chart as a string (i.e. '100px' or '50%')
	width: 0,
	// Specify a fixed height for the chart as a string (i.e. '100px' or '50%')
	height: '200px',
	// If the line should be drawn or not
	showLine: true,
	// If dots should be drawn or not
	showPoint: true,
	// If the line chart should draw an area
	showArea: false,
	// The base for the area chart that will be used to close the area shape (is normally 0)
	areaBase: 0,
	// Specify if the lines should be smoothed. This value can be true or false where true will result in smoothing using the default smoothing interpolation function Chartist.Interpolation.cardinal and false results in Chartist.Interpolation.none. You can also choose other smoothing / interpolation functions available in the Chartist.Interpolation module, or write your own interpolation function. Check the examples for a brief description.
	lineSmooth: true,
	// Overriding the natural low of the chart allows you to zoom in or limit the charts lowest displayed value
	low: 0,
	// Overriding the natural high of the chart allows you to zoom in or limit the charts highest displayed value
	high: 100,
	// Padding of the chart drawing area to the container element and labels as a number or padding object {top: 5, right: 5, bottom: 5, left: 5}
	chartPadding: {
		top: 15,
		right: 15,
		bottom: 5,
		left: 10
	},
	// When set to true, the last grid line on the x-axis is not drawn and the chart elements will expand to the full available width of the chart. For the last label to be drawn correctly you might need to add chart padding or offset the last label with a draw event handler.
	fullWidth: true,
	// If true the whole data is reversed including labels, the series order as well as the whole series data arrays.
	reverseData: false,
	// Override the class names that get used to generate the SVG structure of the chart
	classNames: {
		chart: 'ct-chart-line',
		label: 'ct-label',
		labelGroup: 'ct-labels',
		series: 'ct-series',
		line: 'ct-line',
		point: 'ct-point',
		area: 'ct-area',
		grid: 'ct-grid',
		gridGroup: 'ct-grids',
		vertical: 'ct-vertical',
		horizontal: 'ct-horizontal',
		start: 'ct-start',
		end: 'ct-end'
	}
});
