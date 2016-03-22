(function () {
    'use strict';

    angular.module('javaee-project-details', ['javaee-app-service', 'ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/projects/:project', {
                templateUrl: 'app/page-project-details.html'
            });
        }])
        .controller('ProjectDetailsController', [
            '$scope', '$routeParams', '$sce', '$templateRequest', 'javaeeAppService', '$anchorScroll', '$timeout', '$location',
            function ($scope, $routeParams, $sce, $templateRequest, javaeeAppService, $anchorScroll, $timeout, $location) {

                function prepareDocumentation(originalDocumentation) {
                    var originalEl = angular.element(originalDocumentation);
                    var toc = originalEl.find('#user-content-toc');
                    toc.detach();
                    $scope.project.documentation = $sce.trustAsHtml(originalEl.html());
                    $scope.project.toc = $sce.trustAsHtml(toc.html());
                    $timeout(function () {
                        var hashValue = $location.hash();
                        if (!hashValue.startsWith('user-content-')) {
                            hashValue = 'user-content-' + hashValue;
                        }
                        $anchorScroll(hashValue);
                    });
                    // uncomment this when implementing https://github.com/jcpevangelists/javaee.io/issues/72
                    //$templateRequest('app/section-share-with-friends-documentation.html').then(function (template) {
                    //    var originalChildren = originalEl.find('> article > *');
                    //    var shareEl = angular.element(template);
                    //    var middleEl = angular.element(originalChildren[Math.floor(originalChildren.length / 2)]);
                    //    middleEl.after(shareEl);
                    //    $scope.project.documentation = $sce.trustAsHtml(originalEl.html());
                    //});
                }

                javaeeAppService.whenReady(function (data) {
                    var project = data.projects[$routeParams.project];
                    $scope.project = _.clone(project);
                    prepareDocumentation(project.documentation);
                    $scope.otherProjects = _.filter(_.values(data.projects), function (item) {
                        return item.name !== project.name;
                    });
                });
            }
        ]);
}());
