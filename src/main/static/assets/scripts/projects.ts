///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-projects', [])

    .factory('eeioProjectsService', [
        '$http',
        function ($http) {
            return {
                getProjects: function () {
                    return $http.get('api/project');
                },
                getProject: function (owner, name) {
                    return $http.get('api/project/' + owner + '/' + name);
                },
                getProjectPage: function (owner, name, resource) {
                    return $http.get('api/project/' + owner + '/' + name + '/' + resource);
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
                owner: '=',
                name: '='
            },
            templateUrl: 'app/templates/dir_projects_project_name_description.html',
            controller: ['$scope', '$timeout', '$sce', 'eeioProjectsService',
                function ($scope, $timeout, $sce, projectsService) {
                    $scope.project = {};
                    projectsService.getProject($scope.owner, $scope.name).then(function (response) {
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
                owner: '=',
                name: '=',
                resource: '='
            },
            templateUrl: 'app/templates/dir_projects_project_doc.html',
            controller: ['$scope', '$timeout', '$sce', '$location', 'eeioProjectsService',
                function ($scope, $timeout, $sce, $location, projectsService) {
                    $scope.project = {};
                    projectsService.getProject($scope.owner, $scope.name).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.detail = response.data;
                            });
                        });
                    });
                    projectsService.getProjectPage($scope.owner, $scope.name, $scope.resource).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                var content = angular.element(response.data.content);
                                content.find('[href]').each(function (index, el) {
                                    var ael = angular.element(el);
                                    var currentHref = ael.attr('href');
                                    if (!currentHref.startsWith('http://') && !currentHref.startsWith('https://')) {
                                        var images = ael.find('> img');
                                        if (images.length) {
                                            var pathRoot = '/project/' + $scope.owner + '/' + $scope.name + '/';
                                            var currentHrefSplit = currentHref.split('/');
                                            var resourceNamePath = $location.url().substring(pathRoot.length).split('/');
                                            resourceNamePath.pop();
                                            var resourceName = resourceNamePath.join('/') + '/' + currentHref;
                                            var href = 'api/project/raw/' + $scope.owner + '/' + $scope.name + '/' + resourceName;
                                            ael.attr('href', href);
                                            images.each(function (indexImg, elImg) {
                                                var aelImg = angular.element(elImg);
                                                aelImg.attr('src', href);
                                            });
                                        } else {
                                            if (!currentHref.startsWith('#')) {
                                                var href = 'project/' + $scope.owner + '/' + $scope.name + '/' + currentHref;
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

    .run(function () {
        // placeholder
    });
