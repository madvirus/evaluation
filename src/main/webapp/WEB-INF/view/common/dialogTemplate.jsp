<%@ page contentType="text/html;charset=UTF-8" session="false" %>

<script type="text/ng-template" id="alertDialog.html">
    <div class="modal-header">
        <h3 class="modal-title">{{title}}</h3>
    </div>
    <div class="modal-body">
        <div class="alert alert-{{alertType}}" role="alert">
            {{message}}
        </div>
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
