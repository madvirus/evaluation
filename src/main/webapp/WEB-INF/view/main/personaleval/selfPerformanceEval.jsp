<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="selfPerfEvalApp">
<head>
    <meta charset="utf-8">
    <title>평가 시스템</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
    <style type="text/css">
        [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
            display: none !important;
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/layout/navi.jsp"/>

<div class="container" ng-controller="selfPerfEvalCtrl" ng-init="init('${evalSeasonId}', '${personalEvalId}')">
    <div class="page-header">
        <h3>본인 성과 평가
            <small><sec:authentication property="principal.name"/>님</small>
        </h3>
    </div>

    <p ng-show="!disableBtn" ng-cloak>
        <button class="btn btn-default btn-xs" type="button" ng-click="addRow()">추가</button>
        <button class="btn btn-default btn-xs" type="button" ng-click="removeRow()">삭제</button>
    </p>

    <form novalidate name="perfEvalForm">
        <table class="table table-bordered table-condensed">
            <thead>
            <tr>
                <th rowspan="2" class="col-md-1">선택</th>
                <th rowspan="2" class="col-md-1">구분</th>
                <th rowspan="2" class="col-md-2">목표항목</th>
                <th rowspan="2" class="col-md-3">달성도</th>
                <th rowspan="2" class="col-md-1">가중치</th>
                <th colspan="2">본인 평가</th>
            </tr>
            <tr>
                <th class="col-md-3">의견</th>
                <th class="col-md-1">등급</th>
            </tr>
            </thead>
            <tbody ng-cloak>
            <tr ng-form='itemForm' ng-repeat="itemAndEval in evalData.itemAndEvals">
                <td class="col-md-1">
                    <input type="checkbox" ng-model="itemSelected[$index]"
                           ng-readonly="evalData.done" />
                </td>
                <td class="col-md-1" ng-class="{'has-error': showError && !itemForm.category.$valid}">
                    <input type="text" ng-model="itemAndEval.item.category"
                           ng-class="{'form-control': true, 'input-sm': true}"
                           ng-readonly="evalData.done"
                           name="category" required>
                </td>
                <td class="col-md-2" ng-class="{'has-error': showError && !itemForm.goalType.$valid}">
                    <input type="text" ng-model="itemAndEval.item.goalType"
                           ng-class="{'form-control': true, 'input-sm': true}"
                           ng-readonly="evalData.done"
                           name="goalType" required>
                </td>
                <td class="col-md-3" ng-class="{'has-error': showError && !itemForm.result.$valid}">
                    <input type="text" ng-model="itemAndEval.item.result"
                           ng-class="{'form-control': true, 'input-sm': true}"
                           ng-readonly="evalData.done"
                           name="result" required>
                </td>
                <td class="col-md-1" ng-class="{'has-error': showError && (!itemForm.weight.$valid || itemAndEval.item.weight == 0)}">
                    <input type="number" ng-model="itemAndEval.item.weight"
                           ng-class="{'form-control': true, 'input-sm': true}"
                           ng-readonly="evalData.done"
                           name="weight" min="0" max="100" required>
                </td>
                <td class="col-md-3" ng-class="{'has-error': showError && !itemForm.comment.$valid}">
                    <textarea ng-model="itemAndEval.eval.comment"
                              ng-class="{'form-control': true, 'input-sm': true}"
                              ng-readonly="evalData.done"
                              name="comment" required></textarea>
                </td>
                <td class="col-md-1">
                    <select ng-model="itemAndEval.eval.grade" ng-options="grade for grade in grades"
                            ng-readonly="evalData.done"
                            class="form-control input-sm">
                    </select>
                </td>
            </tr>
            </tbody>
            <tfoot>
            <tr ng-show="showError" ng-cloak>
                <td colspan="7">
                    <div class="alert alert-danger" role="alert">
                        평가를 완료하려면,
                        <ul>
                            <li ng-show="sumError">가중치 합은 <strong>100</strong>이어야 합니다.</li>
                            <li ng-show="zeroWeightError">가중치는 0 보다 커야 합니다.</li>
                            <li ng-show="!perfEvalForm.$valid">모든 항목을 입력해야 합니다.</li>
                        </ul>
                    </div>
                </td>
            </tr>
            <tr ng-show="!disableBtn" ng-cloak>
                <td colspan="7">
                    <button class="btn btn-default" type="button" ng-click="saveDraft()">임시저장</button>
                    <button class="btn btn-default btn-primary" type="button " ng-click="done()">본인 성과 평가 완료</button>
                </td>
            </tr>
            </tfoot>
        </table>
        <div ng-show="evalData.done" ng-cloak class="alert alert-info" role="alert">
            본인 성과 평가를 완료했습니다.
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/view/common/dialogTemplate.jsp"/>

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>
<script src="/webjars/angularjs/1.2.16/angular.js"></script>
<script src="/webjars/angularjs/1.2.16/angular-route.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap-tpls.js"></script>
<script src="/js/common/ng-dialog-module.js"></script>
<script src="/js/main/ng-evalservice.js"></script>
<script src="/js/main/ng-selfPerfEval-app.js"></script>

</body>
</html>
