<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div ng-show="found">
    <h2>평가 시즌 정보</h2>

    <dl class="dl-horizontal">
        <dt>아이디</dt>
        <dd>{{evalSeason.id}}</dd>
        <dt>이름</dt>
        <dd>{{evalSeason.name}}</dd>

        <dt>오픈 여부</dt>
        <dd>
            <span ng-class="{label: true, 'label-primary': evalSeason.opened, 'label-default': !evalSeason.opened}">
            {{evalSeason.opened ? '평가 진행중' : '평가 시작 전'}}
            </span>
            <br />
            <a href="" ng-click="openEvaluation()" ng-show="!evalSeason.opened">평가를 오픈하려면 여기를 클릭하세요.</a>
        </dd>
    </dl>
    <dl class="dl-horizontal">
        <dt>피평가자 매핑</dt>
        <dd>
            <p>
                <button class="btn btn-primary btn-sm" type="button" ng-click="openMappingRegForm()">매핑 등록/수정</button>
                <button class="btn btn-default btn-sm" type="button" ng-click="updateSelectedMappings()">선택한 매핑 수정</button>
                <button class="btn btn-default btn-sm" type="button" ng-click="deleteSelectedMappings()">선택한 매핑 삭제</button>
            </p>
            <table class="table table-bordered table-condensed table-hover">
                <thead>
                <tr>
                    <th>피평가자</th>
                    <th>평가타입</th>
                    <th>1차 평가자</th>
                    <th>2차 평가자</th>
                    <th>동료 평가자</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-show="!evalSeason.mappings.length">
                    <td colspan="6">매핑 정보를 등록하세요.</td>
                </tr>
                <tr ng-repeat="mapping in evalSeason.mappings">
                    <td>
                        <label>
                        <input type="checkbox" ng-model="selected[mapping.ratee.id]"/>{{mapping.ratee.name}}</td>
                        </label>
                    <td>{{mapping.type}}</td>
                    <td>{{mapping.firstRater == null ? '-' : mapping.firstRater.name}}</td>
                    <td>{{mapping.secondRater.name}}</td>
                    <td>
                        <ul class="list-inline">
                            <li ng-repeat="colleague in mapping.colleagueRaters">{{colleague.name}}</li>
                        </ul>
                    </td>
                </tr>
                </tbody>
            </table>
        </dd>
    </dl>
</div>

<div ng-show="notFound">
    <div class="alert alert-danger" role="alert">존재하지 않습니다.</div>
</div>

<script type="text/ng-template" id="multipleMappingFormDialog.html">
    <div class="modal-header">
        <h3 class="modal-title">매핑 정보 등록</h3>
    </div>
    <div class="modal-body">
        <form name="evalMappingForm">
            <div class="form-group">
                <span class="help-block">
                    <ul>
                        <li>입력 형식: <kbd>피평가자,타입,1차평가자,2차평가자,동료평가자1,동료평가자2</kbd>
                            <ul>
                                <li>콤마 대신 탭 가능, 1차평가자가 없는 경우 '-'로 입력, 동료평가자가 없는 경우 입력하지 않음</li>
                                <li>피평가자 별로 한 줄씩 입력</li>
                                <li>예1: 권준,MEMBER,이순신,순조,마영갑</li>
                                <li>예2(1차 평가자 없음): 이순신,TEAM_LEADER,-,순조,마영갑</li>
                                <li>예3(동료평가자 없음): 이순신,TEAM_LEADER,-,순조</li>
                            </ul>
                        </li>
                        <li>타입에 올 수 있는 값:
                            <kbd>MEMBER</kbd>
                            <kbd>PART_LEADER</kbd>
                            <kbd>TEAM_LEADER</kbd>
                            <kbd>AM</kbd>
                            <kbd>AM_MEMBER</kbd>
                            <kbd>AM_LEADER</kbd>
                        </li>
                    </ul>
                </span>
                <label for="mappingInfo">매핑 정보</label>
                <textarea class="form-control" id="mappingInfo" name="mappingInfo" ng-model="mappingInfo" rows="13" required></textarea>

                <div class="help-block" ng-show="errorLines.length > 0">
                    <ul>
                        <li ng-repeat="line in errorLines">{{line}}</li>
                    </ul>
                </div>

                <div class="help-block" ng-show="notFoundNames.length > 0">
                    다음 이름에 해당하는 사용자 정보가 존재하지 않습니다. 이름을 확인하세요.:<br/>
                    <ul class="list-inline">
                        <li ng-repeat="name in notFoundNames"><span class="label label-default">{{name}}</span></li>
                    </ul>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" ng-disabled="!evalMappingForm.$valid" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
    </div>
</script>
