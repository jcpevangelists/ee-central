(function () {
    'use strict';

    angular.module('tribeio', [
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
        }]);
}());
