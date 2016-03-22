(function () {
    'use strict';

    angular.module('javaee-projects', ['javaee-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/projects', {
                templateUrl: 'app/page-projects.html'
            });
        }])
        .controller('ProjectsController', ['javaeeAppService', '$scope', '$window',
            function (javaeeAppService, $scope, $window) {
                javaeeAppService.whenReady(function (data) {
                    $scope.projects = _.values(data.projects);
                });
            }]);
}());
