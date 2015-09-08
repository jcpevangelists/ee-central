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
        var carousel = $element.find('div.tribe-projects-caroussel').first();
        $element.find('article').on('click', function () {
            $location.url('/project-details');
            $scope.$apply();
        });
        $element.find('i.tribe-left').on('click', function () {
            var article = $element.find('article:first-child');
            article.detach();
            $scope.$apply();
            article.appendTo(carousel);
            $scope.$apply();
        });
        $element.find('i.tribe-right').on('click', function () {
            var article = $element.find('article:last-child');
            article.detach();
            $scope.$apply();
            article.prependTo(carousel);
            $scope.$apply();
        });
    }])
    .controller('HeaderImageController', ['$element', function ($element) {
        $(window).scroll(function() {
            var step = $(this).scrollTop();
            $element.css({
                'transform': 'translateY(' + (step / 3) + 'px)'
            });
        });

    }]);
