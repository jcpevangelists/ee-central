angular.module('tribeio', [
    'tribe-projects-carousel',
    'tribe-twitter',
    'tribe-project-details',
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
    }]);
