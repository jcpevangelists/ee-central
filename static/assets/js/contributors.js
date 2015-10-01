angular.module('tribe-contributors', ['tribe-app-service'])
    .controller('ContributorsListController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeAppService',
        function ($scope, $routeParams, $sce, $timeout, tribeAppService) {
            tribeAppService.whenReady(function (data) {
                $scope.contributors = _.sortBy(data.contributors, function (contributor) {
                    return -1 * contributor.commits;
                });
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
        }
    ])
    .controller('ContributorsPicturesController', [
        '$element', '$scope', '$routeParams', '$sce', '$timeout', 'tribeAppService',
        function ($element, $scope, $routeParams, $sce, $timeout, tribeAppService) {
            tribeAppService.whenReady(function (data) {
                $scope.pictures = _.map(data.pictures, function(pic) {
                    return pic.name;
                });
                $scope.selected = $scope.pictures[Math.floor((Math.random() * $scope.pictures.length))];
                $element.find('.tribe-picture').css('background-image', 'url(rest/images/about/' + $scope.selected + ')')
                $timeout(function () {
                    $scope.$apply();
                }, 0);
                $scope.changePicture = function (pic) {
                    $scope.selected = pic;
                    $element.find('.tribe-picture').css('background-image', 'url(rest/images/about/' + $scope.selected + ')')
                    $timeout(function () {
                        $scope.$apply();
                    }, 0);
                };
            })
        }
    ]);
