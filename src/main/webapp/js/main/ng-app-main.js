var mainApp = angular.module('mainApp',
    ['ui.bootstrap', 'personalEvalService', 'dialogModule', 'competency.item', 'monospaced.elastic']);

mainApp.config(function ($provide) {
    $provide.decorator('inputDirective', function($delegate, $log) {
        $log.debug('Hijacking input directive');
        var directive = $delegate[0];
        angular.extend(directive.link, {
            post: function(scope, element, attr, ctrls) {
                element.on('compositionupdate', function (event) {
                    $log.debug('Composition update, faking end');
                    element.triggerHandler('compositionend');
                })
            }
        });
        return $delegate;
    });
    $provide.decorator('textareaDirective', function($delegate, $log) {
        $log.debug('Hijacking input directive');
        var directive = $delegate[0];
        angular.extend(directive.link, {
            post: function(scope, element, attr, ctrls) {
                element.on('compositionupdate', function (event) {
                    $log.debug('Composition update, faking end');
                    element.triggerHandler('compositionend');
                })
            }
        });
        return $delegate;
    });
});

mainApp.directive('ngInitial', function($parse) {
    return {
        restrict: "A",
        compile: function($element, $attrs) {
            var initialValue = $attrs.value || $element.val();
            return {
                pre: function($scope, $element, $attrs) {
                    $parse($attrs.ngModel).assign($scope, initialValue);
                }
            }
        }
    };
});

mainApp.filter('nl2br', function($sce){
    return function(msg,is_xhtml) {
        var is_xhtml = is_xhtml || true;
        var breakTag = (is_xhtml) ? '<br />' : '<br>';
        var msg = (msg + '').replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1'+ breakTag +'$2');
        return $sce.trustAsHtml(msg);
    }
});
