var personalEvalService = angular.module('personalEvalService', []);

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

personalEvalService.factory('selfEvalService',
    ['$http', '$q',
        function ($http, $q) {
            var selfEvalService = {};
            selfEvalService.updateSelfPerfEval = function (evalSeasonId, command) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/currentuser/personaleval/selfPerfEval", command));
            };
            selfEvalService.updateSelfCompeEval = function (evalSeasonId, command) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/currentuser/personaleval/selfCompeEval", command));
            };
            return selfEvalService;
        }
    ]);

personalEvalService.factory('colleagueEvalService',
    ['$http', '$q',
        function ($http, $q) {
            var colleagueEvalService = {};
            colleagueEvalService.getColleagueCompeEval = function (evalSeasonId, rateeId) {
                return runHttpAndGetPromise($q,
                    $http.get("/api/evalseasons/"+evalSeasonId+"/ratees/"+rateeId+"/colleagueevals/currentuser/compeEvalSet"));
            };
            colleagueEvalService.updateColleagueCompeEval = function (evalSeasonId, rateeId, command) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/ratees/"+rateeId+"/colleagueevals/currentuser/compeEvalSet", command));
            };
            return colleagueEvalService;
        }
    ]);


personalEvalService.factory('firstEvalService',
    ['$http', '$q',
        function ($http, $q) {
            var firstEvalService = {};
            firstEvalService.updateFirstPerfEval = function (evalSeasonId, rateeId, command) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/ratees/"+rateeId+"/personaleval/firstPerfEval", command));
            };
            firstEvalService.rejectSelfPerfEval = function (evalSeasonId, rateeId) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/ratees/"+rateeId+"/personaleval/selfPerfEval?op=reject"));
            };
            firstEvalService.updateFirstCompeEval = function (evalSeasonId, rateeId, command) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/ratees/"+rateeId+"/personaleval/firstCompeEval", command));
            };
            firstEvalService.rejectSelfCompeEval = function (evalSeasonId, rateeId) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/ratees/"+rateeId+"/personaleval/selfCompeEval?op=reject"));
            };
            firstEvalService.updateFirstTotalEval = function (evalSeasonId, command) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/evalseasons/"+evalSeasonId+"/firstrater/firstTotalEval", command));
            };

            return firstEvalService;
        }
    ]);
