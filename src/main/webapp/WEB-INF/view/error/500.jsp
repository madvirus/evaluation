<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/WEB-INF/view/main/common/metatag.jsp" />
    <title>평가 시스템</title>
    <jsp:include page="/WEB-INF/view/main/common/css.jsp"/>
</head>
<body>

<jsp:include page="/WEB-INF/view/main/common/navi.jsp" />

<div class="container">
    <div class="alert alert-danger" role="alert">
        요청 처리 중 문제가 발생했습니다. 문제가 계속되면 시스템 관리자에게 문의해주시기 바랍니다.
    </div>

    <jsp:include page="/WEB-INF/view/common/footer.jsp" />
</div>

<jsp:include page="/WEB-INF/view/main/common/script.jsp" />

</body>
</html>
