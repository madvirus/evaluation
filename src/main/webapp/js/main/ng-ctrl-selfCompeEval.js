var selfPerfEvalApp = angular.module('mainApp');

selfPerfEvalApp.controller('selfCompeEvalCtrl',
    ['$scope', 'selfEvalService', 'dialogService','competencyItemService',
        function ($scope, selfEvalService, dialogService, competencyItemService) {
            $scope.grades = ['S', 'A', 'B', 'C', 'D'];
            $scope.competencyItems = competencyItemService.getItems();
            $scope.showError = false;

            $scope.init = function () {
            };

            $scope.evalDone = function() {
                return $scope.selfEvalDone; //$scope.evalData != null && $scope.evalData.evalSet.done;
            };

            var makeCommand = function (done) {
                var command = {};
                command.evalSeasonId = $scope.evalSeasonId;
                command.evalSet = {done: done};
                command.evalSet.commonsEvals = $scope.evalSet.commonsEvals;
                if ($scope.hasLeadership) {
                    command.evalSet.leadershipEvals = $scope.evalSet.leadershipEvals
                }
                if ($scope.hasAm) {
                    command.evalSet.amEvals = $scope.evalSet.amEvals;
                }
                return command;
            };
            $scope.saveDraft = function () {
                hideErrorDisplay();
                var confirmDialogInstance = dialogService.confirm("임시 저장", "내용을 임시로 저장하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    var command = makeCommand(false);
                    selfEvalService.updateSelfCompeEval($scope.evalSeasonId, command).then(
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

            var showErrorDisplay = function() {
                $scope.showError = true;
            };
            var hideErrorDisplay = function() {
                $scope.showError = false;
            };

            $scope.done = function() {
                hideErrorDisplay();
                if (!$scope.compEvalForm.$valid) {
                    showErrorDisplay();
                } else {
                    hideErrorDisplay();
                    var confirmDialogInstance = dialogService.confirm(
                        "본인 역량 평가 완료", "본인 역량 평가를 완료하시겠습니까? (완료하면 1차 평가자가 반려할 때 까지 수정할 수 없습니다.)");
                    confirmDialogInstance.result.then(function () {
                        var command = makeCommand(true);
                        selfEvalService.updateSelfCompeEval($scope.evalSeasonId, command).then(
                            function (result) {
                                $scope.selfEvalDone = true;
                                dialogService.success("성공", "본인 역량 평가를 완료했습니다.");
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
