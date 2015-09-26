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
            $element.find('i.tribe-left').on('click', function () {
                var article = $element.find('article:first-child');
                $scope.$apply();
                article.detach();
                $scope.$apply();
                article.appendTo(carousel);
                $scope.$apply();
            });
            $element.find('i.tribe-right').on('click', function () {
                var article = $element.find('article:last-child');
                $scope.$apply();
                article.detach();
                $scope.$apply();
                article.prependTo(carousel);
                $scope.$apply();
            });
        }]);
