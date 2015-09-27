angular.module('tribe-projects-carousel', ['tribe-app-service'])
    .controller('ProjectsCarousselController',
    ['$element', 'tribeAppService', '$scope', '$timeout',
        function ($element, tribeAppService, $scope, $timeout) {
            tribeAppService.whenReady(function (data) {
                $scope.projects = _.values(data.projects);
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            });
            var carousel = $element.find('div.tribe-projects-caroussel > div').first();
            $scope.onClick = function (dir) {
                var article;
                if ('left' === dir) {
                    article = $element.find('article:last-child');
                    article.detach();
                    article.prependTo(carousel);
                } else {
                    article = $element.find('article:first-child');
                    article.detach();
                    article.appendTo(carousel);
                }
                $timeout(function () {
                    $scope.$apply();
                }, 0);
            };
        }]);
