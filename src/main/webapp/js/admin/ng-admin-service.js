var evalAppService = angular.module('evalService', ['httpModule']);

evalAppService.factory('evalSeasonService',
    ['$http', 'httpRunner',
        function ($http, httpRunner) {
            var evalSeasonService = {};
            evalSeasonService.getEvalSeasons = function () {
                return httpRunner.create(
                    function() {
                        return $http.get("/api/evalseasons", {});
                    }
                );
            };
            evalSeasonService.createNewSeason = function(seasonData) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons", seasonData);
                    }
                );
            };
            evalSeasonService.getEvalSeason = function(evalSeasonId) {
                return httpRunner.create(
                    function() {
                        return $http.get("/api/evalseasons/"+evalSeasonId, {});
                    }
                );
            };
            evalSeasonService.getDistRule = function(evalSeasonId) {
                return httpRunner.create(
                    function() {
                        return $http.get("/api/evalseasons/"+evalSeasonId+"/distrule", {});
                    }
                );
            };
            evalSeasonService.updateDistRule = function(evalSeasonId, command) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/"+evalSeasonId+"/distrule", command);
                    }
                );
            };
            evalSeasonService.updateMappings = function(evalSeasonId, mappings) {
                return httpRunner.create(
                    function() {
                        return $http.post("/api/evalseasons/"+evalSeasonId+"/mappings", mappings);
                    }
                );
            };
            evalSeasonService.deleteMappings = function(evalSeasonId, deletedIds) {
                return httpRunner.create(
                    function() {
                        return $http.delete("/api/evalseasons/"+evalSeasonId+"/mappings?ids="+deletedIds.join(","));
                    }
                );
            };
            evalSeasonService.open = function(evalSeasonId) {
                return httpRunner.create(
                    function() {
                        return $http.put("/api/evalseasons/"+evalSeasonId+"?action=open");
                    }
                );
            };
            evalSeasonService.startColleagueEvaluation = function(evalSeasonId) {
                return httpRunner.create(
                    function() {
                        return $http.put("/api/evalseasons/"+evalSeasonId+"?action=startColleagueEval");
                    }
                );
            };

            return evalSeasonService;
        }
    ]);

evalAppService.factory('userService',
    ['$http', 'httpRunner',
        function ($http, httpRunner) {
            var userService = {};
            userService.getUsersByNames = function (names) {
                return httpRunner.create(
                    function() {
                        return $http.get("/api/users", {"params": {"op": "findId", "name": names}});
                    }
                );
            };
            return userService;
        }
    ]);

evalAppService.factory('personalEvalService', []);