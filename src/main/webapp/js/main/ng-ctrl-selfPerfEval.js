var selfPerfEvalApp = angular.module('mainApp');

selfPerfEvalApp.controller('selfPerfEvalCtrl',
    ['$scope', 'selfEvalService', 'dialogService',
        function ($scope, selfEvalService, dialogService) {
            $scope.grades = ['S', 'A', 'B', 'C', 'D'];
            $scope.itemSelected = [];
            $scope.showError = false;

            $scope.init = function () {
                initItemSelected();
            };

            var initItemSelected = function() {
                $scope.itemSelected = [];
                angular.forEach($scope.itemAndEvals, function(item, idx) {
                    $scope.itemSelected.push(false);
                });
            };

            $scope.evalDone = function() {
                return $scope.selfEvalDone;
            };

            $scope.addRow = function () {
                hideErrorDisplay();
                var row = {
                    item: {category: '', goalType: '', result: '', weight: 0},
                    eval: {comment: '', grade: 'A'}
                };
                $scope.itemAndEvals.push(row);
                $scope.itemSelected.push(false);
            };

            $scope.removeRow = function() {
                hideErrorDisplay();
                var confirmDialogInstance = dialogService.confirm("확인", "선택한 항목을 삭제하시겠습니까?");
                confirmDialogInstance.result.then(function() {
                    var tempItemAndEvals = $scope.itemAndEvals;
                    for (var i = $scope.itemSelected.length - 1 ; i >= 0 ; i--) {
                        tempItemAndEvals.splice(i, $scope.itemSelected[i]);
                    }
                    $scope.itemAndEvals = tempItemAndEvals;
                    initItemSelected();
                }, function() {});
            };

            var makeCommand = function (done) {
                var command = {};
                command.evalSeasonId = $scope.evalSeasonId;
                command.itemAndEvals = $scope.itemAndEvals;
                command.done = done;
                return command;
            };
            $scope.saveDraft = function () {
                hideErrorDisplay();
                var confirmDialogInstance = dialogService.confirm("임시 저장", "내용을 임시로 저장하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    var command = makeCommand(false);

                    selfEvalService.updateSelfPerfEval($scope.evalSeasonId, command).then(
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

            $scope.weightSum = function () {
                var sum = 0;
                angular.forEach($scope.itemAndEvals,
                    function (itemAndEval, idx) {
                        sum += itemAndEval.item.weight;
                    }
                );
                return sum;
            };
            var zeroWeightExists = function () {
                for (var i = 0 ; i < $scope.itemAndEvals.length ; i++) {
                    if ($scope.itemAndEvals[i].item.weight <= 0) {
                        return true;
                    }
                }
                return false;
            };

            var showErrorDisplay = function() {
                $scope.showError = true;
            };
            var hideErrorDisplay = function() {
                $scope.showError = false;
            };

            $scope.done = function() {
                hideErrorDisplay();
                $scope.sumError = $scope.weightSum() != 100;
                $scope.zeroWeightError = zeroWeightExists();
                if (!$scope.perfEvalForm.$valid || $scope.sumError || $scope.zeroWeightError) {
                    showErrorDisplay();
                } else {
                    hideErrorDisplay();
                    var confirmDialogInstance = dialogService.confirm(
                        "본인 성과 평가 완료", "본인 성과 평가를 완료하시겠습니까? (완료하면 1차 평가자가 반려할 때 까지 수정할 수 없습니다.)");
                    confirmDialogInstance.result.then(function () {
                        var command = makeCommand(true);
                        selfEvalService.updateSelfPerfEval($scope.evalSeasonId, command).then(
                            function (result) {
                                $scope.selfEvalDone = true;
                                dialogService.success("성공", "본인 성과 평가를 완료했습니다.");
                            },
                            function (result) {
                                // TODO 에러 메시지 내용을 보여 주어야 함
                                dialogService.error("실패", "문제가 발생했습니다.");
                            }
                        );
                    });

                }
            };
        }
    ]);
