(function () {
    'use strict';

    angular.module('tribe-app-directives', [])
        .directive('tribeShareProject', ['$window', function ($window) {
            return {
                restrict: 'A',
                link: function ($scope, $element, $attrs) {
                    $element.bind('click', function () {
                        var url;
                        if ('twitter' === $attrs.tribeShareProject) {
                            url = 'https://twitter.com/intent/tweet?text=Check out http:'
                                + $scope.baseFullPath + 'projects/' + $scope.project.name;
                        } else if ('facebook' === $attrs.tribeShareProject) {
                            url = 'http://www.facebook.com/sharer/sharer.php?u=http:'
                                + $scope.baseFullPath + 'projects/' + $scope.project.name;
                        }
                        if (url) {
                            $window.open(url, 'name', 'width=600,height=400');
                        }
                    });
                }
            };
        }]);
}());
