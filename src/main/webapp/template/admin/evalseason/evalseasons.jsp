<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<h2>평가 시즌 관리</h2>

<p>
    <button class="btn btn-primary btn-sm" ng-click="openRegDialog()">
        신규 평가 시즌 생성
    </button>
</p>

<table class="table table-bordered table-condensed table-hover">
    <thead>
    <tr>
        <th>ID</th>
        <th>이름</th>
        <th>상태</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="evalSeason in evalSeasons">
        <td><a href="#/admin/evalseasons/{{evalSeason.id}}">{{evalSeason.id}}</a></td>
        <td>{{evalSeason.name}}</td>
        <td>{{evalSeason.opened ? '평가 시작함' : '평가 시작 전'}}</td>
    </tr>
    </tbody>
</table>

<script type="text/ng-template" id="newEvalSeasonDialog.html">
    <div class="modal-header">
        <h3 class="modal-title">신규 평가 시즌 만들기</h3>
    </div>
    <div class="modal-body">
        <form name="evalSeasonForm">
            <div class="form-group">
                <label for="evalSeasonId">평가 시즌 ID</label>
                <input class="form-control" id="evalSeasonId" name="evalSeasonId" ng-model="evalSeasonId" required>
                <span class="help-block" ng-show="evalSeasonForm.evalSeasonId.$dirty && evalSeasonForm.evalSeasonId.$error.required">
                    값을 입력하세요.
                </span>
                <span class="help-block" ng-show="duplicateId != ''">
                    {{duplicateId}} 중복된 ID 입니다.
                </span>
            </div>
            <div class="form-group">
                <label for="evalSeasonName">이름</label>
                <input class="form-control" id="evalSeasonName" name="evalSeasonName" ng-model="evalSeasonName" required>
                <span class="help-block" ng-show="evalSeasonForm.evalSeasonName.$dirty && evalSeasonForm.evalSeasonName.$error.required">
                    값을 입력하세요.
                </span>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" ng-disabled="!evalSeasonForm.$valid" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
    </div>
</script>
