angular.module('tribe-project-details', [])
    .controller('ProjectDetailsController', ['$scope', '$routeParams', function ($scope, $routeParams) {
        $scope.projectName = $routeParams.project;
    }]);
