angular.module('tribe-contributors', ['tribe-app-service'])
    .controller('ContributorsListController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeAppService',
        function ($scope, $routeParams, $sce, $timeout, tribeAppService) {
            tribeAppService.whenReady(function (data) {
                $scope.contributors = _.sortBy(data.contributors, function (contributor) {
                    var contributions = _.isArray(contributor.contributions) ? contributor.contributions : [contributor.contributions];
                    var totalContributions = _.reduce(_.map(contributions, function (contrib) {
                        return contrib.contributions;
                    }), function (memo, contrib) {
                        return memo + contrib;
                    });
                    return -1 * totalContributions;
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
                $scope.pictures = data.pictures;
                $scope.selected = $scope.pictures[Math.floor((Math.random() * $scope.pictures.length))];
                $element.find('.tribe-picture').css('background-image', 'url(pics/' + $scope.selected + ')')
                $timeout(function () {
                    $scope.$apply();
                }, 0);
                $scope.changePicture = function (pic) {
                    $scope.selected = pic;
                    $element.find('.tribe-picture').css('background-image', 'url(pics/' + $scope.selected + ')')
                    $timeout(function () {
                        $scope.$apply();
                    }, 0);
                };
            })
        }
    ]);
