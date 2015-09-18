angular.module('tribe-contributors-service', [])
    .factory('tribeContributorsService', ['$http', function ($http) {
        var getPromise = $http.get('rest/contributors')
        var contributorsList = [];
        var contributorsMap = {};
        return {
            onLoad: function (callback) {
                getPromise.success(function (data) {
                    contributorsList = data.dtoContributor || [];
                    (function () {
                        var newMap = {};
                        _.each(contributorsList, function (item) {
                            newMap[item.name] = item;
                        });
                        contributorsMap = newMap;
                    }());
                    callback({
                        getAll: function () {
                            return contributorsList;
                        },
                        getByName: function (name) {
                            return contributorsMap[name];
                        }
                    });
                });
            }
        };
    }]);