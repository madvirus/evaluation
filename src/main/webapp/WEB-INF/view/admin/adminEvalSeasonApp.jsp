<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="adminEvalSeasonsApp" ng-controller="adminEvalSeasonsCtrl">
<head>
    <meta charset="utf-8">
    <title>관리도구</title>
    <jsp:include page="/WEB-INF/view/main/common/css.jsp" />
</head>
<body>

<jsp:include page="admin-navi.jsp" />

<div class="container">
    <div ng-show="showRouteError" ng-cloak>
        <div ng-show="errorStatus == 403" class="alert alert-danger" role="alert">
            접근 권한이 없습니다. (새로 고침해서 로그아웃 여부를 확인하세요.)
        </div>
        <div ng-show="errorStatus == 500" class="alert alert-danger" role="alert">
            서버 처리 과정에서 문제가 발생했습니다.
        </div>
        <div ng-show="errorStatus == 0" class="alert alert-danger" role="alert">
            서버에 연결할 수 없어 화면을 구성할 수 없습니다.
        </div>
    </div>
    <div ng-show="!showRouteError" ng-view></div>
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
<script src="/js/admin/ng-admin-evalseason-controller-states.js"></script>

</body>
</html>
