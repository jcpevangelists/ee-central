(function () {
    'use strict';

    angular.module('tribe-app-service', [])
        .factory('tribeAppService', ['$http', function ($http) {
            var getPromise = $http.get('rest/application');
            var result = null;

            function buildMap(list, key) {
                var map = {};
                _.each(list, function (item) {
                    map[item[key]] = item;
                });
                return map;
            }

            function normalizeArray(bean) {
                if (bean && !_.isArray(bean)) {
                    return [bean];
                }
                return bean;
            }

            return {
                loadLongDocumentation: function (projectName, callback) {
                    $http.get('rest/project/' + projectName + '/long_documentation').success(function (data) {
                        callback(data);
                    });
                },
                whenReady: function (callback) {
                    getPromise.success(function (data) {
                        if (!result) {
                            var contributors = normalizeArray(data.dtoPage.contributors);
                            _.each(contributors, function (contributor) {
                                contributor.contributions = normalizeArray(contributor.contributions);
                            });
                            contributors = buildMap(contributors, 'login');
                            var projects = normalizeArray(data.dtoPage.projects);
                            _.each(projects, function (project) {
                                project.contributors = _.map(normalizeArray(project.contributors), function (contributor) {
                                    return contributors[contributor.login];
                                });
                                project.tags = normalizeArray(project.tags);
                            });
                            projects = buildMap(projects, 'name');
                            _.each(_.values(contributors), function (contributor) {
                                var commits = 0;
                                _.each(contributor.contributions, function (contribution) {
                                    contribution.project = projects[contribution.project];
                                    commits = commits + contribution.contributions;
                                });
                                contributor.commits = commits;
                            });
                            var pictures = normalizeArray(data.dtoPage.pictures);
                            var tweets = normalizeArray(data.dtoPage.tweets);
                            result = {
                                contributors: contributors,
                                projects: projects,
                                pictures: pictures,
                                tweets: tweets
                            };
                        }
                        callback(result);
                    });
                }
            };
        }]);
}());
