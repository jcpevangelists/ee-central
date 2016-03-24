///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-header', [])

    .directive('eeioHeader', [function () {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                login: '='
            },
            templateUrl: 'app/templates/dir_header.html',
            link: function(scope, el) {
                var transcludeEl = el.find('div[ng-transclude]');
                if(transcludeEl.html() === '') {
                    el.find('.title').remove();
                }
            }
        };
    }])

    .run(function () {
        // placeholder
    });
