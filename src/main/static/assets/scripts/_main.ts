///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-main', [
    'ngRoute',
    'ngStorage',
    'javaeeio-header',
    'javaeeio-projects',
    'javaeeio-contributors'
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
                .when('/project/:owner/:name/:resource*', {
                    templateUrl: 'app/templates/page_project.html',
                    controller: ['$route', '$scope', function ($route, $scope) {
                        $scope.owner = $route.current.params['owner'];
                        $scope.name = $route.current.params['name'];
                        $scope.resource = $route.current.params['resource'];
                    }]
                })
                .otherwise({
                    controller: ['$scope', '$location', function ($scope, $location) {
                        $scope.path = $location.path();
                    }],
                    templateUrl: 'app/templates/page_404.html'
                });
        }
    ])

    .run(['$rootScope', function ($rootScope) {
        $rootScope.baseFullPath = angular.element('head base').first().attr('href');
    }])

    .run(function () {
        // placeholder
    });
