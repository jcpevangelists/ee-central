///<reference path="../../bower_components/DefinitelyTyped/angularjs/angular.d.ts"/>

angular.module('javaeeio-menu', [])

    .factory('eeioMenuService', [
        function () {
            var selectedMenu = null;
            return {
                setSelected: function (value) {
                    selectedMenu = value;
                },
                getSelected: function () {
                    return selectedMenu;
                }
            };
        }
    ])

    .directive('eeioMenu', [function () {
        return {
            restrict: 'E',
            scope: {},
            templateUrl: 'app/templates/dir_menu.html',
            controller: ['$scope', '$timeout', 'eeioMenuService', function ($scope, $timeout, srv) {
                $scope.setSelected = function (value) {
                    $timeout(function () {
                        $scope.$apply(function () {
                            $scope.selectedItem = value;
                            srv.setSelected(value);
                        });
                    });
                };
                $scope.setSelected(srv.getSelected());
            }]
        };
    }])

    .run(function () {
        // placeholder
    });
