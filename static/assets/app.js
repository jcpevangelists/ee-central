angular.module('tribeio', ['ngRoute'])
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
    .controller('ProjectsCarousselController', ['$element', '$location', '$http', '$scope', function ($element, $location, $http, $scope) {
        $http.get('rest/projects').success(function (data) {
            $scope.projects = data.dtoProject;
        });
        var carousel = $element.find('div.tribe-projects-caroussel').first();
        $element.find('i.tribe-left').on('click', function () {
            var article = $element.find('article:first-child');
            article.addClass('slide-in');
            $scope.$apply();
            article.detach();
            article.removeClass('slide-in');
            $scope.$apply();
            article.appendTo(carousel);
            $scope.$apply();
        });
        $element.find('i.tribe-right').on('click', function () {
            var article = $element.find('article:last-child');
            article.addClass('slide-out');
            $scope.$apply();
            article.detach();
            article.removeClass('slide-out');
            $scope.$apply();
            article.prependTo(carousel);
            $scope.$apply();
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
    .controller('TwitterUpdatesController', ['$element', '$scope', '$http', function ($element, $scope, $http) {
        $http.get('rest/twitter').success(function (data) {
            $scope.tweets = data.dtoTweet;
            var position = 0;
            $scope.tweet = $scope.tweets[position];
            $element.find('i.tribe-left').on('click', function () {
                position = position - 1;
                if (position < 0) {
                    position = data.dtoTweet.length - 1;
                }
                $scope.tweet = $scope.tweets[position];
                $scope.$apply();
            });
            $element.find('i.tribe-right').on('click', function () {
                position = position + 1;
                if (position > data.dtoTweet.length - 1) {
                    position = 0;
                }
                $scope.tweet = $scope.tweets[position];
                $scope.$apply();
            });
        });
    }])
    .controller('ProjectDetailsController', ['$scope', '$routeParams', function ($scope, $routeParams) {
        $scope.projectName = $routeParams.project;
    }]);
