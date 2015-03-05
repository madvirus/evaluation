var firstCompeEvalApp = angular.module('mainApp');

firstCompeEvalApp.controller('firstCompeEvalCtrl',
    ['$scope', 'firstEvalService', 'dialogService', 'competencyItemService',
        function ($scope, firstEvalService, dialogService, competencyItemService) {
            $scope.grades = ['S', 'A', 'B', 'C', 'D'];
            $scope.showError = false;
            $scope.evalData = {};
            $scope.competencyItems = competencyItemService.getItems();
            $scope.descCollapsed = false;
            $scope.readonly = function() {
                return !$scope.selfEvalDone || $scope.firstTotalEvalDone;
            };

            var showErrorDisplay = function () {
                $scope.showError = true;
            };
            var hideErrorDisplay = function () {
                $scope.showError = false;
            };

            $scope.getMark = function() {
                var sum = 0;
                var count = 5;
                angular.forEach($scope.evalSet.commonsEvals, function(val, idx){
                    sum += getMark(val.grade);
                });
                if ($scope.hasLeadership) {
                    angular.forEach($scope.evalSet.leadershipEvals, function(val, idx){
                        sum += getMark(val.grade);
                    });
                    count += $scope.evalSet.leadershipEvals.length;
                }
                if ($scope.hasAm) {
                    angular.forEach($scope.evalSet.amEvals, function(val, idx) {
                        sum += getMark(val.grade);
                    });
                    count += $scope.evalSet.amEvals.length;
                }
                return sum / count;
            };

            var getMark = function(grade) {
                if (grade == 'S') return 5;
                else if (grade == 'A') return 4;
                else if (grade == 'B') return 3;
                else if (grade == 'C') return 2;
                else if (grade == 'D') return 1;
                return 0;
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
                        firstEvalService.updateFirstCompeEval(
                            $scope.evalSeasonId, $scope.rateeId, command
                        ).then(
                            function (result) {
                                dialogService.success("성공", "역량 평가를 저장했습니다.");
                            }
                        );
                    });
                }
            };

            $scope.reject = function () {
                hideErrorDisplay();
                var confirmDialogInstance = dialogService.confirm(
                    "반려", "역량 평가를 반려하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    firstEvalService.rejectSelfCompeEval(
                        $scope.evalSeasonId, $scope.rateeId
                    ).then(
                        function (result) {
                            $scope.selfEvalDone = false;
                            initEvalData();
                            dialogService.success("성공", "평가를 반려했습니다.");
                        }
                    );
                });
            };
        }
    ]);

