var dialogModule = angular.module('dialogModule', ['ui.bootstrap']);

dialogModule.factory('dialogService', ['$modal', function($modal) {
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
                    message: function() { return message; },
                    alertType: function() { return 'info'}
                }
            });
            return alertDialogInstance;
        },
        success: function (title, message) {
            var alertDialogInstance = $modal.open({
                templateUrl: 'alertDialog.html',
                controller: 'alertDialogCtrl',
                size: 'sm',
                resolve: {
                    title: function() { return title; },
                    message: function() { return message; },
                    alertType: function() { return 'success'}
                }
            });
            return alertDialogInstance;
        },
        error: function (title, message) {
            var alertDialogInstance = $modal.open({
                templateUrl: 'alertDialog.html',
                controller: 'alertDialogCtrl',
                size: 'sm',
                resolve: {
                    title: function() { return title; },
                    message: function() { return message; },
                    alertType: function() { return 'error'}
                }
            });
            return alertDialogInstance;
        },
        warning: function (title, message) {
            var alertDialogInstance = $modal.open({
                templateUrl: 'alertDialog.html',
                controller: 'alertDialogCtrl',
                size: 'sm',
                resolve: {
                    title: function() { return title; },
                    message: function() { return message; },
                    alertType: function() { return 'warning'}
                }
            });
            return alertDialogInstance;
        }
    };
}]);

dialogModule.controller('confirmDialogCtrl',
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

dialogModule.controller('alertDialogCtrl',
    ['$scope', '$modalInstance', 'title', 'message', 'alertType',
        function ($scope, $modalInstance, title, message, alertType) {
            $scope.title = title;
            $scope.message = message;
            $scope.alertType = alertType;
            $scope.confirm = function () {
                $modalInstance.close();
            };
        }]);