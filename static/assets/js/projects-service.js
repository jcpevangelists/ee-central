angular.module('tribe-projects-service', [])
    .factory('tribeProjectsService', ['$http', function ($http) {
        var getPromise = $http.get('rest/projects')
        return {
            onLoad: function (callback) {
                getPromise.success(function (data) {
                    callback(data.dtoProject);
                });
            }
        };
    }]);