var firstCompeEvalApp = angular.module('mainApp');

firstCompeEvalApp.controller('secondCompeEvalCtrl',
    ['$scope', 'secondEvalService', 'dialogService', 'competencyItemService',
        function ($scope, secondEvalService, dialogService, competencyItemService) {
            $scope.grades = ['S', 'A', 'B', 'C', 'D'];
            $scope.showError = false;
            $scope.evalData = {};
            $scope.competencyItems = competencyItemService.getItems();
            $scope.descCollapsed = false;
            $scope.readonly = function() {
                return !$scope.firstTotalEvalDone || $scope.secondTotalEvalDone;
            };

            var showErrorDisplay = function () {
                $scope.showError = true;
            };
            var hideErrorDisplay = function () {
                $scope.showError = false;
            };

            var makeCommand = function (done) {
                var command = {};
                command.evalSeasonId = $scope.evalSeasonId;
                command.evalSet = {
                    commonsEvals: $scope.evalSet.commonsEvals,
                    totalEval: $scope.evalSet.totalEval,
                    done: done
                };
                if ($scope.hasLeadership) {
                    command.evalSet.leadershipEvals = $scope.evalSet.leadershipEvals;
                }
                if ($scope.hasAm) {
                    command.evalSet.amEvals = $scope.evalSet.amEvals;
                }
                return command;
            };

            $scope.save = function () {
                hideErrorDisplay();
                if (!$scope.compeEvalForm.$valid) {
                    showErrorDisplay();
                } else {
                    hideErrorDisplay();
                    var confirmDialogInstance = dialogService.confirm(
                        "역량 평가 저장", "역량 평가를 저장하시겠습니까?");
                    confirmDialogInstance.result.then(function () {
                        var command = makeCommand();
                        secondEvalService.updateSecondCompeEval(
                            $scope.evalSeasonId, $scope.rateeId, command
                        ).then(
                            function (result) {
                                dialogService.success("성공", "역량 평가를 저장했습니다.");
                            },
                            function () {
                                // TODO 에러 메시지 처리
                            }
                        );
                    });
                }
            };
        }
    ]);

