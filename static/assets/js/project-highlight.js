angular.module('tribe-project-highlight', [])
    .controller('ProjectHighlightController', ['$scope', '$timeout', '$sce', 'tribeProjectsService', function ($scope, $timeout, $sce, tribeProjectsService) {
        tribeProjectsService.onLoad(function (projects) {
            // just grabbing the first project for now TODO
            $scope.highligtedProject = $sce.trustAsHtml(projects[0].longDescription);
            $timeout(function () {
                $scope.$apply();
            }, 0);
        });
    }]);
