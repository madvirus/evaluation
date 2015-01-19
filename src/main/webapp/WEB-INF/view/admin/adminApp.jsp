<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html ng-app="evalAdminApp">
<head>
    <meta charset="utf-8">
    <title><spring:message code="admin.tool.title"/></title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
</head>
<body>

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">
                EVAL
            </a>
        </div>

        <div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="#/admin/evalseason">평가 관리</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="/user/logout"><spring:message code="navigation.logout"/></a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div ng-view></div>
</div>

<script src="/webjars/jquery/1.9.0/jquery.js"></script>
<script src="/webjars/bootstrap/3.1.1/js/bootstrap.js"></script>
<script src="/webjars/angularjs/1.2.16/angular.js"></script>
<script src="/webjars/angularjs/1.2.16/angular-route.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap.js"></script>
<script src="/webjars/angular-ui-bootstrap/0.12.0/ui-bootstrap-tpls.js"></script>
<script src="/js/adminApp.js"></script>
<script src="/js/ng-service.js"></script>
<script src="/js/ng-controllers-admin.js"></script>

</body>
</html>
