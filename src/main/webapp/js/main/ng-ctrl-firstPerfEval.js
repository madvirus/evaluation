var firstPerfEvalApp = angular.module('mainApp');

firstPerfEvalApp.controller('firstPerfEvalCtrl',
    ['$scope', 'firstEvalService', 'dialogService',
        function ($scope, firstEvalService, dialogService) {
            $scope.showError = false;
            $scope.evalData = {};

            $scope.init = function (size) {
                initEvalData(size);
            };

            var initEvalData = function(size) {
                $scope.evalData.itemEvals = [];
                for (var i = 0; i < size; i++) {
                    $scope.evalData.itemEvals.push({comment: '', grade: 'A'});
                }
                $scope.evalData.totalEval = {comment: '', grade: 'A'};
            };

            $scope.readonly = function() {
                return !$scope.selfEvalDone || $scope.firstTotalEvalDone;
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
                    var confirmDialogInstance = dialogService.confirm(
                        "성과 평가 저장", "성과 평가를 저장하시겠습니까?");
                    confirmDialogInstance.result.then(function () {
                        var command = makeUpdatePerfEvalCommand();
                        firstEvalService.updateFirstPerfEval(
                            $scope.evalSeasonId, $scope.rateeId, command
                        ).then(
                            function (result) {
                                dialogService.success("성공", "성과 평가를 저장했습니다.");
                            },
                            function () {
                                // TODO 에러 메시지 처리
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
                        },
                        function (result) {
                            // TODO 에러 메시지 처리
                        }
                    );
                });
            };
        }
    ]);

