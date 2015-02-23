<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html ng-app="mainApp">
<head>
    <meta charset="utf-8">
    <title>평가 시스템</title>
    <jsp:include page="/WEB-INF/view/main/common/css.jsp"/>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/common/navi.jsp"/>

<div ng-cloak class="container" ng-controller="selfPerfEvalCtrl" ng-init="init()">
    <input type="hidden" ng-init="evalSeasonId = '${selfPerfEvalData.evalSeasonId}'" />
    <input type="hidden" ng-init="selfEvalDone = ${selfPerfEvalData.done}"/>
    <c:set var="itemAndEvalsJson"><tf:toJson value="${selfPerfEvalData.itemAndEvals}"/></c:set>
    <input type="hidden" ng-init="itemAndEvals = <c:out value="${itemAndEvalsJson}" escapeXml="true"/>"/>
    <ol class="breadcrumb">
        <li><a href="/main">홈</a></li>
        <li><a href="/main/evalseasons/${evalSeasonId}">평가 메인</a></li>
        <li class="active">본인 성과 평가</li>
    </ol>

    <div class="page-header">
        <h3>본인 성과 평가
            <small><sec:authentication property="principal.name"/>님</small>
        </h3>
    </div>

    <div ng-show="evalDone()" class="alert alert-info" role="alert">
        본인 성과 평가를 완료했습니다.
    </div>

    <p ng-show="!evalDone()">
        <button class="btn btn-default btn-xs" type="button" ng-click="addRow()">추가</button>
        <button class="btn btn-default btn-xs" type="button" ng-click="removeRow()">삭제</button>
    </p>

    <form novalidate name="perfEvalForm">
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2" class="col-md-1">선택</th>
                <th rowspan="2" class="col-md-1">구분</th>
                <th rowspan="2" class="col-md-2">목표항목</th>
                <th rowspan="2" class="col-md-3">달성도</th>
                <th rowspan="2" class="col-md-1">가중치</th>
                <th colspan="2">본인 평가</th>
            </tr>
            <tr class="success">
                <th class="col-md-3">의견</th>
                <th class="col-md-1">등급</th>
            </tr>
            </thead>
            <tbody>

            <tr ng-form='itemForm' ng-repeat="itemAndEval in itemAndEvals">
                <td>
                    <input type="checkbox" ng-model="itemSelected[$index]"
                           ng-disabled="evalDone()"
                           ng-readonly="evalDone()" />
                </td>
                <td ng-class="{'has-error': showError && !itemForm.category.$valid}">
                    <input type="text" ng-model="itemAndEval.item.category"
                           class="form-control input-sm"
                           ng-readonly="evalDone()"
                           name="category" required>
                </td>
                <td ng-class="{'has-error': showError && !itemForm.goalType.$valid}">
                    <input type="text" ng-model="itemAndEval.item.goalType"
                           class="form-control input-sm"
                           ng-readonly="evalDone()"
                           name="goalType" required>
                </td>
                <td ng-class="{'has-error': showError && !itemForm.result.$valid}">
                    <textarea ng-model="itemAndEval.item.result"
                              class="form-control input-sm msd-elastic: \n;"
                              ng-readonly="evalDone()"
                              name="result" required></textarea>
                </td>
                <td ng-class="{'has-error': showError && (!itemForm.weight.$valid || itemAndEval.item.weight == 0)}">
                    <input type="number" ng-model="itemAndEval.item.weight"
                           class="form-control input-sm"
                           ng-readonly="evalDone()"
                           name="weight" min="0" max="100" required>
                </td>
                <td ng-class="{'has-error': showError && !itemForm.comment.$valid}">
                    <textarea ng-model="itemAndEval.eval.comment"
                              class="form-control input-sm msd-elastic: \n;"
                              ng-readonly="evalDone()"
                              name="comment" required></textarea>
                </td>
                <td>
                    <select ng-model="itemAndEval.eval.grade" ng-options="grade for grade in grades"
                            ng-readonly="evalDone()"
                            class="form-control input-sm">
                    </select>
                </td>
            </tr>
            </tbody>
            <tfoot>
            <tr ng-show="showError">
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
            <tr ng-show="!evalDone()">
                <td colspan="7">
                    <button class="btn btn-default" type="button" ng-click="saveDraft()">임시저장</button>
                    <button class="btn btn-default btn-primary" type="button " ng-click="done()">본인 성과 평가 완료</button>
                </td>
            </tr>
            </tfoot>
        </table>
    </form>
    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>

<jsp:include page="/WEB-INF/view/common/dialogTemplate.jsp"/>

<jsp:include page="/WEB-INF/view/main/common/script.jsp" />
<script src="/js/main/ng-ctrl-selfPerfEval.js"></script>

</body>
</html>
