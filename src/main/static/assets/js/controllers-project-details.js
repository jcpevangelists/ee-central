(function () {
    'use strict';

    angular.module('tribe-project-details', ['tribe-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/projects/:project', {
                templateUrl: 'app/page-project-details.html'
            });
        }])
        .controller('ProjectDetailsController', [
            '$scope', '$routeParams', '$sce', 'tribeAppService',
            function ($scope, $routeParams, $sce, tribeAppService) {
                tribeAppService.whenReady(function (data) {
                    var project = data.projects[$routeParams.project];
                    $scope.project = _.clone(project);
                    $scope.project.documentation = $sce.trustAsHtml(project.documentation);
                    $scope.otherProjects = _.filter(_.values(data.projects), function (item) {
                        return item.name !== project.name;
                    });
                });
            }
        ]);
}());
