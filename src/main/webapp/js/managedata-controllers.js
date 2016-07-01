'use strict';

/* Controllers */

var oracleMigration = window.oracleMigration;
oracleMigration.controller('ManageDataController', ["$scope", '$http', 'adAlerts', '$window','$confirm', 'ngDialog', '$location',
		 function($scope, $http, adAlerts, $window, $confirm, ngDialog, $location) {
	$scope.gotoMonitorPage = function() {
    	$window.location.href = 'dashboard/html/table/datatable.html';
    };
}]);