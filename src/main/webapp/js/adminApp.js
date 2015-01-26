var evalAdminApp = angular.module('evalAdminApp',
    ['ngRoute', 'ui.bootstrap', 'adminEvalSeasonController', 'evalService']);

evalAdminApp.config(
    ['$routeProvider', '$httpProvider',
        function($routeProvider, $httpProvider) {
            $routeProvider.
                when('/', {
                    templateUrl: '/template/admin/adminHome.jsp'
                }).
                when('/admin/evalseasons', {
                    templateUrl: '/template/admin/evalseason/evalseasons.jsp',
                    controller: 'adminEvalSeasonListCtrl'
                }).
                when('/admin/evalseasons/:evalSeasonId', {
                    templateUrl: '/template/admin/evalseason/evalseasonDetail.jsp',
                    controller: 'adminEvalSeasonDetailCtrl'
                }).
                otherwise({
                    redirectTo: '/'
                });
        }
    ]);


evalAdminApp.factory('dialogService', ['$modal', function($modal) {
    return {
        confirm: function (title, message) {
            var confirmDialogInstance = $modal.open({
                templateUrl: 'confirmDialog.html',
                controller: 'confirmDialogCtrl',
                size: 'sm',
                resolve: {
                    title: function() { return title; },
                    message: function() { return message; }
                }
            });
            return confirmDialogInstance;
        },
        alert: function (title, message) {
            var alertDialogInstance = $modal.open({
                templateUrl: 'alertDialog.html',
                controller: 'alertDialogCtrl',
                size: 'sm',
                resolve: {
                    title: function() { return title; },
                    message: function() { return message; }
                }
            });
            return alertDialogInstance;
        }
    };
}]);

evalAdminApp.controller('confirmDialogCtrl',
    ['$scope', '$modalInstance', 'title', 'message',
        function ($scope, $modalInstance, title, message) {
            $scope.title = title;
            $scope.message = message;

            $scope.yes = function () {
                $modalInstance.close();
            };
            $scope.no = function () {
                $modalInstance.dismiss();
            };
        }]);

evalAdminApp.controller('alertDialogCtrl',
    ['$scope', '$modalInstance', 'title', 'message',
        function ($scope, $modalInstance, title, message) {
            $scope.title = title;
            $scope.message = message;

            $scope.confirm = function () {
                $modalInstance.close();
            };
        }]);