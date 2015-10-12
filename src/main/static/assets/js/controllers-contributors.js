(function () {
    'use strict';

    angular.module('tribe-contributors', ['tribe-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/contributors', {
                templateUrl: 'app/page-contributors.html'
            });
        }])
        .controller('ContributorsListController', [
            '$scope', 'tribeAppService',
            function ($scope, tribeAppService) {
                tribeAppService.whenReady(function (data) {
                    $scope.contributors = _.sortBy(data.contributors, function (contributor) {
                        return -1 * contributor.commits;
                    });
                });
            }
        ])
        .controller('ContributorsPicturesController', [
            '$element', '$scope', 'tribeAppService',
            function ($element, $scope, tribeAppService) {
                tribeAppService.whenReady(function (data) {
                    $scope.pictures = _.map(data.pictures, function (pic) {
                        return pic.name;
                    });
                    $scope.selected = $scope.pictures[Math.floor((Math.random() * $scope.pictures.length))];
                    $element.find('.tribe-picture').css('background-image', 'url(rest/images/about/' + $scope.selected + ')');
                    $scope.changePicture = function (pic) {
                        $scope.selected = pic;
                        $element.find('.tribe-picture').css('background-image', 'url(rest/images/about/' + $scope.selected + ')');
                    };
                });
            }
        ]);
}());
