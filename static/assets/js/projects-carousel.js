angular.module('tribe-projects-carousel', ['tribe-projects-service'])
    .controller('ProjectsCarousselController', ['$element', 'tribeProjectsService', '$scope', '$timeout', function ($element, tribeProjectsService, $scope, $timeout) {
        tribeProjectsService.onLoad(function (projects) {
            $scope.projects = projects.getAll();
            $timeout(function () {
                $scope.$apply();
            }, 0);
        });
        var carousel = $element.find('div.tribe-projects-caroussel').first();
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
