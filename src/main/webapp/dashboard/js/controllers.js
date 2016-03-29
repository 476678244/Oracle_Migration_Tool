var oracleMigration = window.oracleMigration;
oracleMigration.controller('MonitorController', ["$scope", '$http', 'adAlerts', function($scope, $http, adAlerts) {
	$scope.jobs = {
	};

	$scope.showContainerFlag = false;

	$http({
		method: 'GET',
		url: '/springbased-1.0/jobs'
	}).then(function successCallback(response) {
		$scope.showContainerFlag = true;
		$scope.jobs = response.data;
	}, function errorCallback(response) {
	});

	$scope.showContainer = function() {
		return $scope.showContainerFlag;
	};

			
}]);