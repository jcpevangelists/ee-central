(function () {
    'use strict';

    angular.module('tribe-project-details', ['tribe-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/projects/:project', {
                templateUrl: 'app/page-project-details.html'
            });
        }])
        .controller('ProjectDetailsController', [
            '$scope', '$routeParams', '$sce', '$templateRequest', 'tribeAppService',
            function ($scope, $routeParams, $sce, $templateRequest, tribeAppService) {

                /*
                function insertShareLinks(originalDocumentation) {
                    $scope.project.documentation = $sce.trustAsHtml('');
                    $templateRequest('app/section-share-with-friends-documentation.html').then(function (template) {
                        var originalEl = angular.element(originalDocumentation);
                        var originalChildren = originalEl.find('> article > *');
                        var shareEl = angular.element(template);
                        var middleEl = angular.element(originalChildren[Math.floor(originalChildren.length / 2)]);
                        middleEl.after(shareEl);
                        $scope.project.documentation = $sce.trustAsHtml(originalEl.html());
                    });
                }
                */

                tribeAppService.whenReady(function (data) {
                    var project = data.projects[$routeParams.project];
                    $scope.project = _.clone(project);

                    // uncomment this when implementing https://github.com/tomitribe/tomitribe.io/issues/72
                    // insertShareLinks(project.documentation);
                    // once the line above is uncommented, remove the line below
                    $scope.project.documentation = $sce.trustAsHtml(project.documentation);

                    $scope.otherProjects = _.filter(_.values(data.projects), function (item) {
                        return item.name !== project.name;
                    });
                });
            }
        ]);
}());
