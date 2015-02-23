var personalEvalService = angular.module('personalEvalService', ['httpModule']);
personalEvalService.factory('selfEvalService',
    ['$http', 'httpRunner',
        function ($http, httpRunner) {
            var selfEvalService = {};
            selfEvalService.updateSelfPerfEval = function (evalSeasonId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/currentuser/personaleval/selfPerfEval", command);
                    }
                );
            };
            selfEvalService.updateSelfCompeEval = function (evalSeasonId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/currentuser/personaleval/selfCompeEval", command);
                    }
                );
            };
            return selfEvalService;
        }
    ]);

personalEvalService.factory('colleagueEvalService',
    ['$http', 'httpRunner',
        function ($http, httpRunner) {
            var colleagueEvalService = {};
            colleagueEvalService.updateColleagueCompeEval = function (evalSeasonId, rateeId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/ratees/" + rateeId + "/colleagueevals/currentuser/compeEvalSet", command);
                    }
                );
            };
            return colleagueEvalService;
        }
    ]);


personalEvalService.factory('firstEvalService',
    ['$http', 'httpRunner',
        function ($http, httpRunner) {
            var firstEvalService = {};
            firstEvalService.updateFirstPerfEval = function (evalSeasonId, rateeId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/ratees/" + rateeId + "/personaleval/firstPerfEval", command);
                    }
                );
            };
            firstEvalService.rejectSelfPerfEval = function (evalSeasonId, rateeId) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/ratees/" + rateeId + "/personaleval/selfPerfEval?op=reject");
                    }
                );
            };
            firstEvalService.updateFirstCompeEval = function (evalSeasonId, rateeId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/ratees/" + rateeId + "/personaleval/firstCompeEval", command);
                    }
                );
            };
            firstEvalService.rejectSelfCompeEval = function (evalSeasonId, rateeId) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/ratees/" + rateeId + "/personaleval/selfCompeEval?op=reject");
                    }
                );
            };
            firstEvalService.updateFirstTotalEval = function (evalSeasonId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/firstrater/firstTotalEval", command);
                    }
                );
            };

            return firstEvalService;
        }
    ]);

personalEvalService.factory('secondEvalService',
    ['$http', 'httpRunner',
        function ($http, httpRunner) {
            var secondEvalService = {};
            secondEvalService.updateSecondPerfEval = function (evalSeasonId, rateeId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/ratees/" + rateeId + "/personaleval/secondPerfEval", command);
                    }
                );
            };
            secondEvalService.updateSecondCompeEval = function (evalSeasonId, rateeId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/ratees/" + rateeId + "/personaleval/secondCompeEval", command);
                    }
                );
            };
            secondEvalService.updateSecondTotalEval = function (evalSeasonId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/" + evalSeasonId + "/secondrater/secondTotalEval", command);
                    }
                );
            };
            return secondEvalService;
        }
    ]);
