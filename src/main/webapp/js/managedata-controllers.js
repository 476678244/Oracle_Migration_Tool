'use strict';

/* Controllers */

var oracleMigration = window.oracleMigration;
oracleMigration.controller('ManageDataController', ["$scope", '$http', 'adAlerts', '$window','$confirm', 'ngDialog', '$location', '$q',
		 function($scope, $http, adAlerts, $window, $confirm, ngDialog, $location, $q) {

	// $scope.schema = "sfuser_real";
	// $scope.sourceUsername = "sfuser";
	// $scope.sourcePassword = "sfuser";
	// $scope.sourceUrl = "jdbc:oracle:thin:@10.58.100.66:1521:dbpool1";
    $scope.tableSelected = undefined;
    $scope.schemaSelected = undefined;
    $scope.columnSelected = undefined;
    $scope.orderByColumnSelected = undefined;
    $scope.conditionOp1 = undefined;
    $scope.conditionValue1 = "";
    $scope.jsonResult = {};
    $scope.urlSelected = "{}";

    $scope.backToMigratePage = function() {
    	$location.path("/");
    };

    $scope.tableNames = [];

    $scope.schemaNames = [];

    $scope.columnNames = [];

    $scope.conditionOps = [];

    $scope.urls = [];

    $scope.$watch('urlSelected',function(){
    	if ($scope.urlSelected == "{}") {
    		return;
    	}
    	var obj = JSON.parse($scope.urlSelected); 
    	$scope.sourceUsername = obj.username;
		$scope.sourcePassword = obj.password;
		$scope.sourceUrl = obj.url;
        // init
        $scope.initFromSchemaLayer();
    	// fetch tables
    	$scope.fetchSchemaNameOptions($scope.urlSelected);
    });

    $scope.$watch('schemaSelected',function(){
    	if ($scope.schemaSelected === undefined) {
    		return ;
    	}  
        // init
        $scope.initFromTableLayer();
    	// fetch tables
    	$scope.fetchTableNameOptions($scope.schemaSelected);
    });

    $scope.$watch('tableSelected',function(){
    	if ($scope.tableSelected === undefined) {
    		return ;
    	}  
        // init
 		$scope.initFromColumnLayer();
    	// fetch columns
    	$scope.fetchColumnNameOptions($scope.schemaSelected, $scope.tableSelected);
    });

	$scope.initFromSchemaLayer = function() {
		$scope.schemaNames = [];
		$scope.schemaSelected = undefined;
		$scope.initFromTableLayer();
	};

	$scope.initFromTableLayer = function() {
		$scope.tableNames = [];
		$scope.tableSelected = undefined;
		$scope.initFromColumnLayer();
	};

    $scope.initFromColumnLayer = function() {
    	$scope.columnNames = [];
    	$scope.columnSelected = undefined;
    	$scope.orderByColumnSelected = undefined;
    	$scope.conditionOp1 = undefined;
    	$scope.conditionValue1 = "";
    };

    $scope.columnNameSelected = function(selected) {
    	$scope.columnSelected = selected.columnName.name;
    };

    $scope.orderByColumnNameSelected = function(selected) {
    	$scope.orderByColumnSelected = selected.columnName.name;
    };
    
    $scope.conditionOp1Selected = function(selected) {
    	$scope.conditionOp1 = selected.conditionOp.name;
    };

    $scope.fetchSchemaNameOptions = function(url) {
    	$http({
			method: 'GET',
			url: '/springbased-1.0/schema',
			params: {
				"sourceUsername": $scope.sourceUsername,
				"sourcePassword": $scope.sourcePassword,
				"sourceUrl": $scope.sourceUrl
			}
		}).then(function successCallback(response) {
			var data = response.data;
			var schemaNames = [];
			angular.forEach(data, function(value, key){
			    var obj = {};
			    obj.name = value;
			    schemaNames.push(obj);
			});
			$q.all(schemaNames).then(function () {
	            $scope.schemaNames = schemaNames;
	        });
		}, function errorCallback(response) {
		});
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
				"schema": $scope.schemaSelected,
				"table": $scope.tableSelected,
				"column": $scope.columnSelected,
				"columnOperator": $scope.conditionOp1,
				"value": $scope.conditionValue1,
				"orderBy": $scope.orderByColumnSelected
			}
		}).then(function successCallback(response) {
			var data = response.data;
			$scope.jsonResult = data;
		}, function errorCallback(response) {
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
				$scope.urls.push(quickSelectOption);
			});
		}, function errorCallback(response) {
		});
	};
	$scope.getQuickSelectionOptions();
    // fetch all table name options at first step
    $scope.fetchConditionOps();

}]);