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

    .directive('eeioProjectHighlight', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_projects_project_highlight.html'
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
                    projectsService.getProjectPage($scope.owner, $scope.name, $scope.resource).then(function (response) {
                        $timeout(function () {
                            $scope.$apply(function () {
                                $scope.project.doc = $sce.trustAsHtml(response.data.content);
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
