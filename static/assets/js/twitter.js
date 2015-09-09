angular.module('tribe-twitter', [])
    .controller('TwitterUpdatesController', ['$element', '$scope', '$http', function ($element, $scope, $http) {
        $http.get('rest/twitter').success(function (data) {
            $scope.tweets = data.dtoTweet;
            var position = 0;
            $scope.tweet = $scope.tweets[position];
            $element.find('i.tribe-left').on('click', function () {
                position = position - 1;
                if (position < 0) {
                    position = data.dtoTweet.length - 1;
                }
                $scope.tweet = $scope.tweets[position];
                $scope.$apply();
            });
            $element.find('i.tribe-right').on('click', function () {
                position = position + 1;
                if (position > data.dtoTweet.length - 1) {
                    position = 0;
                }
                $scope.tweet = $scope.tweets[position];
                $scope.$apply();
            });
        });
    }]);
