var evalAdminApp = angular.module('evalAdminApp',['ngRoute', 'adminController']);

evalAdminApp.config(
    ['$routeProvider', '$httpProvider',
        function($routeProvider, $httpProvider) {
            $routeProvider.
                when('/', {
                    templateUrl: '/admin/home'
                }).
                when('/admin/evalseason', {
                    templateUrl: '/admin/evalseasons',
                    controller: 'adminEvalSeasonListCtrl'
                }).
                otherwise({
                    redirectTo: '/'
                });
        }
    ]);
