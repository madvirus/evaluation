<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>평가 시스템</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/layout/navi.jsp"/>

<div class="container">
    <div class="page-header">
        <h2>${evalSeason.name}
            <small>Subtext for header</small>
        </h2>
    </div>

    <%--<c:if test="${evalUserRole.noRole}">--%>
    <%--평가에서 할 일이 없는 사용자--%>
    <%--</c:if>--%>

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
                <li><a href="/main/evalseasons/${evalSeason.id}/selfeval/performance">본인 성과 평가하기</a></li>
                <li><a href=/main/evalseasons/${evalSeason.id}/selfeval/competency>본인 역량 평가하기</a></li>
            </ul>
        </c:if>
    </c:if>

    <c:if test="${evalUserRole.colleagueRaterRole && evalSeason.colleagueEvalutionStarted}">
        <div class="page-header">
            <h3><sec:authentication property="principal.name"/>님의 ${evalSeason.name}동료 평가</h3>
        </div>
        동료 평가 내용
    </c:if>

</div>


<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>

</body>
</html>
