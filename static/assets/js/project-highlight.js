angular.module('tribe-project-highlight', [])
    .controller('ProjectHighlightController', ['$scope', '$timeout', '$sce', 'tribeProjectsService', function ($scope, $timeout, $sce, tribeProjectsService) {
        tribeProjectsService.onLoad(function (projects) {
            // just grabbing a random project for now TODO
            var project = projects[Math.floor((Math.random() * projects.length))];
            var description = project.longDescription;
            $scope.highligtedProject = {
                name: project.name,
                description: $sce.trustAsHtml(description),
                snapshot: project.snapshot,
                icon: project.icon
            };
            $timeout(function () {
                $scope.$apply();
            }, 0);
        });
    }]);
