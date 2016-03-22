///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-main', [
    'ngRoute',
    'ngStorage'
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
