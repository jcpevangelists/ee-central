angular.module('tribe-project-highlight', [])
    .controller('ProjectHighlightController', ['$scope', '$timeout', '$sce', 'tribeProjectsService', function ($scope, $timeout, $sce, tribeProjectsService) {
        tribeProjectsService.onLoad(function (projects) {
            // just grabbing the first project for now TODO
            var description = projects[0].longDescription;
            window.console.log(description);
            $scope.highligtedProject = $sce.trustAsHtml(description);
            $timeout(function () {
                $scope.$apply();
            }, 0);
        });
    }]);
