var colleagueCompeEvalApp = angular.module('mainApp');

colleagueCompeEvalApp.controller('colleagueCompeEvalCtrl',
    ['$scope', 'colleagueEvalService', 'dialogService','competencyItemService',
        function ($scope, colleagueEvalService, dialogService, competencyItemService) {
            $scope.grades = ['S', 'A', 'B', 'C', 'D'];
            $scope.competencyItems = competencyItemService.getItems();
            $scope.showError = false;

            $scope.evalDone = function() {
                return $scope.colleagueEvalDone;
            };

            var makeCommand = function (done) {
                var command = {};
                command.evalSeasonId = $scope.evalSeasonId;
                command.evalSet = {done: done};
                command.evalSet.commonsEvals = $scope.evalSet.commonsEvals;
                if ($scope.hasLeadership) {
                    command.evalSet.leadershipEvals = $scope.evalSet.leadershipEvals;
                }
                if ($scope.hasAm) {
                    command.evalSet.amEvals = $scope.evalSet.amEvals;
                }
                command.evalSet.totalEval = {
                    comment: $scope.evalSet.totalEval.comment
                };
                return command;
            };
            $scope.saveDraft = function () {
                hideErrorDisplay();
                var confirmDialogInstance = dialogService.confirm("임시 저장", "내용을 임시로 저장하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    var command = makeCommand(false);
                    colleagueEvalService.updateColleagueCompeEval($scope.evalSeasonId, $scope.rateeId, command).then(
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

            $scope.saveDone = function() {
                hideErrorDisplay();
                if (!$scope.compeEvalForm.$valid) {
                    showErrorDisplay();
                } else {
                    hideErrorDisplay();
                    var confirmDialogInstance = dialogService.confirm(
                        "동료 역량 평가 완료", "동료에 대한 역량 평가를 완료하시겠습니까?");
                    confirmDialogInstance.result.then(function () {
                        var command = makeCommand(true);
                        colleagueEvalService.updateColleagueCompeEval($scope.evalSeasonId, $scope.rateeId, command).then(
                            function (result) {
                                $scope.colleagueEvalDone = true;
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
