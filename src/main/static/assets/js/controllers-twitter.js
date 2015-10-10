(function () {
    'use strict';

    angular.module('tribe-twitter', ['tribe-app-service'])
        .controller('TwitterUpdatesController', ['$scope', 'tribeAppService', '$timeout',
            function ($scope, tribeAppService, $timeout) {
                tribeAppService.whenReady(function (data) {
                    var tweets = data.tweets;
                    $timeout(function () {
                        $scope.$apply();
                    }, 0);
                    var position = 0;
                    $scope.tweet = tweets[position];
                    $scope.onClick = function (dir) {
                        if ('left' === dir) {
                            position = position - 1;
                            if (position < 0) {
                                position = tweets.length - 1;
                            }
                            $scope.tweet = tweets[position];
                        } else {
                            position = position + 1;
                            if (position > tweets.length - 1) {
                                position = 0;
                            }
                            $scope.tweet = tweets[position];
                        }
                        $timeout(function () {
                            $scope.$apply();
                        }, 0);
                    };

                });
            }]);
}());
