angular.module('tribe-project-highlight', ['tribe-app-service'])
    .controller('ProjectHighlightController',
    ['$scope', '$timeout', '$sce', 'tribeAppService',
        function ($scope, $timeout, $sce, tribeAppService) {
            tribeAppService.whenReady(function (data) {
                var projects = _.values(data.projects);
                // just grabbing a random project for now TODO
                var project = projects[Math.floor((Math.random() * projects.length))];
                var description = project.longDescription;
                $scope.highligtedProject = {
                    name: project.name,
                    friendlyName: project.friendlyName,
                    description: $sce.trustAsHtml(description),
                    snapshot: project.snapshot,
                    icon: project.icon
                };
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
        }]);
