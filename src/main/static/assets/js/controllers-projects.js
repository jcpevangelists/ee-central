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
                $scope.openPopup = function (url) {
                    $window.open(url, 'name', 'width=600,height=400');
                };
                tribeAppService.whenReady(function (data) {
                    $scope.projects = _.values(data.projects);
                    $scope.baseFullPath = angular.element('head base').first().attr('href');
                });
            }]);
}());
