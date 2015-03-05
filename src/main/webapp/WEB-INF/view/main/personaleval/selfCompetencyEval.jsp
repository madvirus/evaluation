<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html ng-app="mainApp">
<head>
    <jsp:include page="/WEB-INF/view/main/common/metatag.jsp" />
    <title>평가 시스템</title>
    <jsp:include page="/WEB-INF/view/main/common/css.jsp"/>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/common/navi.jsp"/>

<div class="container" ng-cloak ng-controller="selfCompeEvalCtrl" ng-init="init()">
    <input type="hidden" ng-init="evalSeasonId = '${compeEvalData.evalSeasonId}'" />
    <input type="hidden" ng-init="hasLeadership = ${compeEvalData.rateeType.hasLeadership()}"/>
    <input type="hidden" ng-init="hasAm = ${compeEvalData.rateeType.hasAm()}"/>
    <c:set var="evalSetJson"><tf:toJson value="${compeEvalData.evalSet}" /></c:set>
    <input type="hidden" ng-init="evalSet = <c:out value="${evalSetJson}" escapeXml="true"/>" />
    <input type="hidden" ng-init="selfEvalDone = ${compeEvalData.evalSet.done}" >
    <ol class="breadcrumb">
        <li><a href="/main">홈</a></li>
        <li><a href="/main/evalseasons/${evalSeasonId}">평가 메인</a></li>
        <li class="active">본인 역량 평가</li>
    </ol>
    <div class="page-header">
        <h3>본인 역량 평가
            <small><sec:authentication property="principal.name"/>님</small>
        </h3>
    </div>
    <div ng-show="evalDone()" class="alert alert-info" role="alert">
        본인 역량 평가를 완료했습니다.
    </div>

    <form novalidate name="compEvalForm">
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2" class="col-md-1">역량</th>
                <th rowspan="2" class="col-md-4">역량정의 및 행동 지표</th>
                <th colspan="2">본인 평가</th>
            </tr>
            <tr class="success">
                <th class="col-md-3">의견</th>
                <th class="col-md-1">등급</th>
            </tr>
            </thead>
        </table>
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th colspan="4">공통</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-form='itemForm' ng-repeat="itemEval in evalSet.commonsEvals">
                <td class="col-md-1">{{competencyItems.common[$index].name}}</td>
                <td class="col-md-4">
                    <small ng-bind-html="competencyItems.common[$index].description | nl2br"></small>
                </td>
                <td class="col-md-3" ng-class="{'has-error': showError && !itemForm.comment.$valid}">
                    <textarea ng-model="itemEval.comment"
                              ng-readonly="evalDone()"
                              class="form-control input-sm msd-elastic: \n;"
                              name="comment"
                              required></textarea>
                </td>
                <td class="col-md-1" ng-class="{'has-error': showError && !itemForm.grade.$valid}">
                    <select ng-model="itemEval.grade"
                            ng-options="grade for grade in grades"
                            ng-readonly="evalDone()"
                            class="form-control input-sm"
                            name="grade" required>
                    </select>
                </td>
            </tr>
            </tbody>
        </table>
        <table ng-show="hasLeadership" class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th colspan="4">리더십</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-form='itemForm' ng-repeat="itemEval in evalSet.leadershipEvals">
                <td class="col-md-1">{{competencyItems.leadership[$index].name}}</td>
                <td class="col-md-4">
                    <small ng-bind-html="competencyItems.leadership[$index].description | nl2br"></small>
                </td>
                <td class="col-md-3" ng-class="{'has-error': showError && !itemForm.comment.$valid}">
                    <textarea ng-model="itemEval.comment"
                              ng-readonly="evalDone()"
                              class="form-control input-sm msd-elastic: \n;"
                              name="comment"
                              required></textarea>
                </td>
                <td class="col-md-1" ng-class="{'has-error': showError && !itemForm.grade.$valid}">
                    <select ng-model="itemEval.grade"
                            ng-options="grade for grade in grades"
                            ng-readonly="evalDone()"
                            class="form-control input-sm"
                            name="grade" required>
                    </select>
                </td>
            </tr>
            </tbody>
        </table>
        <table ng-show="hasAm" class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th colspan="4">AM</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-form='itemForm' ng-repeat="itemEval in evalSet.amEvals">
                <td class="col-md-1">{{competencyItems.am[$index].name}}</td>
                <td class="col-md-4">
                    <small ng-bind-html="competencyItems.am[$index].description | nl2br"></small>
                </td>
                <td class="col-md-3" ng-class="{'has-error': showError && !itemForm.comment.$valid}">
                    <textarea ng-model="itemEval.comment"
                              ng-readonly="evalDone()"
                              class="form-control input-sm msd-elastic: \n;"
                              name="comment"
                              required></textarea>
                </td>
                <td class="col-md-1" ng-class="{'has-error': showError && !itemForm.grade.$valid}">
                    <select ng-model="itemEval.grade"
                            ng-options="grade for grade in grades"
                            ng-readonly="evalDone()"
                            class="form-control input-sm msd-elastic: \n;"
                            name="grade" required>
                    </select>
                </td>
            </tr>
            </tbody>
        </table>

        <table ng-show="showError" class="table table-bordered table-condensed">
            <tr>
                <td>
                    <div class="alert alert-danger" role="alert">
                        <ul>
                            <li ng-show="!selfCompeEvalCtrl.$valid">평가를 완료하려면 모든 항목을 입력해야 합니다.</li>
                        </ul>
                    </div>
                </td>
            </tr>
        </table>

        <p ng-show="!evalDone()">
            <button class="btn btn-default" type="button" ng-click="saveDraft()">임시저장</button>
            <button class="btn btn-default btn-primary" type="button " ng-click="done()">본인 역량 평가 완료</button>
        </p>
    </form>
    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>

<jsp:include page="/WEB-INF/view/common/dialogTemplate.jsp"/>

<jsp:include page="/WEB-INF/view/main/common/script.jsp" />
<script src="/js/main/ng-ctrl-selfCompeEval.js"></script>

</body>
</html>
