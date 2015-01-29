var adminEvalSeasonController = angular.module(
    'adminEvalSeasonController', ['evalService', 'adminEvalSeasonsApp', 'ui.bootstrap']);

adminEvalSeasonController.controller('adminEvalSeasonListCtrl',
    ['$scope', '$modal', 'evalSeasonService',
        function ($scope, $modal, evalSeasonService) {
            $scope.evalSeasons = [];
            var load = function () {
                evalSeasonService.getEvalSeasons()
                    .then(function (result) {
                        $scope.evalSeasons = result.data;
                    });
            };
            load();

            $scope.openRegDialog = function () {
                var newEvalSeasonDialogInstance = $modal.open({
                    templateUrl: 'newEvalSeasonDialog.html',
                    controller: 'newEvalSeasonDialogCtrl'
                });

                newEvalSeasonDialogInstance.result.then(function () {
                    load()
                }, function () {
                });
            };
        }
    ]);

adminEvalSeasonController.controller('newEvalSeasonDialogCtrl',
    ['$scope', '$modalInstance', 'evalSeasonService',
        function ($scope, $modalInstance, evalSeasonService) {
            $scope.evalSeasonId = "";
            $scope.evalSeasonName = "";
            $scope.duplicateId = "";

            $scope.ok = function () {
                evalSeasonService.createNewSeason({
                    evalSeasonId: $scope.evalSeasonId,
                    name: $scope.evalSeasonName
                }).then(function (result) {
                    $modalInstance.close();
                }, function(result) {
                    if (result.status == 409) {
                        $scope.duplicateId = $scope.evalSeasonId;
                    }
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }]);

adminEvalSeasonController.controller('adminEvalSeasonDetailCtrl',
    ['$scope', '$routeParams', '$modal', 'evalSeasonService', 'dialogService',
        function ($scope, $routeParams, $modal, evalSeasonService, dialogService) {
            $scope.evalSeasonId = $routeParams.evalSeasonId;
            $scope.evalSeason = {};

            $scope.selected = {};
            $scope.mappingMap = {};

            $scope.found = false;
            $scope.notFound = false;


            var load = function () {
                evalSeasonService.getEvalSeason($scope.evalSeasonId)
                    .then(function (result) {
                        $scope.evalSeason = result.data;
                        $scope.selected = {};
                        $scope.mappingMap = {};

                        angular.forEach(result.data.mappings, function(mapping, idx) {
                            $scope.selected[mapping.ratee.id] = false;
                            $scope.mappingMap[mapping.ratee.id] = mapping;
                        });
                        $scope.found = true;
                    }, function(result) {
                        if (result.status == 404) {
                            $scope.notFound = true;
                        }
                    });
            };
            load();

            var openMappingDialog = function(mappingInfo) {
                var multipleMappingFormDialogInstance = $modal.open({
                    templateUrl: 'multipleMappingFormDialog.html',
                    controller: 'multipleMappingFormDialogCtrl',
                    size: 'lg',
                    resolve: {
                        evalSeasonId: function() { return $scope.evalSeasonId; },
                        mappingInfo: function() { return mappingInfo; }
                    }
                });

                multipleMappingFormDialogInstance.result.then(function () {
                    load()
                }, function () {
                });
            };

            $scope.openMappingRegForm = function() {
                openMappingDialog("");
            };

            $scope.updateSelectedMappings = function() {
                var ids = [];
                angular.forEach($scope.selected, function(val, key) {
                    if (val == true) {
                        ids.push(key);
                    }
                });
                var lines = [];
                angular.forEach(ids, function(rateeId, idx) {
                    var mapping = $scope.mappingMap[rateeId];
                    if (mapping != null) {
                        var collNames = [];
                        angular.forEach(mapping.colleagueRaters, function(user, idx) {
                            collNames.push(user.name);
                        });
                        var collNamesString = collNames.length > 0 ? "," + collNames.join(",") : "";
                        lines.push(
                            mapping.ratee.name + "," +
                            mapping["type"] + "," +
                            (mapping.firstRater != null ? mapping.firstRater.name : "-") + "," +
                            mapping.secondRater.name +
                            collNamesString
                        );
                    }
                });
                openMappingDialog(lines.join("\n"));
            };

            $scope.deleteSelectedMappings = function() {
                var ids = [];
                angular.forEach($scope.selected, function(val, key) {
                    if (val == true) {
                        ids.push(key);
                    }
                });
                if (ids.length == 0) {
                    dialogService.alert("확인", "선택한 매핑이 없습니다.");
                    return;
                }

                var confirmDialogInstance = dialogService.confirm("매핑 삭제", "선택한 매핑을 삭제하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    evalSeasonService.deleteMappings($scope.evalSeasonId,ids)
                        .then(function(result) {
                            load();
                        });
                });
            };

            $scope.openEvaluation = function() {
                var confirmDialogInstance = dialogService.confirm("확인", "평가를 오픈하시겠습니까?");
                confirmDialogInstance.result.then(function () {
                    evalSeasonService.open($scope.evalSeasonId)
                        .then(function(result) {
                            load();
                        }, function(result) {
                            if (result.status == 409) {
                                load();
                            }
                        });
                });
            };

        }
    ]);

adminEvalSeasonController.controller('multipleMappingFormDialogCtrl',
    ['$scope', '$modalInstance', 'evalSeasonId', 'mappingInfo', 'userService', 'evalSeasonService',
        function ($scope, $modalInstance, evalSeasonId, mappingInfo, userService, evalSeasonService) {
            $scope.errorLines = [];
            $scope.notFoundNames = [];
            $scope.evalSeasonId = evalSeasonId;
            $scope.mappingInfo = mappingInfo;

            $scope.ok = function () {
                $scope.errorLines = [];
                $scope.notFoundNames = [];
                var errorAndNames = validateInputAndGetNames();
                var errorLines = errorAndNames.errors;
                var names = errorAndNames.names;

                if (errorLines.length > 0) {
                    $scope.errorLines = errorLines;
                    return;
                }
                userService.getUsersByNames(names).then(
                    function(result) {
                        var notFoundNames = [];
                        var nameToIdMap = {};
                        angular.forEach(result.data, function(userModel, index) {
                            if (!userModel.found) {
                                notFoundNames.push(userModel.name);
                            } else {
                                nameToIdMap[userModel.name] = userModel.id;
                            }
                        });
                        if (notFoundNames.length > 0) {
                            // 에러 메시지 출력
                            $scope.notFoundNames = notFoundNames;
                        } else {
                            // 서버에 데이터 전송,
                            // API 추가 필요
                            sendRequestAndHandleResponse(nameToIdMap);
                        }
                    },
                    function(result) {}
                );
            };

            var validateInputAndGetNames = function() {
                var errorLines = [];
                var nameMap = {};
                var lines = $scope.mappingInfo.split("\n");
                angular.forEach(lines, function(line, index) {
                    var lineNumber = index + 1;
                    var row = line.trim().split(/\t|,/);
                    if (row == 0) return;
                    if (row.length < 4) {
                        errorLines.push(lineNumber + "행:입력형식 확인 필요");
                        return;
                    }
                    var typeReg = /^(MEMBER|PART_LEADER|TEAM_LEADER|AM|AM_MEMBER|AM_LEADER$)$/
                    if (!typeReg.test(row[1])) {
                        errorLines.push(lineNumber+":잘못된 타입 = " + row[1]);
                    } else {
                        angular.forEach(row, function(val, key) {
                            if (key != 1 && val != '-') nameMap[val] = true;
                        });
                    }
                });
                var names = [];
                angular.forEach(nameMap, function(value, name){
                    names.push(name);
                }, names);
                return {errors: errorLines, names: names};
            };

            var sendRequestAndHandleResponse = function(nameToIdMap) {
                var rateeMappings = [];
                var lines = $scope.mappingInfo.split("\n");
                angular.forEach(lines, function(line, index) {
                    var row = line.trim().split(/\t|,/);
                    var mapping = {
                        rateeId: nameToIdMap[row[0]],
                        type: row[1],
                        secondRaterId: nameToIdMap[row[3]]
                    };
                    if (row[2] != '-') {
                        mapping.firstRaterId = nameToIdMap[row[2]];
                    }
                    if (row.length > 4) {
                        var colleagueRaterIds = [];
                        for (var i = 4; i < row.length ; i++) {
                            colleagueRaterIds.push(nameToIdMap[row[i]]);
                        }
                        mapping.colleagueRaterIds = colleagueRaterIds;
                    }
                    rateeMappings.push(mapping);
                });

                // 서버에 매핑 수정 요청 전송
                evalSeasonService.updateMappings($scope.evalSeasonId, {rateeMappings: rateeMappings})
                    .then(function(result) {
                        $modalInstance.close(result);
                    }, function(result) {
                        // TODO 에러 처리
                        alert(result.status);
                    });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }]);
