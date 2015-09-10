angular.module('tribe-project-highlight', [])
    .controller('ProjectHighlightController', ['$scope', '$timeout', '$sce', 'tribeProjectsService', function ($scope, $timeout, $sce, tribeProjectsService) {
        tribeProjectsService.onLoad(function (projects) {
            // just grabbing the first project for now TODO
            var project = projects[0];
            var description = project.longDescription;
            $scope.highligtedProject = {
                name: project.name,
                description: $sce.trustAsHtml(description)

            };
            $timeout(function () {
                $scope.$apply();
            }, 0);
        });
    }]);
