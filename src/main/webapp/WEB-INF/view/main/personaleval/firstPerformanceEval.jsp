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
<div class="container" ng-controller="firstPerfEvalCtrl" ng-cloak>
    <input type="hidden" ng-model="evalSeasonId" ng-initial value="${personalEval.evalSeasonId}"/>
    <input type="hidden" ng-model="rateeId" ng-initial value="${personalEval.ratee.id}"/>
    <input type="hidden" ng-init="selfEvalDone = ${personalEval.selfPerfEvalDone}" />
    <input type="hidden" ng-init="firstTotalEvalDone = ${personalEval.firstTotalEvalDone}" />
    <c:set var="evalDataJson"><tf:toJson value="${evalData}" /></c:set>
    <input type="hidden" ng-init="evalData = <c:out value="${evalDataJson}"/>" />
    <ol class="breadcrumb">
        <li><a href="/main">홈</a></li>
        <li><a href="/main/evalseasons/${personalEval.evalSeasonId}">평가 메인</a></li>
        <li class="active">1차 성과 평가</li>
    </ol>

    <div class="page-header">
        <h3>1차 성과 평가
            <small>평가 대상자 - ${personalEval.ratee.name}</small>
        </h3>
    </div>

    <p ng-show="selfEvalDone && !firstTotalEvalDone">
        <button type="button" class="btn btn-danger" ng-click="reject()">반려하기</button>
        <span class="help-block text-warning">반려를 하면 모든 입력한 내용이 삭제되므로, 1차 평가 입력전에 반려를 하시기 바랍니다.</span>
    </p>

    <div class="alert alert-warning" role="alert" ng-show="!selfEvalDone">
        평가 대상가자 아직 본인 성과 평가를 완료하지 않았습니다.
    </div>

    <div class="alert alert-info" role="alert" ng-show="firstTotalEvalDone">
        1차 종합 평가를 완료했습니다.
    </div>

    <form novalidate name="perfEvalForm">
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th class="col-md-1">구분</th>
                <th class="col-md-1">목표항목</th>
                <th class="col-md-3">달성도</th>
                <th class="col-md-1">가중치</th>
                <th class="col-md-1">평가자</th>
                <th class="col-md-3">의견</th>
                <th class="col-md-1">등급</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="itemAndEval" items="${personalEval.perfItemAndAllEvals}" varStatus="status">
                <tr>
                    <td rowspan="2">${itemAndEval.item.category}</td>
                    <td rowspan="2">${itemAndEval.item.goalType}</td>
                    <td rowspan="2"><tf:pre value='${itemAndEval.item.result}'/></td>
                    <td rowspan="2" ng-init="addWeight(${itemAndEval.item.weight})">${itemAndEval.item.weight}</td>
                    <td>피평가자</td>
                    <td><tf:pre value='${itemAndEval.selfEval.comment}'/></td>
                    <td>${itemAndEval.selfEval.grade}</td>
                </tr>
                <tr>
                    <td>1차 평가자</td>
                    <td ng-class="{'has-error': showError && !perfEvalForm.comment_${status.index}.$valid}">
                        <textarea ng-model="evalData.itemEvals[${status.index}].comment"
                                  name="comment_${status.index}"
                                  class="form-control input-sm msd-elastic: \n;"
                                  ng-readonly="readonly()"
                                  required></textarea>
                    </td>
                    <td ng-class="{'has-error': showError && !perfEvalForm.grade_${status.index}.$valid}">
                        <select ng-model="evalData.itemEvals[${status.index}].grade"
                                name="grade_${status.index}"
                                ng-options="grade for grade in grades"
                                ng-readonly="readonly()"
                                class="form-control input-sm"
                                required>
                        </select>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <table class="table table-bordered table-condensed">
            <tbody>
            <tr>
                <td class="col-md-2 success">1차 성과 종합 의견/평가</td>
                <td class="col-md-6" ng-class="{'has-error': showError && !perfEvalForm.firstPerfEvalComment.$valid}">
                    <textarea ng-model="evalData.totalEval.comment"
                              class="form-control input-sm msd-elastic: \n;"
                              name="firstPerfEvalComment"
                              ng-readonly="readonly()"
                              required></textarea>
                </td>

                </td>
                <td class="col-md-1" ng-class="{'has-error': showError && !perfEvalForm.firstPerfEvalGrade.$valid}">
                    <select ng-model="evalData.totalEval.grade"
                            name="firstPerfEvalGrade"
                            ng-options="grade for grade in grades"
                            ng-readonly="readonly()"
                            class="form-control input-sm"
                            required>
                    </select>
                </td>
                <td class="col-md-3">계산 평점: {{getMark() | number:2}}</td>
            </tr>
            </tbody>
        </table>
        <table class="table table-bordered table-condensed" ng-show="selfEvalDone">
            <tfoot>
            <tr ng-show="showError && !perfEvalForm.$valid">
                <td colspan="7">
                    <div class="alert alert-danger" role="alert">
                        <ul>
                            <li ng-show="!perfEvalForm.$valid">평가를 저장하려면, 모든 항목을 입력해야 합니다.</li>
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
<script src="/js/main/ng-ctrl-firstPerfEval.js"></script>

</body>
</html>