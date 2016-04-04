///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-projects', [])

    .factory('eeioProjectsService', [
        '$http',
        function ($http) {
            return {
                getProjects: function () {
                    return $http.get('api/project');
                },
                getProject: function (configFile) {
                    if (!configFile) {
                        return {
                            then: function () {
                            }
                        };
                    } else {
                        return $http.get('api/project/' + configFile);
                    }
                },
                getProjectPage: function (configFile, resource) {
                    if (resource === null || resource === undefined) {
                        return $http.get('api/project/page/' + configFile + '/');
                    } else {
                        return $http.get('api/project/page/' + configFile + '/' + resource);
                    }
                }
            };
        }
    ])

    .directive('eeioProjectsShortlist', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_projects_projects_shortlist.html',
            controller: ['$scope', '$timeout', 'eeioProjectsService', function ($scope, $timeout, projectsService) {
                projectsService.getProjects().then(function (response) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.projects = response.data;
                        });
                    });
                });
            }]
        };
    }])

    .directive('eeioProjectNameDescription', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '='
            },
            templateUrl: 'app/templates/dir_projects_project_name_description.html',
            controller: ['$scope', '$timeout', '$sce', 'eeioProjectsService',
                function ($scope, $timeout, $sce, projectsService) {
                    $scope.project = {};
                    projectsService.getProject($scope.configFile).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.detail = response.data;
                            });
                        });
                    });
                }
            ]
        };
    }])

    .directive('eeioProjectDoc', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '=',
                resource: '='
            },
            templateUrl: 'app/templates/dir_projects_project_doc.html',
            controller: ['$scope', '$timeout', '$sce', '$location', 'eeioProjectsService',
                function ($scope, $timeout, $sce, $location, projectsService) {
                    $scope.project = {};
                    projectsService.getProject($scope.configFile).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.detail = response.data;
                            });
                        });
                    });
                    projectsService.getProjectPage($scope.configFile, $scope.resource).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                var content = angular.element(response.data.content);
                                content.find('[href]').each(function (index, el) {
                                    var ael = angular.element(el);
                                    var currentHref = ael.attr('href');
                                    if (!currentHref.startsWith('http://') && !currentHref.startsWith('https://')) {
                                        var images = ael.find('> img');
                                        if (images.length) {
                                            var pathRoot = '/project/' + $scope.configFile + '/';
                                            var currentHrefSplit = currentHref.split('/');
                                            var resourceNamePath = $location.url().substring(pathRoot.length).split('/');
                                            resourceNamePath.pop();
                                            var resourceName = resourceNamePath.join('/') + '/' + currentHref;
                                            var href = 'api/project/raw/' + $scope.configFile + '/' + resourceName;
                                            ael.attr('href', href);
                                            images.each(function (indexImg, elImg) {
                                                var aelImg = angular.element(elImg);
                                                aelImg.attr('src', href);
                                            });
                                        } else {
                                            if (!currentHref.startsWith('#')) {
                                                var href = 'project/' + $scope.configFile + '/' + currentHref;
                                                ael.attr('href', href);
                                            }
                                        }
                                    }
                                });
                                $scope.project.doc = $sce.trustAsHtml(content.html());
                            });
                        });
                    });
                }
            ]
        };
    }])

    .directive('eeioProjectDocRelated', [function () {
        return {
            restrict: 'E',
            scope: {
                configFile: '='
            },
            templateUrl: 'app/templates/dir_projects_project_doc_related.html',
            controller: ['$scope', '$timeout', 'eeioProjectsService',
                function ($scope, $timeout, projectsService) {
                    $scope.related = {};
                    projectsService.getProject($scope.configFile).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.related = response.data;
                            });
                        });
                    });
                }
            ]
        };
    }])

    .run(function () {
        // placeholder
    });
