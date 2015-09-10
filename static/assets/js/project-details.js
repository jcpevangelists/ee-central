angular.module('tribe-project-details', [])
    .controller('ProjectDetailsController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeProjectsService',
        function ($scope, $routeParams, $sce, $timeout, tribeProjectsService) {
            tribeProjectsService.onLoad(function (projects) {
                var project = projects.getByName($routeParams.project);
                $scope.project = {
                    name: project.name,
                    shortDescription: project.shortDescription,
                    documentation: $sce.trustAsHtml(project.documentation),
                    icon: project.icon
                };
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
        }]);
