(function () {
    'use strict';

    angular.module('tribe-controllers-home', [
        'tribe-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'app/page-main.html'
            });
        }])
        .controller('HeaderImageController', ['$element', '$window', function ($element, $window) {
            var el = angular.element($window);
            el.scroll(function () {
                var step = el.scrollTop();
                $element.css({
                    'transform': 'translateY(' + (step / 3) + 'px)'
                });
            });
        }])
        .controller('ProjectHighlightController', [
            '$scope', '$sce', 'tribeAppService',
            function ($scope, $sce, tribeAppService) {
                tribeAppService.whenReady(function (data) {
                    var projects = _.values(data.projects);
                    // crest is hardcoded as the only highlighted project for now TODO
                    var project = _.find(projects, function (item) {
                        return item.name === 'crest';
                    });
                    var description = project.longDescription;
                    $scope.highligtedProject = {
                        name: project.name,
                        friendlyName: project.friendlyName,
                        description: $sce.trustAsHtml(description),
                        snapshot: project.snapshot,
                        icon: project.icon
                    };
                });
            }
        ])
        .controller('ProjectsCarousselController', [
            'tribeAppService', '$scope',
            function (tribeAppService, $scope) {
                tribeAppService.whenReady(function (data) {
                    $scope.projects = _.values(data.projects);
                });
            }
        ])
        .directive('tribeProjectsCarousel', [function () {
            return {
                link: function ($scope, $element) {
                    var carousel = $element.find('div.tribe-projects-caroussel > div').first();
                    $element.find('div:first-child').bind('click', function () {
                        var article = $element.find('article:last-child');
                        article.detach();
                        article.prependTo(carousel);
                    });
                    $element.find('div:last-child').bind('click', function () {
                        var article = $element.find('article:first-child');
                        article.detach();
                        article.appendTo(carousel);
                    });
                }
            };
        }])
        .directive('tribeScrollToTop', [function () {
            return {
                link: function ($scope, $element) {
                    $element.bind('click', function () {
                        angular.element('html, body').animate({scrollTop: 0}, "slow");
                    });
                }
            };
        }]);
}());
