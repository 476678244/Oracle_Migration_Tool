'use strict';

// oracle migration main module
var oracleMigration = angular.module('oracleMigration', 
    ['ngRoute', 'ngSanitize', 'adaptv.adaptStrap', 'ngDropdowns', 'angular-confirm', 'ui.bootstrap.tpls', 'ngDialog', 'jsonFormatter']);
  oracleMigration.config(['$routeProvider', function($routeProvider){
    $routeProvider.when('/', {
      templateUrl : './tmpl/config.html',
      controller  : 'ConfigController'
    }).when('/manageData', {
      templateUrl : './tmpl/managedata.html',
      controller  : 'ManageDataController'
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