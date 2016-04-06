'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('MyCtrl1', ['$scope', function($scope) {
	$scope.formInfo = {};
	
	$scope.saveData = function(){
		$scope.nameRequired = '';
		$scope.emailRequired = '';
		$scope.passwordRequired = '';
		if(!$scope.formInfo.Name){
			$scope.nameRequired = 'Name Required';
		}
		if(!$scope.formInfo.Email){
			$scope.emailRequired = 'Email Required';
		}
		if(!$scope.formInfo.Password){
			$scope.passwordRequired = 'Password Required';
		}
	};
  }])
  .controller('MyCtrl2', [function() {

  }]);

var oracleMigration = window.oracleMigration;
oracleMigration.controller('ConfigController', ["$scope", '$http', 'adAlerts', function($scope, $http, adAlerts) {
	$scope.sourceIp = "10.58.100.66";
	$scope.sourceUsername = "sfuser";
	$scope.sourcePassword = "sfuser";
	$scope.sourceSID = "dbpool1";
	$scope.sourceSchema = "sfuser_tree";

	$scope.sourceValidateResult = 0;
	$scope.showLoadingIconForValidateSourceConnectionButton = false;

	$scope.targetIp = "10.58.100.66";
	$scope.targetUsername = "sfuser";
	$scope.targetPassword = "sfuser";
	$scope.targetSID = "dbpool1";
	$scope.targetSchema = "sfuser_real";

	$scope.targetValidateResult = 0;
	$scope.showLoadingIconForValidateTargetConnectionButton = false;

	$scope.showLoadingIcon = false;
	$scope.successfullyFired = false;

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

	$scope.sourceInputChanged = function () {
		$scope.sourceValidateResult = 0;
	}

	$scope.targetInputChanged = function () {
		$scope.targetValidateResult = 0;
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
			});
		}, function errorCallback(response) {
		});
	};
	$scope.getQuickSelectionOptions();

	$scope.sourceQuickSelectionChange = function() {
		$scope.sourceUsername = $scope.sourceQuickSelectOptionsSelected.username;
		$scope.sourcePassword = $scope.sourceQuickSelectOptionsSelected.password;
		$scope.sourceIp = $scope.sourceQuickSelectOptionsSelected.ip;
		$scope.sourceSID = $scope.sourceQuickSelectOptionsSelected.sid;
	};

	$scope.quickSelectOptions = [
        {
            ip: 'ip1',
            username: 'username1',
            password: 'password1',
            sid: 'sid1',
            url: 'url1'
        },
        {
            ip: 'ip2',
            username: 'username2',
            password: 'password2',
            sid: 'sid2',
            url: 'url2'
        }
    ];

    $scope.sourceQuickSelectOptionsSelected = {};
    if ($scope.quickSelectOptions.length >0) {
    	$scope.sourceQuickSelectOptionsSelected = $scope.quickSelectOptions[0];
    }
}]);