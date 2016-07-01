'use strict';

/* Controllers */

var oracleMigration = window.oracleMigration;
oracleMigration.controller('IndexController', ["$scope", '$http', 'adAlerts', '$window','$confirm', 'ngDialog', '$location',
		 function($scope, $http, adAlerts, $window, $confirm, ngDialog, $location) {
	$location.path('/home');
}]);