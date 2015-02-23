var firstPerfEvalApp = angular.module('mainApp');

firstPerfEvalApp.controller('secondPerfEvalCtrl',
    ['$scope', 'secondEvalService', 'dialogService',
        function ($scope, secondEvalService, dialogService) {
            $scope.grades = ['S', 'A', 'B', 'C', 'D'];
            $scope.showError = false;

            $scope.readonly = function() {
                return !$scope.firstTotalEvalDone || $scope.secondTotalEvalDone;
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
                        secondEvalService.updateSecondPerfEval(
                            $scope.evalSeasonId, $scope.rateeId, command
                        ).then(
                            function (result) {
                                dialogService.success("성공", "성과 평가를 저장했습니다.");
                            }
                        );
                    });
                }
            };
        }
    ]);

