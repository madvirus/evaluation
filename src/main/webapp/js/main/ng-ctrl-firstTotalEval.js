var firstTotalEvalApp = angular.module('mainApp');

firstTotalEvalApp.controller('firstTotalEvalCtrl',
    ['$scope', 'firstEvalService', 'dialogService',
        function ($scope, firstEvalService, dialogService) {
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
                    firstEvalService.updateFirstTotalEval($scope.evalSeasonId, command).then(
                        function (result) {
                            dialogService.success("성공", "평가 내용을 임시로 저장했습니다.");
                        },
                        function (result) {
                            // TODO 에러 메시지 내용을 보여 주어야 함
                            dialogService.error("실패", "문제가 발생했습니다.");
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
                        "1차 종합 평가 완료", "1차 종합 평가를 완료하시겠습니까?");
                    confirmDialogInstance.result.then(function () {
                        var command = makeCommand(true);
                        firstEvalService.updateFirstTotalEval($scope.evalSeasonId, command).then(
                            function (result) {
                                $scope.totalEvalDone = true;
                                dialogService.success("성공", "1차 평가를 완료했습니다.");
                            },
                            function (result) {
                                // TODO 에러 메시지 내용을 보여 주어야 함
                                dialogService.error("실패", "문제가 발생했습니다.");
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

