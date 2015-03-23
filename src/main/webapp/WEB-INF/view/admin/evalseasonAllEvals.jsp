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
        .personalEval {
            border-bottom: solid dimgrey;
            margin-bottom: 60px;
        }

        @media print {
            .personalEval {
                page-break-before: always;
            }
            .pagebreak {
                page-break-before: always;
            }
        }
    </style>
</head>
<body>

<jsp:include page="admin-navi.jsp"/>

<div class="container" ng-controller="allEvalsCtrl">
    <h1>${evalSeason.name} 평가 내용</h1>

    <c:forEach var="personalEval" items="${allEvals}">
        <div class="personalEval">
            <div class="page-header">
                <h1>${personalEval.ratee.name}(${personalEval.ratee.id})
                    <small>${evalSeason.name}</small>
                </h1>
            </div>
            <c:choose>
                <c:when test="${!personalEval.selfEvalDone}">
                    <div class="alert alert-warning" role="alert">
                        본인 평가를 완료하지 않았습니다.
                    </div>
                </c:when>
                <c:when test="${!personalEval.firstTotalEvalDone && !personalEval.secondTotalEvalDone}">
                    <div class="alert alert-warning" role="alert">
                        1차 평가와 2차 평가를 완료하지 않았습니다.
                    </div>
                </c:when>
                <c:when test="${!personalEval.secondTotalEvalDone}">
                    <div class="alert alert-warning" role="alert">
                        아직 2차 평가를 완료하지 않았습니다.
                    </div>
                </c:when>
            </c:choose>

            <h3>${personalEval.ratee.name} 성과 평가 내용</h3>
            <table class="table table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th class="col-md-1">구분</th>
                    <th class="col-md-2">목표항목</th>
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
                        <td rowspan="3">${itemAndEval.item.category}</td>
                        <td rowspan="3">${itemAndEval.item.goalType}</td>
                        <td rowspan="3"><tf:pre value='${itemAndEval.item.result}'/></td>
                        <td rowspan="3">${itemAndEval.item.weight}</td>
                        <td>피평가자</td>
                        <td><tf:pre value='${itemAndEval.selfEval.comment}'/></td>
                        <td>${itemAndEval.selfEval.grade}</td>
                    </tr>
                    <tr>
                        <td>1차 평가자</td>
                        <td><tf:pre value="${itemAndEval.firstEval.comment}"/></td>
                        <td>${itemAndEval.firstEval.grade}</td>
                    </tr>
                    <tr>
                        <td>2차 평가자</td>
                        <td>-</td>
                        <td>${itemAndEval.secondEval.grade}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <table class="table table-bordered table-condensed">
                <tbody>
                <tr>
                    <td class="col-md-2 success">1차 성과 종합 의견/평가</td>
                    <td class="col-md-6">
                        <tf:pre value="${personalEval.firstPerfTotalEval.comment}"/>
                    </td>
                    <td class="col-md-1">${personalEval.firstPerfTotalEval.grade}</td>
                    <td class="col-md-3"></td>
                </tr>
                <tr>
                    <td class="col-md-2 success">2차 성과 종합 의견/평가</td>
                    <td class="col-md-6">
                        <tf:pre value="${personalEval.secondPerfTotalEval.comment}"/>
                    </td>
                    <td class="col-md-1">${personalEval.secondPerfTotalEval.grade}</td>
                    <td class="col-md-3"></td>
                </tr>
                </tbody>
            </table>
            <div class="pagebreak"></div>
            <h3>${personalEval.ratee.name} 역량 평가 내용</h3>
            <table class="table table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th rowspan="2" class="col-md-1">역량</th>
                    <th colspan="3" class="col-md-2">의견</th>
                    <th colspan="2">1차 평가</th>
                    <th>2차 평가</th>
                </tr>
                <tr class="success">
                    <th class="col-md-1">평가자</th>
                    <th class="col-md-2">의견</th>
                    <th class="col-md-1">등급</th>
                    <th class="col-md-2">의견</th>
                    <th class="col-md-1">등급</th>
                    <th class="col-md-1">등급</th>
                </tr>
                </thead>
                <tbody>
                <tr class="success">
                    <th colspan="7">공통</th>
                </tr>
                <c:set var="allCompeEvals" value="${personalEval.allCompeEvals}"/>
                <c:forEach var="idx" begin="0" end="4">
                    <tr>
                        <td rowspan="2">{{competencyItems.common[${idx}].name}}</td>
                        <td>본인</td>
                        <td>
                            <tf:pre value="${allCompeEvals.selfEvalSet.commonsEvals[idx].comment}"/>
                        </td>
                        <td>${allCompeEvals.selfEvalSet.commonsEvals[idx].grade}</td>
                        <td rowspan="2">
                            <tf:pre value="${allCompeEvals.firstEvalSet.commonsEvals[idx].comment}"/>
                        </td>
                        <td rowspan="2">${allCompeEvals.firstEvalSet.commonsEvals[idx].grade}</td>
                        <td rowspan="2">
                                ${allCompeEvals.secondEvalSet.commonsEvals[idx].grade}
                        </td>
                    </tr>
                    <tr>
                        <td>동료 평가자</td>
                        <td>
                            <ul class="list-unstyled">
                                <c:forEach var="collEvalSet" items="${allCompeEvals.colleagueEvals}">
                                    <c:if test="${collEvalSet.done}">
                                        <li>- <tf:pre value="${collEvalSet.commonsEvals[idx].comment}"/></li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </td>
                        <td>
                            <fmt:formatNumber pattern="#.00" value="${allCompeEvals.getColleagueCommonGradeAvg(idx)}"/>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${personalEval.rateeType.hasLeadership()}">
                    <tr class="success">
                        <th colspan="7">리더십</th>
                    </tr>

                    <c:forEach var="idx" begin="0" end="4">
                        <tr>
                            <td rowspan="2">{{competencyItems.leadership[${idx}].name}}</td>
                            <td>본인</td>
                            <td>
                                <tf:pre value="${allCompeEvals.selfEvalSet.leadershipEvals[idx].comment}"/>
                            </td>
                            <td>${allCompeEvals.selfEvalSet.leadershipEvals[idx].grade}</td>
                            <td rowspan="2">${allCompeEvals.firstEvalSet.leadershipEvals[idx].comment}</td>
                            <td rowspan="2">${allCompeEvals.firstEvalSet.leadershipEvals[idx].grade}</td>
                            <td rowspan="2">${allCompeEvals.secondEvalSet.leadershipEvals[idx].grade}</td>
                        </tr>
                        <tr>
                            <td>동료 평가자</td>
                            <td>
                                <ul class="list-unstyled">
                                    <c:forEach var="collEvalSet" items="${allCompeEvals.colleagueEvals}">
                                        <c:if test="${collEvalSet.done}">
                                            <li>- <tf:pre value="${collEvalSet.leadershipEvals[idx].comment}"/></li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </td>
                            <td>
                                <fmt:formatNumber pattern="#.00"
                                                  value="${allCompeEvals.getColleagueLeadershipGradeAvg(idx)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${personalEval.rateeType.hasAm()}">
                    <tr class="success">
                        <th colspan="7">AM</th>
                    </tr>

                    <c:forEach var="idx" begin="0" end="3">
                        <tr ng-form='itemFormA${idx}'>
                            <td rowspan="2">{{competencyItems.am[${idx}].name}}</td>
                            <td>본인</td>
                            <td>
                                <tf:pre value="${allCompeEvals.selfEvalSet.amEvals[idx].comment}"/>
                            </td>
                            <td>${allCompeEvals.selfEvalSet.amEvals[idx].grade}</td>
                            <td rowspan="2">${allCompeEvals.firstEvalSet.amEvals[idx].comment}</td>
                            <td rowspan="2">${allCompeEvals.firstEvalSet.amEvals[idx].grade}</td>
                            <td rowspan="2">
                                    ${allCompeEvals.secondEvalSet.amEvals[idx].grade}
                            </td>
                        </tr>
                        <tr>
                            <td>동료 평가자</td>
                            <td>
                                <ul class="list-unstyled">
                                    <c:forEach var="collEvalSet" items="${allCompeEvals.colleagueEvals}">
                                        <c:if test="${collEvalSet.done}">
                                            <li>- <tf:pre value="${collEvalSet.amEvals[idx].comment}"/></li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </td>
                            <td>
                                <fmt:formatNumber pattern="#.00" value="${allCompeEvals.getColleagueAmGradeAvg(idx)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
            <table class="table table-bordered table-condensed">
                <tbody>
                <tr>
                    <td class="col-md-2 success">동료 종합 의견</td>
                    <td colspan="3">
                        <ul class="list-unstyled">
                            <c:forEach var="collEval" items="${personalEval.allCompeEvals.colleagueEvals}">
                                <li>- ${collEval.totalEval.comment}</li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td class="success">1차 역량 종합 의견/평가</td>
                    <td class="col-md-6">${personalEval.allCompeEvals.firstEvalSet.totalEval.comment}</td>
                    <td class="col-md-1">${personalEval.allCompeEvals.firstEvalSet.totalEval.grade}</td>
                </tr>
                <tr>
                    <td class="col-md-2 success">2차 역량 종합 의견/평가</td>
                    <td class="col-md-6">${personalEval.allCompeEvals.secondEvalSet.totalEval.comment}</td>
                    <td class="col-md-1">${personalEval.allCompeEvals.secondEvalSet.totalEval.grade}</td>
                    <td class="col-md-3">-</td>
                </tr>
                </tbody>
            </table>
            <div class="pagebreak"></div>
            <h3>${personalEval.ratee.name} 종합 평가 내용</h3>

            <table class="table table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th rowspan="2">평가자</th>
                    <th rowspan="2">성과 평가 등급</th>
                    <th colspan="4">2차 역량 평가</th>
                    <th rowspan="2">계산 점수</th>
                    <th rowspan="2" class="col-md-3">종합 의견</th>
                    <th rowspan="2" class="col-md-1">종합 등급</th>
                </tr>
                <tr class="success">
                    <th>공통</th>
                    <th>리더십</th>
                    <th>AM</th>
                    <th>등급</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1차 평가자</td>
                    <c:if test="${!personalEval.firstEvalSkipTarget && personalEval.firstTotalEvalDone}">
                        <td>${personalEval.firstPerfTotalEval.grade}</td>
                        <td>${personalEval.allCompeEvals.firstEvalSet.commonsAverage}</td>
                        <td>${personalEval.allCompeEvals.firstEvalSet.leadershipAverage}</td>
                        <td>${personalEval.allCompeEvals.firstEvalSet.amAverage}</td>
                        <td>${personalEval.firstCompeEvalGrade}</td>
                        <td>${personalEval.firstMark}</td>
                        <td><tf:pre value="${personalEval.firstTotalEval.comment}"/></td>
                        <td>${personalEval.firstTotalEvalGrade}</td>
                    </c:if>
                    <c:if test="${!personalEval.firstTotalEvalDone && !personalEval.firstEvalSkipTarget}">
                        <td colspan="8">1차 평가를 완료하지 않았습니다.</td>
                    </c:if>
                    <c:if test="${personalEval.firstEvalSkipTarget}">
                        <td colspan="8">1차 평가 생략 대상입니다.</td>
                    </c:if>
                </tr>
                <tr>
                    <td>2차 평가자</td>
                    <c:if test="${personalEval.secondTotalEvalDone}">
                        <td>${personalEval.secondPerfTotalEval.grade}</td>
                        <td>${personalEval.allCompeEvals.secondEvalSet.commonsAverage}</td>
                        <td>${personalEval.allCompeEvals.secondEvalSet.leadershipAverage}</td>
                        <td>${personalEval.allCompeEvals.secondEvalSet.amAverage}</td>
                        <td>${personalEval.secondCompeEvalGrade}</td>
                        <td>${personalEval.secondMark}</td>
                        <td><tf:pre value="${personalEval.secondTotalEval.comment}"/></td>
                        <td>${personalEval.secondTotalEvalGrade}</td>
                    </c:if>
                    <c:if test="${!personalEval.secondTotalEvalDone}">
                        <td colspan="8">2차 평가를 완료하지 않았습니다.</td>
                    </c:if>
                </tr>
                </tbody>
            </table>
        </div>
    </c:forEach>

</div>

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>
<script src="/webjars/angularjs/1.3.14/angular.js"></script>
<script src="/js/common/ng-competency-items.js"></script>
<script>
    var mainApp = angular.module('mainApp', ['competency.item']);
    mainApp.controller('allEvalsCtrl',
            ['$scope', 'competencyItemService',
                function ($scope, competencyItemService) {
                    $scope.competencyItems = competencyItemService.getItems();
                }]);

</script>

</body>
</html>
