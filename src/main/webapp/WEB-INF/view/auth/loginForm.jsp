<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<html>
<head>
    <title>로그인</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>
</head>
<body>
<div class="container">
    <form method="POST" action="/login">
        <div class="form-group">
            <label for="username">아이디</label>
            <input type="text" class="form-control" id="username" name="username">
        </div>
        <div class="form-group">
            <label for="password">암호</label>
            <input type="password" class="form-control" id="password" name="password">
        </div>
        <button type="submit" class="btn btn-default">로그인</button>
    </form>
</div>
</body>
</html>
