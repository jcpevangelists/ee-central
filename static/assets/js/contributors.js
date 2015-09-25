angular.module('tribe-contributors', ['tribe-contributors-service', 'tribe-pictures-service', 'tribe-projects-service'])
    .controller('ContributorsListController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeContributorsService', 'tribeProjectsService',
        function ($scope, $routeParams, $sce, $timeout, tribeContributorsService, tribeProjectsService) {
            tribeProjectsService.onLoad(function (projectsData) {
                tribeContributorsService.onLoad(function (contributors) {
                    var contributors = _.sortBy(contributors.getAll(), function (contributor) {
                        var contributions = _.isArray(contributor.contributions) ? contributor.contributions : [contributor.contributions];
                        var totalContributions = _.reduce(_.map(contributions, function (contrib) {
                            return contrib.contributions;
                        }), function (memo, contrib) {
                            return memo + contrib;
                        });
                        return -1 * totalContributions;
                    });
                    $scope.contributors = _.map(contributors, function (contributor) {
                        var contributions = _.isArray(contributor.contributions) ? contributor.contributions : [contributor.contributions];
                        contributor.contributions = _.map(contributions, function (contrib) {
                            contrib.project = projectsData.getByName(contrib.project);
                            return contrib;
                        });
                        return contributor;
                    });

                    $timeout(function () {
                        $scope.$apply();
                    }, 0);
                });
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
