<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>평가 시스템</title>
    <jsp:include page="/WEB-INF/view/main/common/css.jsp"/>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/common/navi.jsp" />

<div class="container">
    <div class="alert alert-warning" role="alert">
        페이지가 존재하지 않습니다.
    </div>

    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>

<jsp:include page="/WEB-INF/view/main/common/script.jsp" />

</body>
</html>
