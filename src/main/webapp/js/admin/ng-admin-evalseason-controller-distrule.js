adminEvalSeasonController.controller('adminEvalSeasonDistruleCtrl',
    ['$scope', '$routeParams', '$modal', 'evalSeasonService',
        function ($scope, $routeParams, $modal, evalSeasonService) {
            $scope.evalSeasonId = $routeParams.evalSeasonId;

            var load = function () {
                evalSeasonService.getDistRule($scope.evalSeasonId).then(
                    function (result) {
                        $scope.ruleDataList = result.data.ruleDataList;
                    },
                    function (result) {
                        return false;
                    }
                );
            };
            load();

            $scope.openRuleManager = function (ruleData) {
                var copiedRuleData = JSON.parse(JSON.stringify(ruleData));
                var ruleManagerDialogInstance = $modal.open({
                    templateUrl: 'ruleManagerDialog.html',
                    controller: 'ruleManagerDialogCtrl',
                    size: 'lg',
                    resolve: {
                        evalSeasonId: function () {
                            return $scope.evalSeasonId;
                        },
                        ruleData: function () {
                            return copiedRuleData;
                        }
                    }
                });

                ruleManagerDialogInstance.result.then(function () {
                    load();
                });
            };
        }]);


adminEvalSeasonController.controller('ruleManagerDialogCtrl',
    ['$scope', '$modalInstance', 'evalSeasonId', 'ruleData', 'evalSeasonService', 'dialogService',
        function ($scope, $modalInstance, evalSeasonId, ruleData, evalSeasonService, dialogService) {
            $scope.evalSeasonId = evalSeasonId;
            $scope.ruleData = ruleData;
            $scope.showError = false;

            $scope.addRule = function() {
                var rule = {
                    name: '',
                    sNumber: 0,
                    aNumber: 0,
                    bNumber: 0,
                    cdNumber: 0,
                    ratees: []
                };
                $scope.ruleData.ruleList.push(rule);
            };
            $scope.deleteRule = function(rule) {
                var idx = -1;
                for (var i = 0 ; i < $scope.ruleData.ruleList.length ; i++) {
                    if ($scope.ruleData.ruleList[i] == rule) {
                        idx = i;
                        break;
                    }
                }
                if (idx >= 0) {
                    $scope.ruleData.ruleList.splice(i, 1);
                }
            };

            $scope.selectAll = function(rule) {
                rule.ratees = [];
                angular.forEach(ruleData.ratees, function(val, idx) {
                    rule.ratees.push(val);
                });
            };
            $scope.deselectAll = function(rule) {
                rule.ratees = [];
            };
            $scope.cancel = function () {
                $modalInstance.dismiss();
            };

            var showErrorDisplay = function() {
                $scope.showError = true;
            };
            var hideErrorDisplay = function() {
                $scope.showError = false;
            };

            $scope.save = function() {
                // 폼 데이터 검증
                hideErrorDisplay();

                $scope.dupRatees = [];
                var notMatching = [];
                var dupRatees = [];
                var zeroSum = [];

                var names = {};
                angular.forEach($scope.ruleData.ruleList, function(rule, idx) {
                    var sum = rule.sNumber + rule.aNumber + rule.bNumber + rule.cdNumber;
                    if (sum == 0) {
                        zeroSum.push(idx);
                    } else if (rule.ratees.length != sum) {
                        notMatching.push(idx);
                    }
                    angular.forEach(rule.ratees, function(ratee, idx) {
                        if (names[ratee.name] == null) {
                            names[ratee.name] = ratee;
                        } else {
                            dupRatees.push(ratee);
                        }
                    });
                });

                if (!$scope.ruleListForm.$valid || notMatching.length > 0 || dupRatees.length > 0 || zeroSum.length > 0) {
                    $scope.dupRatees = dupRatees;
                    showErrorDisplay();
                } else {
                    var confirmDialogInstance = dialogService.confirm('확인', '배분 규칙을 적용하시겠습니까?');
                    confirmDialogInstance.result.then(function () {
                        var command = {};
                        command.firstRaterId = ruleData.firstRater.id;
                        command.rules = [];
                        angular.forEach($scope.ruleData.ruleList, function(rule, idx) {
                            var distRule = {
                                name: rule.name,
                                sNumber: rule.sNumber,
                                aNumber: rule.aNumber,
                                bNumber: rule.bNumber,
                                cdNumber: rule.cdNumber
                            };
                            var rateeIds = [];
                            angular.forEach(rule.ratees, function(ratee, idx) {
                                rateeIds.push(ratee.id);
                            });
                            distRule.rateeIds = rateeIds;
                            command.rules.push(distRule);
                        });

                        evalSeasonService.updateDistRule($scope.evalSeasonId, command).then(
                            function(result) {
                                $modalInstance.close();
                                dialogService.success('배분 변경', '배분 정보를 변경했습니다.');
                            },
                            function(result) {
                                if (result.status == 500) {
                                    if (result.data.exceptionType == 'FirstEvalDoneException') {
                                        dialogService.error("에러", "피평가자의 1차 평가가 완료됐거나 변경할 1차 평가자가 1차 평가를 완료한 경우 배분 규칙을 변경할 수 없습니다.");
                                        return true;
                                    }
                                }
                                return false;
                            }
                        );
                    });
                }

            }
        }]);
