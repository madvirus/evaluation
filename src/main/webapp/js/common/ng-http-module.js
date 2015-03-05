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
                                console.log("httpModule.httpRunner 에러 로그: " + status);
                                if (errorCallback == undefined || ! errorCallback({data: data, status: status, headers: headers})) {
                                    if (status == 0) {
                                        dialogService.error("에러", "서버에 연결할 수 없습니다.");
                                    } else if (status == 403) {
                                        dialogService.error("에러", "접근 권한이 없습니다.");
                                    } else {
                                        dialogService.error("에러", "에러[" + status + "]가 발생했습니다.");
                                    }
                                }
                            });
                    }
                };
                return runner;
            }
        };
    }
    ]);