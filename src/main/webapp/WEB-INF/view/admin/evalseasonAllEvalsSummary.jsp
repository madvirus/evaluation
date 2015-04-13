<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html ng-app="mainApp">
<head>
    <jsp:include page="/WEB-INF/view/main/common/metatag.jsp"/>
    <title>전체 평가 내용</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
    <style>
        table {font-size: 10pt;}

        @media print {
        }
    </style>
</head>
<body>

<jsp:include page="admin-navi.jsp"/>

<div class="container">
    <h1>${evalSeason.name} 평가 내용 요약</h1>

    <table class="table table-bordered table-condensed">
        <thead>
            <tr>
                <th rowspan="2">피평가자</th>
                <th rowspan="2">상태</th>
                <th colspan="3">1차 평가</th>
                <th colspan="3">2차 평가</th>
            </tr>
            <tr>
                <th>성과</th>
                <th>역량</th>
                <th>종합</th>
                <th>성과</th>
                <th>역량</th>
                <th>종합</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="personalEval" items="${allEvals}">
                <tr>
                    <td>${personalEval.ratee.name}(${personalEval.ratee.id})</td>
                    <td>
                        <c:choose>
                            <c:when test="${!personalEval.selfEvalDone}">
                                본인 평가 미완료
                            </c:when>
                            <c:when test="${!personalEval.firstTotalEvalDone && !personalEval.firstEvalSkipTarget}">
                                1차 평가 미완료
                            </c:when>
                            <c:when test="${!personalEval.secondTotalEvalDone}">
                                2차 평가 미완료
                            </c:when>
                        </c:choose>
                    </td>

                    <c:if test="${personalEval.firstEvalSkipTarget}">
                        <td colspan="3">1차 평가 생략 대상</td>
                    </c:if>
                    <c:if test="${!personalEval.firstEvalSkipTarget && personalEval.firstTotalEvalDone}">
                        <td><strong>${personalEval.firstPerfTotalEval.grade}</strong></td>
                        <td><strong>${personalEval.allCompeEvals.firstEvalSet.totalEval.grade}</strong></td>
                        <td><strong>${personalEval.firstTotalEvalGrade}</strong></td>
                    </c:if>

                    <c:if test="${!personalEval.firstTotalEvalDone && !personalEval.firstEvalSkipTarget}">
                        <td><small>${personalEval.firstPerfTotalEval.grade}</small></td>
                        <td><small>${personalEval.allCompeEvals.firstEvalSet.totalEval.grade}</small></td>
                        <td><small>${personalEval.firstTotalEvalGrade}</small></td>
                    </c:if>

                    <c:if test="${personalEval.secondTotalEvalDone}">
                        <td><strong>${personalEval.firstPerfTotalEval.grade}</strong></td>
                        <td><strong>${personalEval.allCompeEvals.firstEvalSet.totalEval.grade}</strong></td>
                        <td><strong>${personalEval.firstTotalEvalGrade}</strong></td>
                    </c:if>
                    <c:if test="${!personalEval.secondTotalEvalDone}">
                        <td><small>${personalEval.secondPerfTotalEval.grade}</small></td>
                        <td><small>${personalEval.allCompeEvals.secondEvalSet.totalEval.grade}</small></td>
                        <td><small>${personalEval.secondTotalEvalGrade}</small></td>
                    </c:if>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</div>

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>

</body>
</html>
