var evalAdminApp = angular.module('adminEvalSeasonsApp',
    ['ngRoute', 'ui.bootstrap', 'adminEvalSeasonController', 'evalService']);

evalAdminApp.config(
    ['$routeProvider', '$httpProvider',
        function($routeProvider) {
            $routeProvider.
                when('/', {
                    templateUrl: '/template/admin/evalseason/evalseasons.jsp',
                    controller: 'adminEvalSeasonListCtrl'
                }).
                when('/:evalSeasonId', {
                    templateUrl: '/template/admin/evalseason/evalseasonDetail.jsp',
                    controller: 'adminEvalSeasonDetailCtrl'
                }).
                when('/:evalSeasonId/distrule', {
                    templateUrl: '/template/admin/evalseason/evalseasonDistrule.jsp',
                    controller: 'adminEvalSeasonDistruleCtrl'
                }).
                when('/:evalSeasonId/evalStates', {
                    templateUrl: '/template/admin/evalseason/evalStates.jsp',
                    controller: 'adminEvalSeasonStatesCtrl'
                }).
                otherwise({
                    redirectTo: '/'
                });
        }
    ]);


evalAdminApp.controller("adminEvalSeasonsCtrl", ["$rootScope", function($rootScope) {
    $rootScope.showRouteError = false;
    $rootScope.$on("$routeChangeStart", function() {
        $rootScope.showRouteError = false;
    });
    $rootScope.$on("$routeChangeError", function(event, current, previous, rejection) {
        $rootScope.showRouteError = true;
        $rootScope.errorStatus = rejection.status;
    })
}]);
