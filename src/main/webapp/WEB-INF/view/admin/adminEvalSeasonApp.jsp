<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="adminEvalSeasonsApp">
<head>
    <meta charset="utf-8">
    <title><spring:message code="admin.tool.title"/></title>
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
                <sec:authorize access="hasAuthority('ROLE_HRADMIN')">
                <li class="active"><a href="/admin/evalseasons">평가 관리</a></li>
                </sec:authorize>
                <sec:authorize access="hasAuthority('ROLE_SYSTEMADMIN')">
                <li><a href="/admin/system">시스템 관리</a></li>
                </sec:authorize>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="/logout"><spring:message code="navigation.logout"/></a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div ng-view></div>
</div>

<script type="text/ng-template" id="alertDialog.html">
    <div class="modal-header">
        <h3 class="modal-title">{{title}}</h3>
    </div>
    <div class="modal-body">
        {{message}}
    </div>
    <div class="modal-footer">
        <button class="btn btn-default" ng-click="confirm()">확인</button>
    </div>
</script>

<script type="text/ng-template" id="confirmDialog.html">
    <div class="modal-header">
        <h3 class="modal-title">{{title}}</h3>
    </div>
    <div class="modal-body">
        {{message}}
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" ng-click="yes()">예</button>
        <button class="btn btn-default" ng-click="no()">아니오</button>
    </div>
</script>

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>
<script src="/webjars/angularjs/1.2.16/angular.js"></script>
<script src="/webjars/angularjs/1.2.16/angular-route.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap-tpls.js"></script>
<script src="/js/ng-service.js"></script>
<script src="/js/ng-admin-evanseason-app.js"></script>
<script src="/js/ng-admin-evalseason-controller.js"></script>

</body>
</html>
