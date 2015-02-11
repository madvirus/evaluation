<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
                <li><a href="/admin/evalseasons">평가 관리</a></li>
                </sec:authorize>
                <sec:authorize access="hasAuthority('ROLE_SYSTEMADMIN')">
                <li><a href="/admin/system">시스템 관리</a></li>
                </sec:authorize>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="/logout">로그아웃</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
