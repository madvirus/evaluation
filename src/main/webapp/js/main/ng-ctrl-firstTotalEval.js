var firstTotalEvalApp = angular.module('mainApp');

firstTotalEvalApp.controller('firstTotalEvalCtrl',
    ['$scope', 'firstEvalService', 'dialogService',
        function ($scope, firstEvalService, dialogService) {
            $scope.showError = false;
            $scope.ruleGradeSet = [];
            $scope.freeGrade = {
                totalEvals: []
            };

            $scope.addRuleGradeSet = function(sNumber, aNumber, bNumber, cdNumber) {
                var ruleGrade = {
                    rule: {
                        sNumber: sNumber,
                        aNumber: aNumber,
                        bNumber: bNumber,
                        cdNumber: cdNumber
                    }
                };
                ruleGrade.totalEvals = [];
                var size = sNumber + aNumber + bNumber + cdNumber;
                for (var i = 0 ; i < size ; i++) {
                    ruleGrade.totalEvals.push({rateeId: '', command: '', grade: ''});
                }
                $scope.ruleGradeSet.push(ruleGrade);
            };

            $scope.initFreeGrade = function(size) {
                for (var i = 0 ; i < size ; i++) {
                    $scope.freeGrade.totalEvals.push({rateeId: '', command: '', grade: ''});
                }
            };

            $scope.readonly = function() {
                return !$scope.totalEvalAvailable || $scope.totalEvalDone;
            };

            var makeCommand = function(done) {
                var command = {};
                command.done = done;
                command.evalUpdates = [];

                angular.forEach($scope.ruleGradeSet, function(ruleGrade, idx) {
                    angular.forEach(ruleGrade.totalEvals, function(val, idx) {
                        command.evalUpdates.push({
                            rateeId: val.rateeId,
                            comment: val.comment,
                            grade: val.grade
                        });
                    });
                });
                angular.forEach($scope.freeGrade.totalEvals, function(val, idx) {
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
                        }
                    );
                });
            };

            var checkRuleGradeCount = function() {
                var ruleViolationIdx = [];
                angular.forEach($scope.ruleGradeSet, function(ruleGrade, idx) {
                    var sCount = 0, aCount = 0, bCount = 0, cdCount = 0;
                    angular.forEach(ruleGrade.totalEvals, function(val, idx) {
                        if (val.grade == 'S') sCount++;
                        else if (val.grade == 'A') aCount++;
                        else if (val.grade == 'B') bCount++;
                        else if (val.grade == 'C') cdCount++;
                        else if (val.grade == 'D') cdCount++;
                    });

                    if (sCount != ruleGrade.rule.sNumber ||
                        aCount != ruleGrade.rule.aNumber ||
                        bCount != ruleGrade.rule.bNumber ||
                        cdCount != ruleGrade.rule.cdNumber
                    ) {
                        ruleViolationIdx.push(idx);
                    }
                });
                return ruleViolationIdx;
            };

            $scope.saveDone = function() {
                hideErrorDisplay();
                var ruleViolationIdx = checkRuleGradeCount();
                if (!$scope.totalEvalForm.$valid) {
                    showErrorDisplay();
                } else if (ruleViolationIdx.length > 0) {
                    $scope.ruleCountViolation = true;
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
                            }
                        );
                    });

                }
            };

            $scope.showErrorHelp = function() {
                return $scope.showError && $scope.ruleCountViolation;
            };
            var showErrorDisplay = function() {
                dialogService.alert("확인", "올바르게 입력해주세요.");
                $scope.showError = true;
            };
            var hideErrorDisplay = function() {
                $scope.ruleCountViolation = false;
                $scope.showError = false;
            };

        }
    ]);

