angular.module('tribe-projects-service', [])
    .factory('tribeProjectsService', ['$http', function ($http) {
        var getPromise = $http.get('rest/projects')
        var projectsList = [];
        var projectsMap = {};
        return {
            onLoad: function (callback) {
                getPromise.success(function (data) {
                    projectsList = data.dtoProject || [];
                    (function () {
                        var newMap = {};
                        _.each(projectsList, function (item) {
                            newMap[item.name] = item;
                        });
                        projectsMap = newMap;
                    }());
                    callback({
                        getAll: function () {
                            return projectsList;
                        },
                        getByName: function (name) {
                            return projectsMap[name];
                        }
                    });
                });
            }
        };
    }]);