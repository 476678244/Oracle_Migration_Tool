'use strict';

/* Controllers */

var oracleMigration = window.oracleMigration;
oracleMigration.controller('NewController', ["$scope", '$http', 'adAlerts', '$window','$confirm', 'ngDialog', '$location',
		 function($scope, $http, adAlerts, $window, $confirm, ngDialog, $location) {
	$window.location.href = './';
}]);