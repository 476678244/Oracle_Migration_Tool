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
var oracleMigration = angular.module('oracleMigration', ['ngRoute', 'ngSanitize', 'adaptv.adaptStrap', 'ngDropdowns']);
  oracleMigration.config(['$routeProvider', function($routeProvider){
    $routeProvider.when('/', {
      templateUrl : './new.html',
      controller  : 'ConfigController'
    });
  }]);

window.oracleMigration = oracleMigration;
oracleMigration.run(function ($templateCache) {
    $templateCache.put('ngDropdowns/templates/dropdownSelect.html', [
      '<div ng-class="{\'disabled\': dropdownDisabled}" class="wrap-dd-select" tabindex="0">',
      '<span class="selected">{{"Drop down to select from history"}}</span>',
      '<ul class="dropdown">',
      '<li ng-repeat="item in dropdownSelect"',
      ' class="dropdown-item"',
      ' dropdown-select-item="item"',
      ' dropdown-item-label="labelField">',
      '</li>',
      '</ul>',
      '</div>'
    ].join(''));
});