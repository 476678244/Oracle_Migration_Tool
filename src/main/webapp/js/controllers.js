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