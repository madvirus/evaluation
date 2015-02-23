var mainApp = angular.module('mainApp');

mainApp.controller('mainCtrl',
    ['$scope',
        function ($scope) {
            $scope.selfCollapsed = false;

            $scope.toggleSelfState = function() {
                $scope.selfCollapsed = !$scope.selfCollapsed;
            };
        }
    ]);
