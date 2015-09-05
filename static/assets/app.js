angular.module('tribeio', ['ngRoute'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider.when('/', {
            templateUrl: 'app/main.html'
        }).when('/projects', {
            templateUrl: 'app/projects.html'
        }).when('/project-details', {
            templateUrl: 'app/project-details.html'
        });
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }])
    .controller('ProjectsCarousselController', ['$element', '$location', '$scope', function ($element, $location, $scope) {
        $element.find('article').on('click', function () {
            $location.url('/project-details');
            $scope.$apply();
        });
        $element.find('i.tribe-left').on('click', function () {
            var article = $element.find('article:last-child');
            article.detach().appendTo('div.tribe-projects-caroussel');
        });
        $element.find('i.tribe-right').on('click', function () {
            var article = $element.find('article:first-child');
            article.detach().prependTo('div.tribe-projects-caroussel');
        });
    }])
    .controller('ProjectDetailController', ['$element', function ($element) {

    }])
    .controller('ProjectsController', ['$element', function ($element) {

    }])
    .controller('HeaderImageController', ['$element', function ($element) {
        $(window).scroll(function() {
            var step = $(this).scrollTop();
            $element.css({
                'transform': 'translateY(' + (step / 3) + 'px)'
            });
        });

    }]);
