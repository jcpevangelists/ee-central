///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-main', [
    'ngRoute',
    'ngStorage',
    'javaeeio-header',
    'javaeeio-footer',
    'javaeeio-projects',
    'javaeeio-contributors',
    'javaeeio-googlegroups'
])

    .config([
        '$locationProvider', '$routeProvider',
        function ($locationProvider, $routeProvider) {
            $locationProvider.html5Mode({
                enabled: true,
                requireBase: true
            });
            $routeProvider
                .when('/', {
                    templateUrl: 'app/templates/page_home.html'
                })
                .when('/contributors', {
                    templateUrl: 'app/templates/page_contributors.html'
                })
                .when('/project/:configFile/:resourceName*', {
                    templateUrl: 'app/templates/page_project.html',
                    controller: 'ProjectPageController'
                })
                .when('/project/:configFile', {
                    templateUrl: 'app/templates/page_project.html',
                    controller: 'ProjectPageController'
                })
                .otherwise({
                    controller: ['$scope', '$location', function ($scope, $location) {
                        $scope.path = $location.path();
                    }],
                    templateUrl: 'app/templates/page_404.html'
                });
        }
    ])

    .controller('ProjectPageController', ['$route', '$scope', function ($route, $scope) {
        $scope.configFile = $route.current.params['configFile'];
        $scope.resource = $route.current.params['resourceName'];
    }])

    .run(['$rootScope', function ($rootScope) {
        $rootScope.baseFullPath = angular.element('head base').first().attr('href');
    }])

    .run(function () {
        // placeholder
    });
