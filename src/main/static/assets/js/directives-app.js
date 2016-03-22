(function () {
    'use strict';

    angular.module('javaee-app-directives', [])
        .directive('javaeeShareProject', ['$window', function ($window) {
            return {
                restrict: 'A',
                link: function ($scope, $element, $attrs) {
                    $element.bind('click', function () {
                        var url;
                        if ('twitter' === $attrs.javaeeShareProject) {
                            url = 'https://twitter.com/intent/tweet?text=Check out http:'
                                + $scope.baseFullPath + 'projects/' + $scope.project.name;
                        } else if ('facebook' === $attrs.javaeeShareProject) {
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
