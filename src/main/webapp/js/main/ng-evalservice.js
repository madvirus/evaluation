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
            selfEvalService.getSelfPerfEval = function (personalEvalId) {
                return runHttpAndGetPromise($q,
                    $http.get("/api/personalevals/"+personalEvalId+"/selfPerfEval"));
            };
            selfEvalService.updateSelfPerfEval = function (personalEvalId, command) {
                return runHttpAndGetPromise($q,
                    $http.post("/api/personalevals/"+personalEvalId+"/selfPerfEval", command));
            };
            return selfEvalService;
        }
    ]);
