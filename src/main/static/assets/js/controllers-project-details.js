(function () {
    'use strict';

    angular.module('tribe-project-details', ['tribe-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/projects/:project', {
                templateUrl: 'app/page-project-details.html'
            });
        }])
        .controller('ProjectDetailsController', [
            '$scope', '$routeParams', '$sce', '$timeout', 'tribeAppService',
            function ($scope, $routeParams, $sce, $timeout, tribeAppService) {
                $scope.openPopup = function (url) {
                    window.open(url, 'name', 'width=600,height=400');
                };
                tribeAppService.whenReady(function (data) {
                    var project = data.projects[$routeParams.project];
                    $scope.baseFullPath = angular.element('head base').first().attr('href');
                    $scope.project = _.clone(project);
                    $scope.project.documentation = $sce.trustAsHtml(project.documentation);
                    $scope.otherProjects = _.filter(_.values(data.projects), function (item) {
                        return item.name !== project.name;
                    });
                });
            }
        ])
        .directive('prism', ['$timeout', function ($timeout) {
            return {
                restrict: 'A',
                link: function ($scope, $element, $attrs) {
                    $scope.$watch(
                        function () {
                            return $scope.$eval($attrs.ngBindHtml);
                        },
                        function () {
                            $timeout(function () {
                                $element.find('pre.highlight').each(function () {
                                    var preEl = $(this);
                                    preEl.find('code').addClass('hljs');
                                    var codeEl = preEl.find('code.language-java, code.language-xml');
                                    $timeout(function () {
                                        $scope.$apply();
                                    }, 0);
                                    if (codeEl.length) {
                                        codeEl.each(function () {
                                            hljs.highlightBlock(this);
                                        });
                                    }
                                    $timeout(function () {
                                        $scope.$apply();
                                    }, 0);
                                });
                            });
                        }
                    );
                }
            };
        }]);
}());
