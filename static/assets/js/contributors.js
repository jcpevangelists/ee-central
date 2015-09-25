angular.module('tribe-contributors', ['tribe-contributors-service', 'tribe-pictures-service'])
    .controller('ContributorsListController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeContributorsService',
        function ($scope, $routeParams, $sce, $timeout, tribeContributorsService) {
            tribeContributorsService.onLoad(function (contributors) {
                $scope.contributors = _.sortBy(contributors.getAll(), function (contributor) {
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
        '$element', '$scope', '$routeParams', '$sce', '$timeout', 'tribePicturesService',
        function ($element, $scope, $routeParams, $sce, $timeout, tribePicturesService) {
            tribePicturesService.onLoad(function (pictures) {
                $scope.pictures = pictures.getAll();
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
