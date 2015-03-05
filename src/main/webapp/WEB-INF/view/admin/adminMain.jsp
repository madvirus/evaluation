<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/view/main/common/metatag.jsp" />
    <title>관리도구</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
</head>
<body>

<jsp:include page="admin-navi.jsp" />

<div class="container">
    <ul>
        <sec:authorize access="hasAuthority('ROLE_HRADMIN')">
        <li><a href="/admin/evalseasons">'평가 관리'</a>를 이용해서 평가를 생성하고 관리하세요.</li>
        </sec:authorize>
        <sec:authorize access="hasAuthority('ROLE_SYSTEMADMIN')">
        <li><a href="/admin/system">'시스템 관리'</a>를 이용해서 권한을 관리하세요.</li>
        </sec:authorize>
    </ul>
</div>

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>

</body>
</html>
