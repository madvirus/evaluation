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

<jsp:include page="/WEB-INF/view/main/layout/navi.jsp" />

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
