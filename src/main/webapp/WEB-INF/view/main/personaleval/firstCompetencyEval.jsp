<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<c:set var="allCompeEvals" value="${personalEval.allCompeEvals}" />
<div class="container" ng-controller="firstCompeEvalCtrl" ng-cloak>
    <input type="hidden" ng-model="evalSeasonId" ng-initial value="${personalEval.evalSeasonId}"/>
    <input type="hidden" ng-model="rateeId" ng-initial value="${personalEval.ratee.id}"/>
    <input type="hidden" ng-init="hasLeadership = ${personalEval.rateeType.hasLeadership()}"/>
    <input type="hidden" ng-init="hasAm = ${personalEval.rateeType.hasAm()}"/>
    <input type="hidden" ng-init="selfEvalDone = ${personalEval.selfCompeEvalDone}" />
    <input type="hidden" ng-init="firstTotalEvalDone = ${personalEval.firstTotalEvalDone}" />
    <c:set var="firstEvalSetJson"><tf:toJson value="${firstEvalSet}"/></c:set>
    <input type="hidden" ng-init="evalSet = <c:out value="${firstEvalSetJson}" escapeXml="true"/>" />

    <ol class="breadcrumb">
        <li><a href="/main">홈</a></li>
        <li><a href="/main/evalseasons/${personalEval.evalSeasonId}">평가 메인</a></li>
        <li class="active">1차 역량 평가</li>
    </ol>

    <div class="page-header">
        <h3>1차 역량 평가
            <small>평가 대상자 - ${personalEval.ratee.name}</small>
        </h3>
    </div>

    <p ng-show="selfEvalDone && !firstTotalEvalDone">
        <button type="button" class="btn btn-danger" ng-click="reject()">반려하기</button>
        <span class="help-block text-warning">반려를 하면 모든 입력한 내용이 삭제되므로, 1차 평가 입력전에 반려를 하시기 바랍니다.</span>
    </p>

    <div class="alert alert-warning" role="alert" ng-show="!selfEvalDone">
        평가 대상가자 아직 본인 역량 평가를 완료하지 않았습니다.
    </div>

    <div class="alert alert-warning" role="alert" ng-show="firstTotalEvalDone">
        1차 종합 평가를 완료했습니다.
    </div>

    <form novalidate name="compeEvalForm">
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2" class="col-md-1">역량</th>
                <th rowspan="2" class="col-md-3">역량정의 및 행동 지표 | <a href="" ng-click="descCollapsed = !descCollapsed">{{descCollapsed ? '내용보기' : '내용감추기'}}</a></th>
                <th rowspan="2" class="col-md-2">의견</th>
                <th rowspan="2" class="col-md-1">등급</th>
                <th colspan="2">1차 평가</th>
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
                <th colspan="6">공통</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="idx" begin="0" end="4">
                <tr ng-form='itemForm${idx}'>
                    <td rowspan="2" class="col-md-1">{{competencyItems.common[${idx}].name}}</td>
                    <td rowspan="2" class="col-md-3">
                        <span ng-show="descCollapsed">내용 숨기</span>
                        <div collapse="descCollapsed">
                            <small ng-bind-html="competencyItems.common[${idx}].description | nl2br"></small>
                        </div>
                    </td>
                    <td class="col-md-2">
                        <tf:pre value="${allCompeEvals.selfEvalSet.commonsEvals[idx].comment}"/>
                    </td>
                    <td class="col-md-1">${allCompeEvals.selfEvalSet.commonsEvals[idx].grade}</td>
                    <td rowspan="2" class="col-md-3" ng-class="{'has-error': showError && !itemForm${idx}.comment.$valid}">
                        <textarea ng-model="evalSet.commonsEvals[${idx}].comment"
                                  ng-readonly="readonly()"
                                  class="form-control input-sm msd-elastic: \n;"
                                  name="comment"
                                  required></textarea>
                    </td>
                    <td rowspan="2" class="col-md-1" ng-class="{'has-error': showError && !itemForm${idx}.grade.$valid}">
                        <select ng-model="evalSet.commonsEvals[${idx}].grade"
                                ng-readonly="readonly()"
                                class="form-control input-sm"
                                ng-options="grade for grade in grades"
                                name="grade" required>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="col-md-2">
                        <ul class="list-unstyled">
                        <c:forEach var="collEvalSet" items="${allCompeEvals.colleagueEvals}">
                            <c:if test="${collEvalSet.done}">
                                <li>- <tf:pre value="${collEvalSet.commonsEvals[idx].comment}"/></li>
                            </c:if>
                        </c:forEach>
                        </ul>
                    </td>
                    <td class="col-md-1">
                        <fmt:formatNumber pattern="#.00" value="${allCompeEvals.getColleagueCommonGradeAvg(idx)}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${personalEval.rateeType.hasLeadership()}">
            <table class="table table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th colspan="6">리더십</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="idx" begin="0" end="4">
                    <tr ng-form='itemForm${idx}'>
                        <td rowspan="2" class="col-md-1">{{competencyItems.leadership[${idx}].name}}</td>
                        <td rowspan="2" class="col-md-3">
                            <span ng-show="descCollapsed">내용 숨기</span>
                            <div collapse="descCollapsed">
                                <small ng-bind-html="competencyItems.leadership[${idx}].description | nl2br"></small>
                            </div>
                        </td>
                        <td class="col-md-2">
                            <tf:pre value="${allCompeEvals.selfEvalSet.leadershipEvals[idx].comment}"/>
                        </td>
                        <td class="col-md-1">${allCompeEvals.selfEvalSet.leadershipEvals[idx].grade}</td>
                        <td rowspan="2" class="col-md-3" ng-class="{'has-error': showError && !itemForm${idx}.comment.$valid}">
                        <textarea ng-model="evalSet.leadershipEvals[${idx}].comment"
                                  ng-readonly="readonly()"
                                  class="form-control input-sm msd-elastic: \n;"
                                  name="comment"
                                  required></textarea>
                        </td>
                        <td rowspan="2" class="col-md-1" ng-class="{'has-error': showError && !itemForm${idx}.grade.$valid}">
                            <select ng-model="evalSet.leadershipEvals[${idx}].grade"
                                    ng-readonly="readonly()"
                                    ng-options="grade for grade in grades"
                                    class="form-control input-sm"
                                    name="grade" required>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-md-2">
                            <ul class="list-unstyled">
                            <c:forEach var="collEvalSet" items="${allCompeEvals.colleagueEvals}">
                                <c:if test="${collEvalSet.done}">
                                    <li>- <tf:pre value="${collEvalSet.leadershipEvals[idx].comment}"/></li>
                                </c:if>
                            </c:forEach>
                            </ul>
                        </td>
                        <td class="col-md-1">
                            <fmt:formatNumber pattern="#.00" value="${allCompeEvals.getColleagueLeadershipGradeAvg(idx)}"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>


        <c:if test="${personalEval.rateeType.hasAm()}">
            <table class="table table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th colspan="6">AM</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="idx" begin="0" end="3">
                    <tr ng-form='itemForm${idx}'>
                        <td rowspan="2" class="col-md-1">{{competencyItems.am[${idx}].name}}</td>
                        <td rowspan="2" class="col-md-3">
                            <span ng-show="descCollapsed">내용 숨기</span>
                            <div collapse="descCollapsed">
                                <small ng-bind-html="competencyItems.am[${idx}].description | nl2br"></small>
                            </div>
                        </td>
                        <td class="col-md-2">
                            <tf:pre value="${allCompeEvals.selfEvalSet.amEvals[idx].comment}"/>
                        </td>
                        <td class="col-md-1">${allCompeEvals.selfEvalSet.amEvals[idx].grade}</td>
                        <td rowspan="2" class="col-md-3" ng-class="{'has-error': showError && !itemForm${idx}.comment.$valid}">
                        <textarea ng-model="evalSet.amEvals[${idx}].comment"
                                  ng-readonly="readonly()"
                                  class="form-control input-sm msd-elastic: \n;"
                                  name="comment"
                                  required></textarea>
                        </td>
                        <td rowspan="2" class="col-md-1" ng-class="{'has-error': showError && !itemForm${idx}.grade.$valid}">
                            <select ng-model="evalSet.amEvals[${idx}].grade"
                                    ng-readonly="readonly()"
                                    ng-options="grade for grade in grades"
                                    class="form-control input-sm"
                                    name="grade" required>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-md-2">
                            <ul class="list-unstyled">
                            <c:forEach var="collEvalSet" items="${allCompeEvals.colleagueEvals}">
                                <c:if test="${collEvalSet.done}">
                                    <li>- <tf:pre value="${collEvalSet.amEvals[idx].comment}"/></li>
                                </c:if>
                            </c:forEach>
                            </ul>
                        </td>
                        <td class="col-md-1">
                            <fmt:formatNumber pattern="#.00" value="${allCompeEvals.getColleagueAmGradeAvg(idx)}"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <table class="table table-bordered table-condensed">
            <tbody>
            <tr>
                <td class="col-md-2 success">동료 종합 평가</td>
                <td colspan="3">
                    <ul class="list-unstyled">
                        <c:forEach var="collEval" items="${personalEval.allCompeEvals.colleagueEvals}">
                            <li>- ${collEval.totalEval.comment}</li>
                        </c:forEach>
                    </ul>
                </td>
            </tr>
            <tr>
                <td class="col-md-2 success">1차 역량 종합 평가</td>
                <td class="col-md-6" ng-class="{'has-error': showError && !compeEvalForm.firstCompeEvalComment.$valid}">
                    <textarea ng-model="evalSet.totalEval.comment"
                              class="form-control input-sm msd-elastic: \n;"
                              name="firstCompeEvalComment"
                              ng-readonly="readonly()"
                              required></textarea>
                </td>
                </td>
                <td class="col-md-1">
                    <select ng-model="evalSet.totalEval.grade"
                            ng-options="grade for grade in grades"
                            ng-readonly="readonly()"
                            class="form-control input-sm">
                    </select>
                </td>
                <td class="col-md-3"></td>
            </tr>
            </tbody>
        </table>
        <table class="table table-bordered table-condensed" ng-show="selfEvalDone">
            <tfoot>
            <tr ng-show="showError && !compeEvalForm.$valid">
                <td colspan="7">
                    <div class="alert alert-danger" role="alert">
                        <ul>
                            <li ng-show="!compeEvalForm.$valid">평가를 저장하려면, 모든 항목을 입력해야 합니다.</li>
                        </ul>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <button class="btn btn-default btn-primary" type="button " ng-click="save()">평가 저장</button>
                </td>
            </tr>
            </tfoot>
        </table>
    </form>

    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>


<jsp:include page="/WEB-INF/view/common/dialogTemplate.jsp"/>

<jsp:include page="/WEB-INF/view/main/common/script.jsp"/>
<script src="/js/main/ng-ctrl-firstCompeEval.js"></script>

</body>
</html>