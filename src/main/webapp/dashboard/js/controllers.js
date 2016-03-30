var oracleMigration = window.oracleMigration;
oracleMigration.controller('MonitorController', ["$scope", '$http', 'adAlerts', function($scope, $http, adAlerts) {
	$scope.jobs = {
	};

	$scope.searchKeyword = '';
	$scope.lineLengthSelect = {
		"selectValue" : 5
	}
	
	window.lineLengthSelect = $scope.lineLengthSelect;

	$http({
		method: 'GET',
		url: '/springbased-1.0/jobs'
	}).then(function successCallback(response) {
		$scope.jobs = response.data;
	}, function errorCallback(response) {
	});

	$scope.searchKeywordChanged = function() {
		var a = $scope.lineLengthSelect;
	}

	$scope.lineLengthSelectChanged = function() {
		var b = 0;
	}
			
}]);