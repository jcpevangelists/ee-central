angular.module('tribe-project-details', [])
    .controller('ProjectDetailsController', [
        '$scope', '$routeParams', '$sce', '$timeout', 'tribeProjectsService',
        function ($scope, $routeParams, $sce, $timeout, tribeProjectsService) {
            tribeProjectsService.onLoad(function (projects) {
                var project = projects.getByName($routeParams.project);
                $scope.baseFullPath = $('head base').first().attr('href');
                $scope.project = {
                    name: project.name,
                    friendlyName: project.friendlyName,
                    shortDescription: project.shortDescription,
                    documentation: $sce.trustAsHtml(project.documentation),
                    icon: project.icon,
                    contributors: _.isArray(project.contributors) ? project.contributors : [project.contributors],
                    tags: _.isArray(project.tags) ? project.tags : [project.tags]
                };
                $scope.otherProjects = _.filter(projects.getAll(), function (item) {
                    return item.name !== project.name;
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
