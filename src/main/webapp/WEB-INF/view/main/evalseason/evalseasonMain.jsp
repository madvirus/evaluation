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

<div class="container">
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
        동료 평가 내용
    </c:if>

    <c:if test="${evalUserRole.firstRaterRole}">
        <div class="page-header">
            <h3><sec:authentication property="principal.name"/>님의 1차 평가</h3>
        </div>
        <table class="table table-bordered table-condensed">
            <thead>
            <tr class="success">
                <th rowspan="2">평가 대상자</th>
                <th colspan="2">대상자의 본인 평가 진행 상태</th>
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
            <c:forEach var="userState" items="${firstRateeEvalStates}">
                <tr>
                    <td>${userState.user.name}</td>
                    <td>
                        <c:if test="${!userState.state.started}">
                            평가 시작 전
                        </c:if>
                        <c:if test="${userState.state.started}">
                            <c:if test="${userState.state.selfPerfEvalDone}">
                                <span class="label label-success">평가 완료</span>
                            </c:if>
                            <c:if test="${!userState.state.selfPerfEvalDone}">
                                평과 진행 중
                            </c:if>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${!userState.state.started}">
                            평가 시작 전
                        </c:if>
                        <c:if test="${userState.state.started}">
                            <c:if test="${userState.state.selfCompeEvalDone}">
                                <span class="label label-success">평가 완료</span>
                            </c:if>
                            <c:if test="${!userState.state.selfCompeEvalDone}">
                                평과 진행 중
                            </c:if>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${!userState.state.selfPerfEvalDone}">
                            본인 성과 평가가 완료되지 않아, 1차 평가를 할 수 없습니다.
                        </c:if>
                        <c:if test="${userState.state.selfPerfEvalDone}">
                            <c:if test="${!userState.state.firstPerfEvalHad}">
                                1차 평가 전
                            </c:if>
                            <c:if test="${userState.state.firstPerfEvalHad}">
                                [${userState.state.firstPerfEvalGrade}]
                            </c:if>
                            <a href="/main/evalseasons/${evalSeason.id}/firsteval/${userState.user.id}/performance">
                                <c:if test="${userState.state.firstTotalEvalDone}">
                                    평가 내용보기
                                </c:if>
                                <c:if test="${!userState.state.firstTotalEvalDone}">
                                    평가 입력/수정하기
                                </c:if>
                            </a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${!userState.state.selfCompeEvalDone}">
                            본인 역량 평가가 완료되지 않아, 1차 평가를 할 수 없습니다.
                        </c:if>
                        <c:if test="${userState.state.selfCompeEvalDone}">
                            <c:if test="${!userState.state.firstCompeEvalHad}">
                                1차 평가 전
                            </c:if>
                            <c:if test="${userState.state.firstCompeEvalHad}">
                                [${userState.state.firstCompeEvalGrade}]
                            </c:if>
                            <a href="/main/evalseasons/${evalSeason.id}/firsteval/${userState.user.id}/competency">
                                <c:if test="${userState.state.firstTotalEvalDone}">
                                    평가 내용보기
                                </c:if>
                                <c:if test="${!userState.state.firstTotalEvalDone}">
                                    평가 입력/수정하기
                                </c:if>
                            </a>
                        </c:if>
                    </td>
                    <td>
                        ${userState.state.firstTotalEvalGrade}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="6">
                        <a class="btn btn-primary btn-sm" href="/main/evalseasons/${evalSeason.id}/firsttotaleval">종합 평가 하기</a>
                    </td>
                </tr>
            </tfoot>
        </table>

    </c:if>

    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>


<jsp:include page="/WEB-INF/view/main/common/script.jsp" />

</body>
</html>
