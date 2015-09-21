angular.module('tribe-projects', ['tribe-projects-service'])
    .controller('ProjectsController', ['tribeProjectsService', '$scope', '$timeout', function (tribeProjectsService, $scope, $timeout) {
        tribeProjectsService.onLoad(function (projects) {
            $scope.projects = projects.getAll();
            $scope.baseFullPath = $('head base').first().attr('href');
            $timeout(function () {
                $scope.$apply();
            }, 0);
        });
    }]);
