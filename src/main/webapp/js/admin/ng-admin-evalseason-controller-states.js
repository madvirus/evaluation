adminEvalSeasonController.controller('adminEvalSeasonStatesCtrl',
    ['$scope', '$routeParams', '$modal', 'evalSeasonService',
        function ($scope, $routeParams, $modal, evalSeasonService) {
            $scope.evalSeasonId = $routeParams.evalSeasonId;

            var load = function () {
                evalSeasonService.getEvalSeasonStates($scope.evalSeasonId).then(
                    function (result) {
                        $scope.allEvalStates = result.data.allEvalStates;
                    },
                    function (result) {
                        return false;
                    }
                );
            };
            load();
        }]);
