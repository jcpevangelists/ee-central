///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-header', [])

    .directive('eeioHeader', ['$window', function ($window) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                login: '='
            },
            templateUrl: 'app/templates/dir_header.html',
            link: function (scope, el) {
                var transcludeEl = el.find('div[ng-transclude]');
                if (transcludeEl.html() === '') {
                    el.find('.title').remove();
                }
                var page = angular.element($window);
                var background = el.find('.background');
                var header = el.find('.head');
                var headerSpacing = el.find('.head-spacing');
                page.scroll(function () {
                    var step = page.scrollTop();
                    background.css({
                        'transform': 'translateY(' + (step / 3) + 'px)'
                    });
                    // header out
                    var heightValue = header.outerHeight();
                    if (heightValue < step) {
                        if (!header.hasClass('floating')) {
                            headerSpacing.css('height', heightValue + 'px');
                            header.addClass('floating');
                        }
                    } else {
                        headerSpacing.css('height', '');
                        header.removeClass('floating');
                    }
                });
            }
        };
    }])

    .run(function () {
        // placeholder
    });
