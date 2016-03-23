///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-header', [])

    .directive('eeioHeader', [function () {
        return {
            restrict: 'E',
            scope: {
                login: '='
            },
            templateUrl: 'app/templates/dir_header.html'
        };
    }])

    .run(function () {
        // placeholder
    });