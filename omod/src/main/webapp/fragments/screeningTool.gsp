<div class="ke-panel-frame"  ng-controller="ScreeningToolCtrl" ng-init="init()">
    <div class="ke-panel-heading">Screening Tool Red Flags
        <input type="text" name="screeningToolRedFlagSearchString" ng-model="screeningToolRedFlagSearchString"
               ng-change="loadScreeningToolRedFlags()" style="width: 450px;margin-left:50px" placeholder="Search using CCC, client name, client contact, request, screening tool name and status."/>
    </div>
    <div class="ke-panel-content">
        <table class="ke-table-vertical">
            <thead>
            <tr>
                <th>Client Name</th>
                <th>Client Contact</th>
                <th>CCC Number</th>
                <th>Request</th>
                <th>Screening Tool Name</th>
                <th>Screening Score</th>
                <th>Status</th>
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
                <td>{{screeningToolRedFlag.ScreeningToolName}}</td>
                <td>{{screeningToolRedFlag.ScreeningToolScore}}</td>
                <td>{{screeningToolRedFlag.Status}}</td>
                <td>{{screeningToolRedFlag.dateResolved}}</td>
                <td>{{screeningToolRedFlag.resolvedBy}}</td>
                <td>
                    <button ng-hide="screeningToolRedFlag.dateResolved != ''" type="submit" ng-click="setRedFlagResolved(screeningToolRedFlag.uuid)" class="btn btn-warning">RESOLVE</button>&nbsp;
                </td>
            </tr>
            </tbody>
        </table>
        <div class="ke-panel-content pagination">Page {{screeningToolRedFlagCurrentPage}} of {{screeningToolRedFlagPages}} &nbsp;&nbsp;&nbsp;
            <span ng-repeat="pageIndex in createRange(screeningToolRedFlagPages);">
                <span  ng-show="screeningToolRedFlagCurrentPage == pageIndex">{{pageIndex}}</span>
                <a href="#"  ng-show="screeningToolRedFlagCurrentPage != pageIndex"
                   ng-click="screeningToolRedFlagPagination(pageIndex)">{{pageIndex}}</a> |
            </span>
        </div>
    </div>
</div>