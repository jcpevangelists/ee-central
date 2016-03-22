(function () {
    'use strict';

    angular.module('javaee-contributors', ['javaee-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/contributors', {
                templateUrl: 'app/page-contributors.html'
            });
        }])
        .controller('ContributorsListController', [
            '$scope', 'javaeeAppService',
            function ($scope, javaeeAppService) {
                javaeeAppService.whenReady(function (data) {
                    $scope.contributors = _.sortBy(data.contributors, function (contributor) {
                        return -1 * contributor.commits;
                    });
                });
            }
        ])
        .controller('ContributorsPicturesController', [
            '$element', '$scope', 'javaeeAppService',
            function ($element, $scope, javaeeAppService) {
                javaeeAppService.whenReady(function (data) {
                    $scope.pictures = _.map(data.pictures, function (pic) {
                        return pic.name;
                    });
                    $scope.selected = $scope.pictures[Math.floor((Math.random() * $scope.pictures.length))];
                    $element.find('.javaee-picture').css('background-image', 'url(rest/images/about/' + $scope.selected + ')');
                    $scope.changePicture = function (pic) {
                        $scope.selected = pic;
                        $element.find('.javaee-picture').css('background-image', 'url(rest/images/about/' + $scope.selected + ')');
                    };
                });
            }
        ]);
}());
