angular.module('tribe-contributors', ['tribe-contributors-service'])
    .controller('ContributorsListController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeContributorsService',
        function ($scope, $routeParams, $sce, $timeout, tribeContributorsService) {
            tribeContributorsService.onLoad(function (contributors) {
                $scope.contributors = contributors.getAll();
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
        }
    ]);
