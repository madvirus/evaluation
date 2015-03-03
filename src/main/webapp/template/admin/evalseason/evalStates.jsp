<%@ page contentType="text/html;charset=UTF-8" session="false" %>

<ol class="breadcrumb">
    <li><a href="#/">평가관리</a></li>
    <li><a href="#/{{evalSeasonId}}">평가 시즌 정보</a></li>
    <li class="active">평가 진행 상태</li>
</ol>

<h2>평가 진행 상태</h2>

<table class="table table-bordered table-condensed">
    <thead>
    <tr>
        <th>피평가자</th>
        <th>동료평가자</th>
        <th>1차평가자</th>
        <th>2차평가자</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="evalState in allEvalStates">
        <td>
            {{evalState.self.rater.name}}:
            <span ng-show="evalState.self.state == 'NONE'" class="label label-default">시작전</span>
            <span ng-show="evalState.self.state == 'DOING'" class="label label-info">진행중</span>
            <span ng-show="evalState.self.state == 'DONE'" class="label label-success">완료</span>
        </td>
        <td>
            <ul ng-show="evalState.colleagues.length > 0">
                <li ng-repeat="collState in evalState.colleagues">
                    {{collState.rater.name}}:
                    <span ng-show="collState.state == 'NONE'" class="label label-default">시작전</span>
                    <span ng-show="collState.state == 'DOING'" class="label label-info">진행중</span>
                    <span ng-show="collState.state == 'DONE'" class="label label-success">완료</span>
                </li>
            </ul>
        </td>
        <td ng-show="evalState.firstSkip">
            1차 평가 대상 아님
        </td>
        <td ng-show="!evalState.firstSkip">
            {{evalState.first.rater.name}}:
            <span ng-show="evalState.first.state == 'NONE'" class="label label-default">시작전</span>
            <span ng-show="evalState.first.state == 'DOING'" class="label label-info">진행중</span>
            <span ng-show="evalState.first.state == 'DONE'" class="label label-success">완료</span>

            <span ng-show="canReturnDraft(evalState.first.rater.id)">
                <a href="" ng-click="returnToDraftFirstEvalOf(evalState.first.rater)">[1차 평가 완료 취소하기]</a>
            </span>
        </td>
        <td>
            {{evalState.second.rater.name}}:
            <span ng-show="evalState.second.state == 'NONE'" class="label label-default">시작전</span>
            <span ng-show="evalState.second.state == 'DOING'" class="label label-info">진행중</span>
            <span ng-show="evalState.second.state == 'DONE'" class="label label-success">완료</span>
        </td>
    </tr>
    </tbody>
</table>

