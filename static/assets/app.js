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
            $scope.$apply()
        });
    }])
    .controller('ProjectDetailController', ['$element', function ($element) {

    }])
    .controller('ProjectsController', ['$element', function ($element) {

    }]);
