var evalAppService = angular.module('evalService', []);


var createParams = function (pNum, added) {
    var params = {offset: (pNum - 1) * 10, size: 10};
    if (added) {
        angular.forEach(added, function (value, key) {
            params[key] = value;
        });
    }
    return params;
};

function runHttpAndGetPromise($q, executedHttp) {
    var deferred = $q.defer();
    executedHttp.success(function (data, status, headers, config) {
        deferred.resolve({data: data, status: status});
    }).error(function (data, status, headers, config) {
        console.log("에러: " + status);
        deferred.reject({data: data, status: status});
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
            }
            return evalSeasonService;
        }
    ]);
