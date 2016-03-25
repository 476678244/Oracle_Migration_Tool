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
oracleMigration.controller('ConfigController', ["$scope", '$http', function($scope, $http) {
	$scope.sourceIp = "10.58.100.66";
	$scope.sourceUsername = "sfuser";
	$scope.sourcePassword = "sfuser";
	$scope.sourceSID = "dbpool1";
	$scope.sourceSchema = "sfuser_tree";

	$scope.sourceValidateResult = 0;

	$scope.targetIp = "10.58.100.66";
	$scope.targetUsername = "sfuser";
	$scope.targetPassword = "sfuser";
	$scope.targetSID = "dbpool1";
	$scope.targetSchema = "sfuser_real";

	$scope.targetValidateResult = 0;

	$scope.validate = function (source) {
		if (source) {
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
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		} else {
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
			}, function errorCallback(response) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
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
		return true;
	}
}]);