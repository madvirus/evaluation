<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="mainApp">
<head>
    <meta charset="utf-8">
    <title>평가 시스템</title>
    <jsp:include page="/WEB-INF/view/main/common/css.jsp"/>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/common/navi.jsp" />

<div class="container">
    <c:if test="${empty evalSeasons}">
        <div class="alert alert-warning" role="alert">
            아직 진행중인 평가가 존재하지 않습니다.
        </div>
    </c:if>
    <c:if test="${! empty evalSeasons}">
        <h2>진행중인 평가</h2>
        <ul>
            <c:forEach var="eval" items="${evalSeasons}">
                <li><a href="/main/evalseasons/${eval.id}">${eval.name}</a></li>
            </c:forEach>
        </ul>
    </c:if>

    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>

<jsp:include page="/WEB-INF/view/main/common/script.jsp" />

</body>
</html>
