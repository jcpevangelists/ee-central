(function () {
    'use strict';

    angular.module('tribeio', [
        'tribe-controllers-home',
        'tribe-projects',
        'tribe-contributors',
        'tribe-twitter',
        'tribe-project-details'])
        .config(['$locationProvider', function ($locationProvider) {
            $locationProvider.html5Mode({
                enabled: true,
                requireBase: true
            });
        }])
        .directive('tribeShareProject', ['$window', function ($window) {
            return {
                restrict: 'A',
                link: function ($scope, $element, $attrs) {
                    $element.bind('click', function () {
                        var url;
                        if ('twitter' === $attrs.tribeShareProject) {
                            url = 'https://twitter.com/intent/tweet?text=Check out http:'
                                + $scope.baseFullPath + 'projects/' + $scope.project.name;
                        } else if ('facebook' === $attrs.tribeShareProject) {
                            url = 'http://www.facebook.com/sharer/sharer.php?u=http:'
                                + $scope.baseFullPath + 'projects/' + $scope.project.name;
                        }
                        if (url) {
                            $window.open(url, 'name', 'width=600,height=400');
                        }

                    });
                }
            };
        }])
        .run(function ($rootScope) {
            $rootScope.baseFullPath = angular.element('head base').first().attr('href');
        });
}());
