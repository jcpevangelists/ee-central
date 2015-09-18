angular.module('tribe-contributors', ['tribe-contributors-service', 'tribe-pictures-service'])
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
    ])
    .controller('ContributorsPicturesController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribePicturesService',
        function ($scope, $routeParams, $sce, $timeout, tribePicturesService) {
            tribePicturesService.onLoad(function (pictures) {
                $scope.pictures = pictures.getAll();
                $scope.selected = $scope.pictures[Math.floor((Math.random() * $scope.pictures.length))];
                $timeout(function () {
                    $scope.$apply();
                }, 0);
                $scope.changePicture = function(pic) {
                    $scope.selected = pic;
                    $timeout(function () {
                        $scope.$apply();
                    }, 0);
                };
            })
        }
    ]);
