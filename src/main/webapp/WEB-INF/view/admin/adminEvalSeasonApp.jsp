<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="adminEvalSeasonsApp">
<head>
    <meta charset="utf-8">
    <title>관리도구</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
</head>
<body>

<jsp:include page="admin-navi.jsp" />

<div class="container">
    <div ng-view></div>
</div>

<jsp:include page="/WEB-INF/view/common/dialogTemplate.jsp" />

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>
<script src="/webjars/angularjs/1.2.16/angular.js"></script>
<script src="/webjars/angularjs/1.2.16/angular-route.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap-tpls.js"></script>
<script src="/js/common/checklist-model.js"></script>
<script src="/js/common/ng-dialog-module.js"></script>
<script src="/js/common/ng-http-module.js"></script>
<script src="/js/admin/ng-admin-service.js"></script>
<script src="/js/admin/ng-admin-evanseason-app.js"></script>
<script src="/js/admin/ng-admin-evalseason-controller.js"></script>
<script src="/js/admin/ng-admin-evalseason-controller-distrule.js"></script>

</body>
</html>
