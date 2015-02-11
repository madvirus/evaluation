<%@ page contentType="text/html; charset=UTF-8" session="false" %>
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

<div class="container" ng-controller="firstTotalEvalCtrl" ng-cloak
        ng-init="init(${firstTotalEvalData.evalSummaries.size()})">
    <input type="hidden" ng-model="evalSeasonId" ng-initial value="${evalSeasonId}" />
    <input type="hidden" ng-init="totalEvalAvailable = ${firstTotalEvalData.totalEvalAvailable}" />
    <input type="hidden" ng-init="totalEvalDone = ${firstTotalEvalData.totalEvalDone}" />
    <ol class="breadcrumb">
        <li><a href="/main">홈</a></li>
        <li><a href="/main/evalseasons/${evalSeasonId}">평가 메인</a></li>
        <li class="active">1차 종합 평가</li>
    </ol>
    <div class="page-header">
        <h3>1차 종합 평가</h3>
    </div>

    <div class="alert alert-warning" role="alert" ng-show="!totalEvalAvailable">
        성과 평가와 역량 평가를 모두 작성해야 종합 평가를 할 수 있습니다.
    </div>

    <div class="alert alert-info" role="alert" ng-show="totalEvalDone">
        1차 종합 평가를 완료했습니다.
    </div>

    <form name="totalEvalForm" novalidate>
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2">피평가자</th>
                <th rowspan="2">1차 성과 평가</th>
                <th colspan="4">1차 역량 평가</th>
                <th rowspan="2">계산 점수</th>
                <th colspan="2">1차 종합 평가</th>
            </tr>
            <tr class="success">
                <th>공통</th>
                <th>리더십</th>
                <th>AM</th>
                <th>등급</th>
                <th class="col-md-3">종합 의견</th>
                <th class="col-md-1">종합 등급</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="eval" items="${firstTotalEvalData.evalSummaries}" varStatus="status">
                <tr>
                    <td>${eval.ratee.name}</td>
                    <td>${eval.firstPerfEvalHad ? eval.firstPerfEvalGrade : '평가 입력 전'}</td>
                    <c:if test="${!eval.firstCompeEvalHad}">
                        <td colspan="4">평가 입력 전</td>
                    </c:if>
                    <c:if test="${eval.firstCompeEvalHad}">
                        <td>${eval.firstCompeCommonAvg}</td>
                        <td>${eval.firstCompeLeadershipAvg}</td>
                        <td>${eval.firstCompeAmAvg}</td>
                        <td>${eval.firstCompeEvalGrade}</td>
                    </c:if>
                    <c:if test="${eval.firstPerfEvalHad && eval.firstCompeEvalHad}">
                        <td>${eval.calculateTotalMark()}</td>
                        <td>
                            <input type="hidden" ng-initial ng-model="evalData.totalEvals[${status.index}].rateeId" value="${eval.ratee.id}">
                            <textarea ng-model="evalData.totalEvals[${status.index}].comment"
                                      class="form-control input-sm msd-elastic: \n;"
                                      name="totalEvalComment"
                                      ng-initial
                                      ng-readonly="readonly()"
                                      required>${eval.firstTotalEval.comment}</textarea>
                        </td>
                        <td>
                            <select ng-model="evalData.totalEvals[${status.index}].grade"
                                    ng-initial
                                    ng-readonly="readonly()"
                                    name="totalEvalGrade"
                                    class="form-control input-sm">
                                <option value="S" <c:if test="${eval.firstTotalEval.grade == 'S'}">selected</c:if>>S
                                </option>
                                <option value="A"
                                        <c:if test="${eval.firstTotalEval.grade == 'A' || empty eval.firstTotalEval.grade}">selected="selected"</c:if>>
                                    A
                                </option>
                                <option value="B" <c:if test="${eval.firstTotalEval.grade == 'B'}">selected</c:if>>B
                                </option>
                                <option value="C" <c:if test="${eval.firstTotalEval.grade == 'C'}">selected</c:if>>C
                                </option>
                                <option value="D" <c:if test="${eval.firstTotalEval.grade == 'D'}">selected</c:if>>D
                                </option>
                            </select>
                        </td>
                    </c:if>
                    <c:if test="${!(eval.firstPerfEvalHad && eval.firstCompeEvalHad)}">
                        <td>-</td>
                        <td>-</td>
                        <td>-</td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="9">
                    <button ng-disabled="readonly()" class="btn btn-default" type="button " ng-click="saveDraft()">임시 저장</button>
                    <button ng-disabled="readonly()" class="btn btn-primary" type="button " ng-click="saveDone()">평가 완료</button>
                </td>
            </tr>
            </tfoot>
        </table>
    </form>
    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>

<jsp:include page="/WEB-INF/view/common/dialogTemplate.jsp"/>

<jsp:include page="/WEB-INF/view/main/common/script.jsp"/>
<script src="/js/main/ng-ctrl-firstTotalEval.js"></script>

</body>
</html>
