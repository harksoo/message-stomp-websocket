/**
 * 
 */

(function(){
	
	console.log("app module");
	
	angular.module("oamApp", 
			["jqGrid", "chart", "chat", "occm", "websokServiceModule"]);
	
	angular.module("jqGrid", []);
	angular.module("chart", []);
	angular.module("chat", []);
	angular.module("occm", []);
	angular.module("websokServiceModule", []);
	
	
})(angular);