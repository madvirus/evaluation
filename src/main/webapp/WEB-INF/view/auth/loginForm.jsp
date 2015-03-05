<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/view/main/common/metatag.jsp" />
    <title>로그인</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
</head>
<body>
<p>&nbsp;</p>
<div class="container">

    <c:if test='<%= request.getParameter("error") != null %>'>
        <div class="alert alert-danger" role="alert">아이디와 암호가 일치하지 않습니다.</div>
    </c:if>
    <form class="form-inline" method="POST" action="/login">
        <div class="form-group">
            <label for="username">아이디</label>
            <input type="text" class="form-control" id="username" name="username">
        </div>
        <div class="form-group">
            <label for="password">암호</label>
            <input type="password" class="form-control" id="password" name="password">
        </div>
        <button type="submit" class="btn btn-primary">로그인</button>
    </form>

</div>
</body>
</html>
