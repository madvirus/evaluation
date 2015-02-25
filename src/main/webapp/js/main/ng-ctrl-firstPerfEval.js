var firstPerfEvalApp = angular.module('mainApp');

firstPerfEvalApp.controller('firstPerfEvalCtrl',
    ['$scope', 'firstEvalService', 'dialogService',
        function ($scope, firstEvalService, dialogService) {
            $scope.grades = ['S', 'A', 'B', 'C', 'D'];
            $scope.showError = false;
            $scope.weights = [];

            $scope.readonly = function() {
                return !$scope.selfEvalDone || $scope.firstTotalEvalDone;
            };
            $scope.addWeight = function(weight) {
                $scope.weights.push(weight);
            };

            $scope.getMark = function() {
                var sum = 0;
                angular.forEach($scope.evalData.itemEvals, function(val, idx) {
                    var mark = 0;
                    if (val.grade == 'S') mark = 5;
                    else if (val.grade == 'A') mark = 4;
                    else if (val.grade == 'B') mark = 3;
                    else if (val.grade == 'C') mark = 2;
                    else if (val.grade == 'D') mark = 1;
                    sum += mark * $scope.weights[idx] / 100;
                });
                return sum;
            };

            var showErrorDisplay = function () {
                $scope.showError = true;
            };
            var hideErrorDisplay = function () {
                $scope.showError = false;
            };

            var makeUpdatePerfEvalCommand = function (done) {
                var command = {};
                command.itemEvals = $scope.evalData.itemEvals;
                command.totalEval = $scope.evalData.totalEval;
                return command;
            };

            $scope.save = function () {
                hideErrorDisplay();
                if (!$scope.perfEvalForm.$valid) {
                    showErrorDisplay();
                } else {
                    hideErrorDisplay();
                    var confirmDialogInstance = dialogService
                        .confirm("성과 평가 저장", "성과 평가를 저장하시겠습니까?");
                    confirmDialogInstance.result.then(function () {
                        var command = makeUpdatePerfEvalCommand();
                        firstEvalService.updateFirstPerfEval(
                            $scope.evalSeasonId, $scope.rateeId, command
                        ).then(
                            function (result) {
                                dialogService.success("성공", "성과 평가를 저장했습니다.");
                            }
                        );
                    });
                }
            };

            $scope.reject = function () {
                hideErrorDisplay();
                var confirmDialogInstance = dialogService.confirm(
                    "반려", "성과 평가를 반려하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    firstEvalService.rejectSelfPerfEval(
                        $scope.evalSeasonId, $scope.rateeId
                    ).then(
                        function (result) {
                            $scope.selfEvalDone = false;
                            initEvalData($scope.evalData.itemEvals.length);
                            dialogService.success("성공", "평가를 반려했습니다.");
                        }
                    );
                });
            };
        }
    ]);

