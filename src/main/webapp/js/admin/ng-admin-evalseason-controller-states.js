adminEvalSeasonController.controller('adminEvalSeasonStatesCtrl',
    ['$scope', '$routeParams', '$modal', 'evalSeasonService', 'dialogService',
        function ($scope, $routeParams, $modal, evalSeasonService, dialogService) {
            $scope.evalSeasonId = $routeParams.evalSeasonId;

            var load = function () {
                evalSeasonService.getEvalSeasonStates($scope.evalSeasonId).then(
                    function (result) {
                        $scope.allEvalStates = result.data.allEvalStates;
                        initFirstRaterReturnable();
                    },
                    function (result) {
                        return false;
                    }
                );
            };
            load();

            var firstReturnable = {};
            var initFirstRaterReturnable = function() {
                firstReturnable = {};
                angular.forEach($scope.allEvalStates, function(state, idx) {
                    if (!state.firstSkip && state.first.state == 'DONE') {
                        var returnable = firstReturnable[state.first.rater.id];
                        if (returnable == null || returnable != false) {
                            var secondState = state.second.state;
                            if (secondState == 'DONE') {
                                firstReturnable[state.first.rater.id] = false;
                            } else {
                                firstReturnable[state.first.rater.id] = true;
                            }
                        }
                    }
                });
            };

            $scope.canReturnDraft = function(firstRaterId) {
                var result = firstReturnable[firstRaterId];
                return result == null ? false : result;
            };

            $scope.returnToDraftFirstEvalOf = function(firstRater) {
                var confirmDialogInstance = dialogService.confirm('확인', firstRater.name+'의 1차 평가 완료를 임시 상태로 되돌리겠습니까?');
                confirmDialogInstance.result.then(function () {
                    evalSeasonService.returnFirstEvalDraft($scope.evalSeasonId, firstRater.id).then(
                        function(result) {
                            load();
                            dialogService.success('성공', '1차 평가를 임시 상태로 변경했습니다.');
                        }
                    );
                });
            };
        }]);
