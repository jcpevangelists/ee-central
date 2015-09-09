angular.module('tribe-twitter-service', [])
    .factory('tribeTwitterService', ['$http', function ($http) {
        var getPromise = $http.get('rest/twitter')
        return {
            onLoad: function (callback) {
                getPromise.success(function (data) {
                    callback(data.dtoTweet);
                });
            }
        };
    }]);
