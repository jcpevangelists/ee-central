(function () {
    'use strict';

    angular.module('tribe-projects', ['tribe-app-service'])
        .controller('ProjectsController', ['tribeAppService', '$scope', '$timeout',
            function (tribeAppService, $scope, $timeout) {
                $scope.openPopup = function (url) {
                    window.open(url, 'name', 'width=600,height=400');
                };
                tribeAppService.whenReady(function (data) {
                    $scope.projects = _.values(data.projects);
                    $scope.baseFullPath = $('head base').first().attr('href');
                    $timeout(function () {
                        $scope.$apply();
                    }, 0);
                });
            }]);
}());
