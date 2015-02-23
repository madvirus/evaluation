<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="mainApp">
<head>
    <meta charset="utf-8">
    <title>평가 시스템</title>
    <jsp:include page="/WEB-INF/view/main/common/css.jsp"/>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/common/navi.jsp"/>

<div class="container" ng-controller="mainCtrl" ng-cloak>
    <div class="page-header">
        <h2>${evalSeason.name}</h2>
    </div>

    <c:if test="${evalUserRole.noRole}">
        <div class="alert alert-warning" role="alert">
            [${evalSeason.name}]의 피평가자나 평가자가 아닙니다.
        </div>
    </c:if>

    <c:if test="${evalUserRole.rateeRole}">
        <div class="page-header">
            <h3><sec:authentication property="principal.name"/>님의 ${evalSeason.name}본인 평가</h3>
        </div>
        <c:if test="${!myPersonalEval.started}">
            <div class="alert alert-warning" role="alert">
                아직 본인 평가를 시작하지 않았습니다. <em>본인 평가를 시작하세요.</em>
            </div>
            <p>
                <a class="btn btn-default btn-xs" href="/main/evalseasons/${evalSeason.id}/selfeval/performance"
                   role="button">본인 성과 평가 시작하기</a>
                <a class="btn btn-default btn-xs" href="/main/evalseasons/${evalSeason.id}/selfeval/competency"
                   role="button">본인 역량 평가 시작하기</a>
            </p>
        </c:if>
        <c:if test="${myPersonalEval.started}">
            <c:if test="${myPersonalEval.selfPerfEvalDone && myPersonalEval.selfCompeEvalDone}">
                <div ng-init="selfCollapsed = true">본인 평가를 완료했습니다.
                    <a href="" ng-click="toggleSelfState()">{{selfCollapsed ? '[펼쳐보기]' : '[감추기]'}}</a>
                </div>
            </c:if>
            <div collapse="selfCollapsed">
                <ul>
                    <li>
                        <c:if test="${myPersonalEval.selfPerfEvalDone}">
                        <span class="label label-success">본인 성과 평가 완료</span>
                        <a href="/main/evalseasons/${evalSeason.id}/selfeval/performance">본인 성과 평가 내용보기</a></li>
                    </c:if>
                    <c:if test="${! myPersonalEval.selfPerfEvalDone}">
                        <a href="/main/evalseasons/${evalSeason.id}/selfeval/performance">본인 성과 평가하기</a></li>
                    </c:if>
                    <li>
                        <c:if test="${myPersonalEval.selfCompeEvalDone}">
                            <span class="label label-success">본인 역량 평가 완료</span>
                            <a href=/main/evalseasons/${evalSeason.id}/selfeval/competency>본인 역량 평가 내용보기</a>
                        </c:if>
                        <c:if test="${! myPersonalEval.selfCompeEvalDone}">
                            <a href=/main/evalseasons/${evalSeason.id}/selfeval/competency>본인 역량 평가하기</a>
                        </c:if>
                    </li>
                </ul>
            </div>
        </c:if>
    </c:if>

    <c:if test="${evalUserRole.colleagueRaterRole && evalSeason.colleagueEvalutionStarted}">
        <div class="page-header">
            <h3><sec:authentication property="principal.name"/>님의 동료 평가</h3>
        </div>
        <ul>
            <c:forEach var="collEvalState" items="${colleagueEvalStates}">
                <li>
                        ${collEvalState.ratee.name}:
                    <c:if test="${!collEvalState.personalEvalStarted}">
                        ${collEvalState.ratee.name}님이 본인 평가를 시작한 뒤에 동료 평가를 시작할 수 있습니다.
                    </c:if>

                    <c:if test="${collEvalState.personalEvalStarted}">
                        <c:if test="${collEvalState.colleagueEvalDone}">
                            <span class="label label-success">동료 역량 평가 완료</span>
                            <a href="/main/evalseasons/EVAL2014/colleval/ratees/${collEvalState.ratee.id}">평가 내용 보기</a>
                        </c:if>
                        <c:if test="${!collEvalState.colleagueEvalDone}">
                            <a href="/main/evalseasons/EVAL2014/colleval/ratees/${collEvalState.ratee.id}">평가하러 가기</a>
                        </c:if>
                    </c:if>
                </li>

            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${evalUserRole.firstRaterRole}">
        <c:set var="allTotalEvalDone" value="<%= true %>"/>
        <c:set var="allSelfDone" value="<%= true %>"/>
        <c:set var="anyStarted" value="<%= false %>"/>
        <div class="page-header">
            <h3><sec:authentication property="principal.name"/>님의 1차 평가</h3>
        </div>
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2">평가 대상자</th>
                <th colspan="2">본인 평가 상태</th>
                <th colspan="3">1차 평가 상태</th>
            </tr>
            <tr class="success">
                <th>성과 평가</th>
                <th>역량 평가</th>
                <th>성과 평가</th>
                <th>역량 평가</th>
                <th>종합 평가</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="rateeState" items="${firstRateeEvalStates}">
                <tr>
                    <td>${rateeState.ratee.name}</td>
                    <td>
                        <c:if test="${!rateeState.started}">
                            <c:set var="allSelfDone" value="<%= false %>"/>
                            평가 시작 전
                        </c:if>
                        <c:if test="${rateeState.started}">
                            <c:if test="${rateeState.selfPerfEvalDone}">
                                <span class="label label-success">평가 완료</span>
                            </c:if>
                            <c:if test="${!rateeState.selfPerfEvalDone}">
                                평과 진행 중
                            </c:if>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${!rateeState.started}">
                            평가 시작 전
                        </c:if>
                        <c:if test="${rateeState.started}">
                            <c:if test="${rateeState.selfCompeEvalDone}">
                                <span class="label label-success">평가 완료</span>
                            </c:if>
                            <c:if test="${!rateeState.selfCompeEvalDone}">
                                평과 진행 중
                            </c:if>
                            <c:set var="anyStarted" value="<%= true %>"/>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${!rateeState.selfPerfEvalDone}">
                            본인 성과 평가가 완료되지 않아, 1차 평가를 할 수 없습니다.
                        </c:if>
                        <c:if test="${rateeState.selfPerfEvalDone}">
                            <c:if test="${!rateeState.firstPerfEvalHad}">
                                1차 평가 전
                            </c:if>
                            <c:if test="${rateeState.firstPerfEvalHad}">
                                [${rateeState.firstPerfEvalGrade}]
                            </c:if>
                            <a href="/main/evalseasons/${evalSeason.id}/firsteval/${rateeState.ratee.id}/performance">
                                <c:if test="${rateeState.firstTotalEvalDone}">
                                    평가 내용보기
                                </c:if>
                                <c:if test="${!rateeState.firstTotalEvalDone}">
                                    평가 입력/수정하기
                                </c:if>
                            </a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${!rateeState.selfCompeEvalDone}">
                            본인 역량 평가가 완료되지 않아, 1차 평가를 할 수 없습니다.
                        </c:if>
                        <c:if test="${rateeState.selfCompeEvalDone}">
                            <c:if test="${!rateeState.firstCompeEvalHad}">
                                1차 평가 전
                            </c:if>
                            <c:if test="${rateeState.firstCompeEvalHad}">
                                [${rateeState.firstCompeEvalGrade}]
                            </c:if>
                            <a href="/main/evalseasons/${evalSeason.id}/firsteval/${rateeState.ratee.id}/competency">
                                <c:if test="${rateeState.firstTotalEvalDone}">
                                    평가 내용보기
                                </c:if>
                                <c:if test="${!rateeState.firstTotalEvalDone}">
                                    평가 입력/수정하기
                                </c:if>
                            </a>
                        </c:if>
                    </td>
                    <td>${rateeState.firstTotalEvalGrade}</td>
                </tr>
                <c:if test="${! rateeState.firstTotalEvalDone}">
                    <c:set var="allTotalEvalDone" value="<%= false %>"/>
                </c:if>
            </c:forEach>
            </tbody>
                <tfoot>
                <c:if test="${!allSelfDone}">
                <tr>
                    <td colspan="6">
                        피평가자가 본인 평가를 모두 완료하면 1차 종합 평가를 진행할 수 있습니다.
                    </td>
                </tr>
                </c:if>
                <c:if test="${allSelfDone && anyStarted}">
                <tr>
                    <td colspan="6">
                        <c:if test="${allTotalEvalDone}">
                            <span class="label label-success">1차 평가 완료</span>
                            <a href="/main/evalseasons/${evalSeason.id}/firsttotaleval">종합 평가 내용 보기</a>
                        </c:if>
                        <c:if test="${!allTotalEvalDone}">
                            <a class="btn btn-primary btn-sm" href="/main/evalseasons/${evalSeason.id}/firsttotaleval">종합
                                평가 하기</a>
                        </c:if>
                    </td>
                </tr>
                </c:if>
                </tfoot>
        </table>
    </c:if>

    <c:if test="${evalUserRole.secondRaterRole}">
        <c:set var="allTotalEvalDone" value="<%= true %>"/>
        <c:set var="allFirstEvalDone" value="<%= true %>"/>
        <div class="page-header">
            <h3><sec:authentication property="principal.name"/>님의 2차 평가</h3>
        </div>
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2">평가 대상자</th>
                <th rowspan="2">본인 평가 상태</th>
                <th rowspan="2">1차 평가 상태</th>
                <th colspan="3">2차 평가 상태</th>
            </tr>
            <tr class="success">
                <th>성과 평가</th>
                <th>역량 평가</th>
                <th>종합 평가</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="rateeState" items="${secondRateeEvalStates}">
                <c:if test="${!rateeState.firstTotalEvalDone}">
                    <c:set var="allFirstEvalDone" value="<%= false %>"/>
                </c:if>
                <tr>
                    <td>${rateeState.ratee.name}</td>
                    <td>
                        <c:if test="${!rateeState.started}">
                            평가 시작 전
                            <c:set var="allStarted" value="<%= false %>"/>
                        </c:if>
                        <c:if test="${rateeState.selfPerfEvalDone && rateeState.selfCompeEvalDone}">
                            <span class="label label-success">본인 평가 완료</span>
                        </c:if>
                        <c:if test="${!rateeState.selfPerfEvalDone || !rateeState.selfCompeEvalDone}">
                            본인 평가 진행 중
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${rateeState.started && rateeState.firstEvalSkipTarget}">
                            1차 평가 생략 대상
                        </c:if>
                        <c:if test="${!rateeState.firstEvalSkipTarget}">
                            <c:if test="${!rateeState.firstPerfEvalHad && !rateeState.firstCompeEvalHad}">
                                1차 평가 시작 전
                            </c:if>
                            <c:if test="${!rateeState.firstTotalEvalDone && (rateeState.firstPerfEvalHad || rateeState.firstCompeEvalHad)}">
                                1차 평가 진행 중
                            </c:if>
                            <c:if test="${rateeState.firstTotalEvalDone}">
                                <span class="label label-success">1차 평가 완료</span>
                            </c:if>
                        </c:if>
                    </td>

                    <td>
                        <c:if test="${!rateeState.firstTotalEvalDone}">
                            1차 평가 완료 후, 진행 가능합니다.
                        </c:if>
                        <c:if test="${rateeState.firstTotalEvalDone}">
                            <c:if test="${!rateeState.secondPerfEvalHad}">
                                2차 평가 전
                            </c:if>
                            <c:if test="${rateeState.secondPerfEvalHad}">
                                [${rateeState.secondPerfEvalGrade}]
                            </c:if>
                            <a href="/main/evalseasons/${evalSeason.id}/secondeval/${rateeState.ratee.id}/performance">
                                <c:if test="${rateeState.secondTotalEvalDone}">
                                    평가 내용보기
                                </c:if>
                                <c:if test="${!rateeState.secondTotalEvalDone}">
                                    평가 입력/수정하기
                                </c:if>
                            </a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${!rateeState.firstTotalEvalDone}">
                            1차 평가 완료 후, 진행 가능합니다.
                        </c:if>
                        <c:if test="${rateeState.firstTotalEvalDone}">
                            <c:if test="${!rateeState.secondCompeEvalHad}">
                                2차 평가 전
                            </c:if>
                            <c:if test="${rateeState.secondCompeEvalHad}">
                                [${rateeState.secondCompeEvalGrade}]
                            </c:if>
                            <a href="/main/evalseasons/${evalSeason.id}/secondeval/${rateeState.ratee.id}/competency">
                                <c:if test="${rateeState.secondTotalEvalDone}">
                                    평가 내용보기
                                </c:if>
                                <c:if test="${!rateeState.secondTotalEvalDone}">
                                    평가 입력/수정하기
                                </c:if>
                            </a>
                        </c:if>
                    </td>
                    <td>${rateeState.secondTotalEvalGrade}</td>
                </tr>
                <c:if test="${! rateeState.secondTotalEvalDone}">
                    <c:set var="allTotalEvalDone" value="<%= false %>"/>
                </c:if>
            </c:forEach>
            </tbody>
            <c:if test="${allFirstEvalDone}">
                <tfoot>
                <tr>
                    <td colspan="6">
                        <c:if test="${allTotalEvalDone}">
                            <span class="label label-success">2차 평가 완료</span>
                            <a href="/main/evalseasons/${evalSeason.id}/secondtotaleval">종합 평가 내용 보기</a>
                        </c:if>
                        <c:if test="${!allTotalEvalDone}">
                            <a class="btn btn-primary btn-sm" href="/main/evalseasons/${evalSeason.id}/secondtotaleval">종합
                                평가 하기</a>
                        </c:if>
                    </td>
                </tr>
                </tfoot>
            </c:if>
        </table>
    </c:if>

    <jsp:include page="/WEB-INF/view/common/footer.jsp"/>
</div>


<jsp:include page="/WEB-INF/view/main/common/script.jsp"/>
<script src="/js/main/ng-ctrl-main.js"></script>
</body>
</html>
