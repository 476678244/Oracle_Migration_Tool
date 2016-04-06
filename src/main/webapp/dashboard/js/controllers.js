var oracleMigration = window.oracleMigration;
oracleMigration.controller('MonitorController', ["$scope", '$http', 'adAlerts', function($scope, $http, adAlerts) {
	$scope.jobs = {
	};

	$scope.searchKeyword = '';
	$scope.lineLengthSelect = {
		"selectValue" : 10
	}

	window.monitorControllerScope = $scope;

	$http({
		method: 'GET',
		url: '/springbased-1.0/jobs'
	}).then(function successCallback(response) {
		$scope.jobs = response.data;
		// transform data to readable
		angular.forEach($scope.jobs, function(value, key) {
			var date = value.startTime;
			if (date === 0) {
				value.startTime = '';
			} else {
				value.startTime = (new Date(date)).toString();
			}
		});
	}, function errorCallback(response) {
	});

	$scope.searchKeywordChanged = function() {
		var a = $scope.lineLengthSelect;
	}

	$scope.lineLengthSelectChanged = function() {
		var b = 0;
	}
			
}]);

$(document).ready(function() {
  $('#table007_filter').remove();
  $('#table007_length').remove();
  $( "select" ).change(function() {
    var str = "";
    $( "select option:selected" ).each(function() {
      str += $( this ).text() + " ";
    });
    window.monitorControllerScope.$apply(function() {
		window.monitorControllerScope.lineLengthSelect.selectValue = str;
    });
    //angular.element(document.querySelector('#searchBoxInput')).triggerHandler('change');
  });
} );