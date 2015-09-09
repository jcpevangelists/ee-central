angular.module('tribe-projects-carousel', [])
    .controller('ProjectsCarousselController', ['$element', '$location', '$http', '$scope', function ($element, $location, $http, $scope) {
        $http.get('rest/projects').success(function (data) {
            $scope.projects = data.dtoProject;
        });
        var carousel = $element.find('div.tribe-projects-caroussel').first();
        $element.find('i.tribe-left').on('click', function () {
            var article = $element.find('article:first-child');
            article.addClass('slide-in');
            $scope.$apply();
            article.detach();
            article.removeClass('slide-in');
            $scope.$apply();
            article.appendTo(carousel);
            $scope.$apply();
        });
        $element.find('i.tribe-right').on('click', function () {
            var article = $element.find('article:last-child');
            article.addClass('slide-out');
            $scope.$apply();
            article.detach();
            article.removeClass('slide-out');
            $scope.$apply();
            article.prependTo(carousel);
            $scope.$apply();
        });
    }]);
