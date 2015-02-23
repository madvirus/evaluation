var httpModule = angular.module('httpModule', ['dialogModule']);
httpModule.factory('httpRunner',
    ['dialogService', function (dialogService) {
        return {
            create: function (httpCreator) {
                var runner = {
                    then: function(successCallback, errorCallback) {
                        var executedHttp = httpCreator();
                        executedHttp
                            .success(function (data, status, headers, config) {
                                successCallback({data: data, status: status, headers: headers});
                            })
                            .error(function (data, status, headers, config) {
                                console.log("에러: " + status);
                                if (errorCallback == undefined || ! errorCallback({data: data, status: status, headers: headers})) {
                                    // TODO angularjs http 공통 에러 처리 코드!
                                    dialogService.error("에러", "에러["+status+"]가 발생했습니다.");
                                }
                            });
                    }
                };
                return runner;
            }
        };
    }
    ]);