<div class="ke-panel-frame">
    <div class="ke-panel-heading">Screening Tool</div>
    <div class="ke-panel-content"  ng-controller="ScreeningToolCtrl" ng-init="init()">
        <table class="ke-table-vertical">
            <thead>
            <tr>
                <th>Client Name</th>
                <th>Client Contact</th>
                <th>CCC Number</th>
                <th>Request</th>
                <th>Request Type</th>
                <th>Screening Tool Name</th>
                <th>Screening Score</th>
                <th>Status</th>
                <th>Progress Date</th>
                <th>Progress By</th>
                <th>Date Resolved</th>
                <th>Resolved By</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="screeningToolRedFlag in screeningToolRedFlags">
                <td>{{screeningToolRedFlag.clientName}}</td>
                <td>{{screeningToolRedFlag.clientContact}}</td>
                <td>{{screeningToolRedFlag.cccNumber}}</td>
                <td>{{screeningToolRedFlag.Request}}</td>
                <td>{{screeningToolRedFlag.RequestType}}</td>
                <td>{{screeningToolRedFlag.ScreeningToolName}}</td>
                <td>{{screeningToolRedFlag.ScreeningToolScore}}</td>
                <td>{{screeningToolRedFlag.Status}}</td>
                <td>{{screeningToolRedFlag.progressDate}}</td>
                <td>{{screeningToolRedFlag.progressBy}}</td>
                <td>{{screeningToolRedFlag.dateResolved}}</td>
                <td>{{screeningToolRedFlag.resolvedBy}}</td>
                <td>
                    <button ng-hide="screeningToolRedFlag.progressDate != '' || screeningToolRedFlag.dateResolved != ''" type="submit" ng-click="setRedFlagInProgress(screeningToolRedFlag.uuid)" class="btn btn-primary">IN PROGRESS</button>&nbsp;
                    <button ng-hide="screeningToolRedFlag.dateResolved != ''" type="submit" ng-click="setRedFlagResolved(screeningToolRedFlag.uuid)" class="btn btn-warning">RESOLVED</button>&nbsp;
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<div  class="row">
    <div class="col-lg-8">
        <ul ng-show="screeningToolRedFlagPages > 1" uib-pagination total-items="screeningToolRedFlagTotalItems" ng-model="screeningToolRedFlagCurrentPage" max-size="maxSize" ng-change="screeningToolRedFlagPagination(screeningToolRedFlagCurrentPage)"
            items-per-page="pageSize" boundary-links="true" force-ellipses="true" class="pull-right"></ul>
    </div>
</div>