'use strict';

/* Controllers */

var oracleMigration = window.oracleMigration;
oracleMigration.controller('ConfigController', ["$scope", '$http', 'adAlerts', '$window','$confirm', 'ngDialog', '$location',
		 function($scope, $http, adAlerts, $window, $confirm, ngDialog, $location) {
	$scope.sourceIp = "10.58.100.66";
	$scope.sourceUsername = "sfuser";
	$scope.sourcePassword = "sfuser";
	$scope.sourceSID = "dbpool1";
	$scope.sourceSchema = "";

	$scope.sourceValidateResult = 0;
	$scope.showLoadingIconForValidateSourceConnectionButton = false;

	$scope.targetIp = "10.58.100.66";
	$scope.targetUsername = "sfuser";
	$scope.targetPassword = "sfuser";
	$scope.targetSID = "dbpool1";
	$scope.targetSchema = "";

	$scope.targetValidateResult = 0;
	$scope.showLoadingIconForValidateTargetConnectionButton = false;
	$scope.showLoadingIconForRecreate = false;
	$scope.showLoadingIcon = false;
	$scope.successfullyFired = false;

	$scope.recreateSuccess = false;
	$scope.validate = function (source) {
		if (source) {
			$scope.showLoadingIconForValidateSourceConnectionButton = true;
			$http({
				method: 'GET',
				url: '/springbased-1.0/validateSourceConnection',
				params: {
					ip: $scope.sourceIp,
					username: $scope.sourceUsername,
					password: $scope.sourcePassword,
					sid: $scope.sourceSID,
					schema: $scope.sourceSchema
				}
			}).then(function successCallback(response) {
				$scope.sourceValidateResult = response.data.status;
				$scope.sourceValidateMessage = response.data.cause;
				$scope.showLoadingIconForValidateSourceConnectionButton = false;
			}, function errorCallback(response) {
			});
		} else {
			$scope.showLoadingIconForValidateTargetConnectionButton = true;
			$http({
				method: 'GET',
				url: '/springbased-1.0/validateTargetConnection',
				params: {
					ip: $scope.targetIp,
					username: $scope.targetUsername,
					password: $scope.targetPassword,
					sid: $scope.targetSID,
					schema: $scope.targetSchema
				}
			}).then(function successCallback(response) {
				$scope.targetValidateResult = response.data.status;
				$scope.targetValidateMessage = response.data.cause;
				$scope.showLoadingIconForValidateTargetConnectionButton = false;
			}, function errorCallback(response) {
			});			
		}
	}

	$scope.validateSuccess = function(result) {
		if (result === 1) {
			return true;
		}
		return false;
	} 

	$scope.validateFail = function(result) {
		if (result < 0) {
			return true;
		}
		return false;
	} 

	$scope.validateWarn = function(result) {
		if (result === 2) {
			return true;
		}
		return false;
	}

	$scope.showToValidateButton = function() {
		if ($scope.showLoadingIcon) {
			return false;
		}
		if ($scope.targetValidateResult > 0 && $scope.sourceValidateResult > 0) {
			return false;
		}
		return true;
	}
	var showToValidateButton = $scope.showToValidateButton;

	$scope.showToMigrateButton = function() {
		if (showToValidateButton()) {
			return false;
		}
		if ($scope.showLoadingIcon) {
			return false;
		}
		if ($scope.successfullyFired) {
			return false;
		}
		return true;
	}

	$scope.sourceInputChanged = function (schema) {
		$scope.sourceValidateResult = 0;
		if (!schema) {
			$scope.sourceQuickSelectOptionsSelected = {};
		}
	}

	$scope.targetInputChanged = function (schema) {
		$scope.targetValidateResult = 0;
		if (!schema) {
			$scope.targetQuickSelectOptionsSelected = {};
		}
	}

	$scope.fireMigration = function() {
		$scope.showLoadingIcon = true;
		var sourceUrl = "jdbc:oracle:thin:@" + $scope.sourceIp + ":1521:" + $scope.sourceSID;
		var targetUrl = "jdbc:oracle:thin:@" + $scope.targetIp + ":1521:" + $scope.targetSID;
		$http({
			method: 'GET',
			url: '/springbased-1.0/fireMigration',
			params: {
				sourceUsername: $scope.sourceUsername,
				sourcePassword: $scope.sourcePassword,
				sourceUrl: sourceUrl,
				sourceSchema: $scope.sourceSchema,
				targetUsername: $scope.targetUsername,
				targetPassword: $scope.targetPassword,
				targetUrl: targetUrl,
				targetSchema: $scope.targetSchema
			}
		}).then(function successCallback(response) {
			$scope.showLoadingIcon = false;
			$scope.successfullyFired = true;
			adAlerts.success('Success!', 'Migration Job has been scheduled! Job Id:' + response.data);
		}, function errorCallback(response) {
			adAlerts.error('Error!', 'Internal Error, please contact Zonghan for help.');
		});
	};

	$scope.getQuickSelectionOptions = function() {
		$http({
			method: 'GET',
			url: '/springbased-1.0/connections'
		}).then(function successCallback(response) {
			angular.forEach(response.data, function(value, key) {
				var quickSelectOption = {};
				quickSelectOption.username = value.username;
				quickSelectOption.password = value.password;
				quickSelectOption.ip = value.url.replace('jdbc:oracle:thin:@','').replace(/:1521:.*/,'');
				quickSelectOption.sid = value.url.replace(/jdbc:oracle:thin:@.*:1521:/,'');
				quickSelectOption.url = value.url;
				$scope.quickSelectOptions.push(quickSelectOption);
				$scope.sourceQuickSelectOptionsSelected = quickSelectOption;
				$scope.targetQuickSelectOptionsSelected = quickSelectOption;
			});
		}, function errorCallback(response) {
		});
	};
	$scope.getQuickSelectionOptions();

	$scope.sourceQuickSelectionChange = function(selected) {
		$scope.sourceUsername = selected.username;
		$scope.sourcePassword = selected.password;
		$scope.sourceIp = selected.ip;
		$scope.sourceSID = selected.sid;
	};

	$scope.targetQuickSelectionChange = function(selected) {
		$scope.targetUsername = selected.username;
		$scope.targetPassword = selected.password;
		$scope.targetIp = selected.ip;
		$scope.targetSID = selected.sid;
	};

	$scope.quickSelectOptions = [
    ];

    $scope.sourceQuickSelectOptionsSelected = {};
    $scope.targetQuickSelectOptionsSelected = {};

    $scope.gotoMonitorPage = function() {
    	$window.location.href = 'dashboard/html/table/datatable.html';
    };

    $scope.recreateSchema = function() {
    	$confirm({text: 'Are you sure you want to delete and create this schema?'}).then(function() {
    		$scope.showLoadingIconForRecreate = true;
    		$scope.recreateSuccess = false;
			$http({
				method: 'GET',
				url: '/springbased-1.0/recreateSchema',
				params: {
					ip: $scope.targetIp,
					username: $scope.targetUsername,
					password: $scope.targetPassword,
					sid: $scope.targetSID,
					schema: $scope.targetSchema
				}
			}).then(function successCallback(response) {
				$scope.showLoadingIconForRecreate = false;
				$scope.recreateSuccess = true;
				$scope.targetValidateResult = 1;
			}, function errorCallback(response) {
			});
        });
    };

    $scope.selectSchema = function () {
        $location.path('/manageData');
        //$window.location.href = 'managedata.html';
    };
}]);