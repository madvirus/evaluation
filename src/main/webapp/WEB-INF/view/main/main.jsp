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

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="/main">
                평가 홈
            </a>
        </div>

        <div>
            <ul class="nav navbar-nav">
                <sec:authorize url="/admin">
                    <li><a href="/admin">관리자</a></li>
                </sec:authorize>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="/logout"><sec:authentication property="principal.name"/> 로그아웃</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <c:if test="${empty evalSeasons}">
        <div class="alert alert-warning" role="alert">
            아직 진행중인 평가가 존재하지 않습니다.
        </div>
    </c:if>
    <c:if test="${! empty evalSeasons}">
        진행중인 평가:
        <ul>
            <c:forEach var="eval" items="${evalSeasons}">
                <li><a href="/main/evalseasons/${eval.id}">${eval.name}</a></li>
            </c:forEach>
        </ul>
    </c:if>
</div>

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>

</body>
</html>
