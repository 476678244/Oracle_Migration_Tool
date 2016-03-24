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
oracleMigration.controller('ConfigController', ["$scope", function($scope) {
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
			if ($scope.sourceIp == "10.58.100.66") {
				$scope.sourceValidateResult = 1;
			} else {
				$scope.sourceValidateResult = -1;
			}
		}
	}

	$scope.validateSuccess = function(result) {
		if (result > 0) {
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
}]);