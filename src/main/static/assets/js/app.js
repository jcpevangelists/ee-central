(function () {
    'use strict';

    angular.module('tribeio', [
        'tribe-app-directives',
        'tribe-controllers-home',
        'tribe-projects',
        'tribe-contributors',
        'tribe-twitter',
        'tribe-project-details'])
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
