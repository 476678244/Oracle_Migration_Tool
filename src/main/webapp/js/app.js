'use strict';

// Declare app level module which depends on filters, and services
angular.module('myApp', [
  'ngRoute',
  'myApp.filters',
  'myApp.services',
  'myApp.directives',
  'myApp.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {templateUrl: 'partials/partial1.html', controller: 'MyCtrl1'});
  $routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: 'MyCtrl2'});
  $routeProvider.otherwise({redirectTo: '/view1'});
}]);

// oracle migration main module
var oracleMigration = angular.module('oracleMigration', ['ngRoute', 'ngSanitize', 'adaptv.adaptStrap']);
  oracleMigration.config(['$routeProvider', function($routeProvider){
    $routeProvider.when('/', {
      templateUrl : './new.html',
      controller  : 'ConfigController'
    });
  }]);

window.oracleMigration = oracleMigration;