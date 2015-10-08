(function () {
    'use strict';

    angular.module('tribeio', [
        'tribe-projects',
        'tribe-contributors',
        'tribe-projects-carousel',
        'tribe-twitter',
        'tribe-project-details',
        'tribe-project-highlight',
        'ngRoute'])
        .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
            $routeProvider.when('/', {
                templateUrl: 'app/page-main.html'
            }).when('/projects', {
                templateUrl: 'app/page-projects.html'
            }).when('/contributors', {
                templateUrl: 'app/page-contributors.html'
            }).when('/projects/:project', {
                templateUrl: 'app/page-project-details.html'
            });
            $locationProvider.html5Mode({
                enabled: true,
                requireBase: false
            });
        }])
        .controller('HeaderImageController', ['$element', '$window', function ($element, $window) {
            var el = $($window);
            el.scroll(function () {
                var step = el.scrollTop();
                $element.css({
                    'transform': 'translateY(' + (step / 3) + 'px)'
                });
            });
        }])
        .directive('scrollToTop', ['$timeout', function ($timeout) {
            return {
                link: function ($scope) {
                    $scope.scrollToTop = function () {
                        $timeout(function () {
                            $("html, body").animate({scrollTop: 0}, "slow");
                        });
                    };
                }
            };
        }]);
}());
