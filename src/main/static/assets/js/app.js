(function () {
    'use strict';

    angular.module('javaeeio', [
        'javaee-app-directives',
        'javaee-controllers-home',
        'javaee-projects',
        'javaee-contributors',
        'javaee-twitter',
        'javaee-project-details',
        'javaee-project-long-documentation'])
        .config(['$locationProvider', function ($locationProvider) {
            $locationProvider.html5Mode({
                enabled: true,
                requireBase: true
            });
        }])
        .run(function ($rootScope) {
            $rootScope.baseFullPath = angular.element('head base').first().attr('href');
        });
}());
