var oracleMigration = window.oracleMigration;
oracleMigration.controller('MonitorController', ["$scope", '$http', 'adAlerts', function($scope, $http, adAlerts) {
	$scope.jobs = {
	};

	$scope.searchKeyword = '8';

	$http({
		method: 'GET',
		url: '/springbased-1.0/jobs'
	}).then(function successCallback(response) {
		$scope.jobs = response.data;
	}, function errorCallback(response) {
	});

	$scope.searchKeywordChanged = function() {
		var a = 0;
	}
			
}]);