(function () {
    'use strict';

    angular.module('tribe-projects', ['tribe-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/projects', {
                templateUrl: 'app/page-projects.html'
            });
        }])
        .controller('ProjectsController', ['tribeAppService', '$scope', '$window',
            function (tribeAppService, $scope, $window) {
                tribeAppService.whenReady(function (data) {
                    $scope.projects = _.values(data.projects);
                });
            }]);
}());
