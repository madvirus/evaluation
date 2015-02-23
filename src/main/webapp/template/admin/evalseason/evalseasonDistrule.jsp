<%@ page contentType="text/html;charset=UTF-8" session="false" %>

<ol class="breadcrumb">
    <li><a href="#/">평가관리</a></li>
    <li><a href="#/{{evalSeasonId}}">평가 시즌 정보</a></li>
    <li class="active">등급 배분</li>
</ol>

<h2>분배 규칙 수정</h2>

<table class="table table-bordered table-condensed">
    <thead>
    <tr>
        <th>1차 평가자</th>
        <th>규칙 목록</th>
        <th>피평가자 목록</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="ruleData in ruleDataList">
        <td>
            {{ruleData.firstRater.name}}
            <a href="" ng-click="openRuleManager(ruleData)">[규칙 관리]</a>
        </td>
        <td>
            <ul>
                <li ng-repeat="rule in ruleData.ruleList">
                    {{rule.name}}: S={{rule.sNumber}},A={{rule.aNumber}},B={{rule.bNumber}},C/D={{rule.cdNumber}}
                    <br/>
                    <span ng-repeat="ratee in rule.ratees">{{ratee.name}} </span>
                </li>
            </ul>
        </td>
        <td><span ng-repeat="ratee in ruleData.ratees">{{ratee.name}}&nbsp;</span></td>
    </tr>
    </tbody>
</table>

<script type="text/ng-template" id="ruleManagerDialog.html">
    <div class="modal-header">
        <h3 class="modal-title">1차 평가자 {{ruleData.firstRater.name}}: 배분 규칙 관리</h3>
    </div>
    <div class="modal-body">
        <button class="btn btn-default btn-sm" ng-click="addRule()">규칙 추가</button>
        <form novalidate name="ruleListForm">
            <table class="table table-bordered table-condensed">
                <thead>
                <tr>
                    <th class="col-md-2">규칙이름</th>
                    <th class="col-md-1">S</th>
                    <th class="col-md-1">A</th>
                    <th class="col-md-1">B</th>
                    <th class="col-md-1">C/D</th>
                    <th class="col-md-3">대상 피평가자</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-form="ruleForm" ng-repeat="rule in ruleData.ruleList">
                    <td ng-class="{'has-error': showError && !ruleForm.name.$valid}">
                        <input type="text" class="form-control input-sm" required ng-model="rule.name" name="name">
                        <br/>
                        <a href="" ng-click="deleteRule(rule)">[삭제]</a>
                    </td>
                    <td ng-class="{'has-error': showError && !ruleForm.sNumber.$valid}">
                        <input type="number" class="form-control input-sm" ng-model="rule.sNumber" required name="sNumber">
                    </td>
                    <td ng-class="{'has-error': showError && !ruleForm.aNumber.$valid}">
                        <input type="number" class="form-control input-sm" ng-model="rule.aNumber" required name="aNumber">
                    </td>
                    <td ng-class="{'has-error': showError && !ruleForm.bNumber.$valid}">
                        <input type="number" class="form-control input-sm" ng-model="rule.bNumber" required name="bNumber">
                    </td>
                    <td ng-class="{'has-error': showError && !ruleForm.cdNumber.$valid}">
                        <input type="number" class="form-control input-sm" ng-model="rule.cdNumber" required name="cdNumber">
                    </td>
                    <td>
                        <div class="checkbox" ng-repeat="ratee in ruleData.ratees">
                            <label>
                                <input type="checkbox"
                                       checklist-model="rule.ratees"
                                       checklist-value="ratee">{{ratee.name}}
                            </label>
                        </div>
                        <a href="" ng-click="selectAll(rule)">[전체 선택]</a>
                        <a href="" ng-click="deselectAll(rule)">[전체 선택취소]</a>
                    </td>
                </tr>
                </tbody>
                <tbody>
                <tr>

                </tr>
                <tr ng-show="showError">
                    <td colspan="6">
                        <ul>
                            <li>배분 합이 0이면 안 됩니다.</li>
                            <li>배분 합과 대상 피평가자의 수가 일치해야 합니다.</li>
                            <li ng-show="dupRatees.length > 0">한 피평가자는 한 개의 규칙에만 포함할 수 있습니다.(<span ng-repeat="ratee in dupRatees">{{ratee.name}} </span>)</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        선택 가능 피평가자:
                        <span ng-repeat="ratee in ruleData.ratees">{{ratee.name}} </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" ng-disabled="!ruleListForm.$valid" ng-click="save()">저장</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
    </div>
</script>

