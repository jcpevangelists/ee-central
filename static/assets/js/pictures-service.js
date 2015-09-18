angular.module('tribe-pictures-service', [])
    .factory('tribePicturesService', ['$http', function ($http) {
        var getPromise = $http.get('rest/pictures')
        var picturesList = [];
        return {
            onLoad: function (callback) {
                getPromise.success(function (data) {
                    picturesList = _.map((data.dtoPicture || []), function (pic) {
                        return pic.name;
                    });
                    callback({
                        getAll: function () {
                            return picturesList;
                        }
                    });
                });
            }
        };
    }]);