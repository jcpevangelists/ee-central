angular.module('tribe-twitter', ['tribe-twitter-service'])
    .controller('TwitterUpdatesController', ['$element', '$scope', 'tribeTwitterService', '$timeout', function ($element, $scope, tribeTwitterService, $timeout) {
        tribeTwitterService.onLoad(function (tweets) {
            $timeout(function () {
                $scope.$apply();
            }, 0);
            var position = 0;
            $scope.tweet = tweets[position];
            $element.find('i.tribe-left').on('click', function () {
                position = position - 1;
                if (position < 0) {
                    position = tweets.length - 1;
                }
                $scope.tweet = tweets[position];
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
            $element.find('i.tribe-right').on('click', function () {
                position = position + 1;
                if (position > tweets.length - 1) {
                    position = 0;
                }
                $scope.tweet = tweets[position];
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
        });
    }]);
