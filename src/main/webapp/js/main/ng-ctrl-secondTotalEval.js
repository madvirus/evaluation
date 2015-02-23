var firstTotalEvalApp = angular.module('mainApp');

firstTotalEvalApp.controller('secondTotalEvalCtrl',
    ['$scope', 'secondEvalService', 'dialogService',
        function ($scope, secondEvalService, dialogService) {
            $scope.showError = false;

            $scope.init = function(size) {
                $scope.evalData = {};
                $scope.evalData.totalEvals = [];
                for (var i = 0 ; i < size ; i++) {
                    $scope.evalData.totalEvals.push({rateeId: '', command: '', grade: ''});
                }
            };

            $scope.readonly = function() {
                return !$scope.totalEvalAvailable || $scope.totalEvalDone;
            };

            var makeCommand = function(done) {
                var command = {};
                command.done = done;
                command.evalUpdates = [];
                angular.forEach($scope.evalData.totalEvals, function(val, idx) {
                    command.evalUpdates.push({
                        rateeId: val.rateeId,
                        comment: val.comment,
                        grade: val.grade
                    });
                });
                return command;
            };

            $scope.saveDraft = function() {
                hideErrorDisplay();
                var confirmDialogInstance = dialogService.confirm("임시 저장", "내용을 임시로 저장하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    var command = makeCommand(false);
                    secondEvalService.updateSecondTotalEval($scope.evalSeasonId, command).then(
                        function (result) {
                            dialogService.success("성공", "평가 내용을 임시로 저장했습니다.");
                        }
                    );
                });
            };

            $scope.saveDone = function() {
                hideErrorDisplay();
                if (!$scope.totalEvalForm.$valid) {
                    showErrorDisplay();
                } else {
                    hideErrorDisplay();
                    var confirmDialogInstance = dialogService.confirm(
                        "2차 종합 평가 완료", "2차 종합 평가를 완료하시겠습니까?");
                    confirmDialogInstance.result.then(function () {
                        var command = makeCommand(true);
                        secondEvalService.updateSecondTotalEval($scope.evalSeasonId, command).then(
                            function (result) {
                                $scope.totalEvalDone = true;
                                dialogService.success("성공", "2차 평가를 완료했습니다.");
                            }
                        );
                    });

                }
            };

            var showErrorDisplay = function() {
                $scope.showError = true;
            };
            var hideErrorDisplay = function() {
                $scope.showError = false;
            };

        }
    ]);

