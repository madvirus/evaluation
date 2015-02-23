var evalAdminApp = angular.module('adminEvalSeasonsApp',
    ['ngRoute', 'ui.bootstrap', 'adminEvalSeasonController', 'evalService']);

evalAdminApp.config(
    ['$routeProvider', '$httpProvider',
        function($routeProvider, $httpProvider) {
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
                otherwise({
                    redirectTo: '/'
                });
        }
    ]);
