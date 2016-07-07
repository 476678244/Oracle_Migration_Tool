'use strict';

/* Controllers */

var oracleMigration = window.oracleMigration;
oracleMigration.controller('ManageDataController', ["$scope", '$http', 'adAlerts', '$window','$confirm', 'ngDialog', '$location',
		 function($scope, $http, adAlerts, $window, $confirm, ngDialog, $location) {

	$scope.schema = "sfuser_real";
	$scope.sourceUsername = "sfuser";
	$scope.sourcePassword = "sfuser";
	$scope.sourceUrl = "jdbc:oracle:thin:@10.58.100.66:1521:dbpool1";
    $scope.tableSelected = undefined;
    $scope.columnSelected = undefined;
    $scope.conditionOp1 = undefined;
    $scope.conditionValue1 = "";
    $scope.jsonResult = {};

	$scope.gotoMonitorPage = function() {
    	$window.location.href = 'dashboard/html/table/datatable.html';
    };

    $scope.backToMigratePage = function() {
    	$location.path("/");
    };

    $scope.tableNames = [];

    $scope.columnNames = [];

    $scope.conditionOps = [];

    $scope.$watch('tableSelected',function(){
        // init
    	$scope.columnNames = [];
    	$scope.columnSelected = undefined;
    	$scope.conditionOp1 = undefined;
    	$scope.conditionValue1 = "";
    	// fetch columns
    	$scope.fetchColumnNameOptions($scope.schema, $scope.tableSelected);
    })

    $scope.columnNameSelected = function(selected) {
    	$scope.columnSelected = selected.columnName.name;
    };

    $scope.conditionOp1Selected = function(selected) {
    	$scope.conditionOp1 = selected.conditionOp.name;
    };

    $scope.fetchTableNameOptions = function(schema) {
    	$http({
			method: 'GET',
			url: '/springbased-1.0/table',
			params: {
				"sourceUsername": $scope.sourceUsername,
				"sourcePassword": $scope.sourcePassword,
				"sourceUrl": $scope.sourceUrl,
				"schema" : schema
			}
		}).then(function successCallback(response) {
			var data = response.data;
			angular.forEach(data, function(value, key){
			    var obj = {};
			    obj.name = value;
			    $scope.tableNames.push(obj);
			});
		}, function errorCallback(response) {
		});
    };

    $scope.fetchColumnNameOptions = function(schema, tableName) {
    	$http({
			method: 'GET',
			url: '/springbased-1.0/column',
			params: {
				"sourceUsername": $scope.sourceUsername,
				"sourcePassword": $scope.sourcePassword,
				"sourceUrl": $scope.sourceUrl,
				"schema": schema,
				"table": tableName
			}
		}).then(function successCallback(response) {
			var data = response.data;
			angular.forEach(data, function(value, key){
			    var obj = {};
			    obj.name = value;
			    $scope.columnNames.push(obj);
			});
		}, function errorCallback(response) {
		});
    };

    $scope.fetchConditionOps = function() {
    	$http({
			method: 'GET',
			url: '/springbased-1.0/columnOperator'
		}).then(function successCallback(response) {
			var data = response.data;
			angular.forEach(data, function(value, key){
			    var obj = {};
			    obj.name = value;
			    $scope.conditionOps.push(obj);
			});
		}, function errorCallback(response) {
		});
    };

    $scope.query = function() {
    	$http({
			method: 'GET',
			url: '/springbased-1.0/tableDataInJson',
			params: {
				"sourceUsername": $scope.sourceUsername,
				"sourcePassword": $scope.sourcePassword,
				"sourceUrl": $scope.sourceUrl,
				"schema": $scope.schema,
				"table": $scope.tableSelected,
				"column": $scope.columnSelected,
				"columnOperator": $scope.conditionOp1,
				"value": $scope.conditionValue1
			}
		}).then(function successCallback(response) {
			var data = response.data;
			$scope.jsonResult = data;
		}, function errorCallback(response) {
		});
    };

    // fetch all table name options at first step
    $scope.fetchTableNameOptions($scope.schema);
    $scope.fetchConditionOps();

}]);