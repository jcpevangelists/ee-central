angular.module('tribeio', [
    'tribe-projects',
    'tribe-projects-carousel',
    'tribe-twitter',
    'tribe-project-details',
    'tribe-project-highlight',
    'ngRoute'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider.when('/', {
            templateUrl: 'app/main.html'
        }).when('/projects', {
            templateUrl: 'app/projects.html'
        }).when('/project-details/:project', {
            templateUrl: 'app/project-details.html'
        });
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }])
    .controller('HeaderImageController', ['$element', function ($element) {
        $(window).scroll(function () {
            var step = $(this).scrollTop();
            $element.css({
                'transform': 'translateY(' + (step / 3) + 'px)'
            });
        });
    }])
    .directive('prism', ['$timeout', function ($timeout) {
        return {
            restrict: 'A',
            link: function ($scope, $element, $attrs) {
                $scope.$watch(
                    function () {
                        return $scope.$eval($attrs.ngBindHtml);
                    },
                    function (value) {
                        $timeout(function () {
                            $element.find('pre').each(function () {
                                $(this).addClass('line-numbers');
                            });
                            $element.find('code').each(function () {
                                Prism.highlightElement(this);
                            });
                        });
                    }
                );
            }
        }
    }]);
