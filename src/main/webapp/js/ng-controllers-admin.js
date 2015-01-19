var adminController = angular.module('adminController', ['evalService', 'ui.bootstrap']);

adminController.controller('adminEvalSeasonListCtrl',
    ['$scope', '$modal', 'evalSeasonService',
        function ($scope, $modal, evalSeasonService) {
            $scope.evalSeasons = [];
            var load = function () {
                evalSeasonService.getEvalSeasons()
                    .then(function (result) {
                        $scope.evalSeasons = result.data;
                    });
            };
            load();

            $scope.openRegDialog = function () {
                var newEvalSeasonDialogInstance = $modal.open({
                    templateUrl: 'newEvalSeasonDialog.html',
                    controller: 'newEvalSeasonDialogCtrl'
                });

                newEvalSeasonDialogInstance.result.then(function () {
                    load()
                }, function () {
                });
            };
        }
    ]);


adminController.controller('newEvalSeasonDialogCtrl',
    ['$scope', '$modalInstance', 'evalSeasonService',
        function ($scope, $modalInstance, evalSeasonService) {
            $scope.evalSeasonId = "";
            $scope.evalSeasonName = "";
            $scope.duplicateId = "";

            $scope.ok = function () {
                evalSeasonService.createNewSeason({
                    evalSeasonId: $scope.evalSeasonId,
                    name: $scope.evalSeasonName
                }).then(function (result) {
                    $modalInstance.close();
                }, function(result) {
                    if (result.status == 409) {
                        $scope.duplicateId = $scope.evalSeasonId;
                    }
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }]);