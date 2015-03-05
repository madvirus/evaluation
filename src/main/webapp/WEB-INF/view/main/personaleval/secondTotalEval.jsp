<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

<div class="container" ng-controller="secondTotalEvalCtrl" ng-cloak
        ng-init="init(${secondTotalEvalData.evalSummaries.size()})">
    <input type="hidden" ng-model="evalSeasonId" ng-initial value="${evalSeasonId}" />
    <input type="hidden" ng-init="totalEvalAvailable = ${secondTotalEvalData.totalEvalAvailable}" />
    <input type="hidden" ng-init="totalEvalDone = ${secondTotalEvalData.totalEvalDone}" />
    <ol class="breadcrumb">
        <li><a href="/main">홈</a></li>
        <li><a href="/main/evalseasons/${evalSeasonId}">평가 메인</a></li>
        <li class="active">2차 종합 평가</li>
    </ol>
    <div class="page-header">
        <h3>2차 종합 평가</h3>
    </div>

    <div class="alert alert-warning" role="alert" ng-show="!totalEvalAvailable">
        성과 평가와 역량 평가를 모두 작성해야 종합 평가를 할 수 있습니다.
    </div>

    <div class="alert alert-info" role="alert" ng-show="totalEvalDone">
        2차 종합 평가를 완료했습니다.
    </div>

    <form name="totalEvalForm" novalidate>
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2">피평가자</th>
                <th rowspan="2">2차 성과 평가</th>
                <th colspan="4">2차 역량 평가</th>
                <th rowspan="2">계산 점수</th>
                <th colspan="2">2차 종합 평가</th>
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
            <c:forEach var="eval" items="${secondTotalEvalData.evalSummaries}" varStatus="status">
                <tr>
                    <td>${eval.ratee.name}</td>
                    <td>${eval.secondPerfEvalHad ? eval.secondPerfEvalGrade : '평가 입력 전'}</td>
                    <c:if test="${!eval.secondCompeEvalHad}">
                        <td colspan="4">평가 입력 전</td>
                    </c:if>
                    <c:if test="${eval.secondCompeEvalHad}">
                        <td>${eval.secondCompeCommonAvg}</td>
                        <td>${eval.secondCompeLeadershipAvg}</td>
                        <td>${eval.secondCompeAmAvg}</td>
                        <td>${eval.secondCompeEvalGrade}</td>
                    </c:if>
                    <c:if test="${eval.secondPerfEvalHad && eval.secondCompeEvalHad}">
                        <td>${eval.getSecondTotalMark()}</td>
                        <td>
                            <input type="hidden" ng-initial ng-model="evalData.totalEvals[${status.index}].rateeId" value="${eval.ratee.id}">
                            <textarea ng-model="evalData.totalEvals[${status.index}].comment"
                                      class="form-control input-sm msd-elastic: \n;"
                                      name="totalEvalComment"
                                      ng-initial
                                      ng-readonly="readonly()"
                                      required>${eval.secondTotalEval.comment}</textarea>
                        </td>
                        <td>
                            <select ng-model="evalData.totalEvals[${status.index}].grade"
                                    ng-initial
                                    ng-readonly="readonly()"
                                    name="totalEvalGrade"
                                    class="form-control input-sm">
                                <option value="S" <c:if test="${eval.secondTotalEval.grade == 'S'}">selected</c:if>>S
                                </option>
                                <option value="A"
                                        <c:if test="${eval.secondTotalEval.grade == 'A' || empty eval.secondTotalEval.grade}">selected="selected"</c:if>>
                                    A
                                </option>
                                <option value="B" <c:if test="${eval.secondTotalEval.grade == 'B'}">selected</c:if>>B
                                </option>
                                <option value="C" <c:if test="${eval.secondTotalEval.grade == 'C'}">selected</c:if>>C
                                </option>
                                <option value="D" <c:if test="${eval.secondTotalEval.grade == 'D'}">selected</c:if>>D
                                </option>
                            </select>
                        </td>
                    </c:if>
                    <c:if test="${!(eval.secondPerfEvalHad && eval.secondCompeEvalHad)}">
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
<script src="/js/main/ng-ctrl-secondTotalEval.js"></script>

</body>
</html>
