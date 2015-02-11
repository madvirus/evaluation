var evalAppService = angular.module('evalService', []);

function runHttpAndGetPromise($q, executedHttp) {
    var deferred = $q.defer();
    executedHttp.success(function (data, status, headers, config) {
        deferred.resolve({data: data, status: status, headers: headers});
    }).error(function (data, status, headers, config) {
        console.log("에러: " + status);
        deferred.reject({data: data, status: status, headers: headers});
    });
    return deferred.promise;
}

evalAppService.factory('evalSeasonService',
    ['$http', '$q',
        function ($http, $q) {
            var evalSeasonService = {};
            evalSeasonService.getEvalSeasons = function () {
                return runHttpAndGetPromise($q, $http.get("/api/evalseasons", {}));
            };
            evalSeasonService.createNewSeason = function(seasonData) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons", seasonData));
            };
            evalSeasonService.getEvalSeason = function(evalSeasonId) {
                return runHttpAndGetPromise($q, $http.get("/api/evalseasons/"+evalSeasonId, {}));
            };
            evalSeasonService.updateMappings = function(evalSeasonId, mappings) {
                return runHttpAndGetPromise($q, $http.post("/api/evalseasons/"+evalSeasonId+"/mappings", mappings));
            };
            evalSeasonService.deleteMappings = function(evalSeasonId, deletedIds) {
                return runHttpAndGetPromise($q, $http.delete("/api/evalseasons/"+evalSeasonId+"/mappings?ids="+deletedIds.join(",")) );
            };
            evalSeasonService.open = function(evalSeasonId) {
                return runHttpAndGetPromise($q, $http.put("/api/evalseasons/"+evalSeasonId+"?action=open") );
            };
            evalSeasonService.startColleagueEvaluation = function(evalSeasonId) {
                return runHttpAndGetPromise($q, $http.put("/api/evalseasons/"+evalSeasonId+"?action=startColleagueEval") );
            };

            return evalSeasonService;
        }
    ]);

evalAppService.factory('userService',
    ['$http', '$q',
        function ($http, $q) {
            var userService = {};
            userService.getUsersByNames = function (names) {
                return runHttpAndGetPromise($q, $http.get("/api/users", {"params": {"op": "findId", "name": names}}));
            };
            return userService;
        }
    ]);

evalAppService.factory('personalEvalService', []);