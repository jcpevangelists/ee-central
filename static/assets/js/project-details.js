angular.module('tribe-project-details', ['tribe-app-service'])
    .controller('ProjectDetailsController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeAppService',
        function ($scope, $routeParams, $sce, $timeout, tribeAppService) {
            $scope.openPopup = function (url) {
                window.open(url, 'name', 'width=600,height=400');
            };
            tribeAppService.whenReady(function (data) {
                var project = data.projects[$routeParams.project];
                $scope.baseFullPath = $('head base').first().attr('href');
                $scope.project = _.clone(project);
                $scope.project.documentation = $sce.trustAsHtml(project.documentation);
                $scope.otherProjects = _.filter(_.keys(projects), function (item) {
                    return item !== project.name;
                });
                $timeout(function () {
                    $scope.$apply();
                }, 0);
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
                    function (value) {
                        $timeout(function () {
                            $element.find('pre.highlight').each(function () {
                                var preEl = $(this);
                                var codeEl = preEl.find('code.language-java');
                                if (codeEl.length) {
                                    codeEl.each(function () {
                                        hljs.highlightBlock(this);
                                    });
                                }
                            });
                        });
                    }
                );
            }
        }
    }]);
