<div class="ke-panel-frame">
    <div class="ke-panel-heading">Appointment Requests</div>
    <div class="ke-panel-content" ng-controller="AppointmentRequestsCtrl" ng-init="init()">
        <table class="ke-table-vertical">
            <thead>
            <tr>
                <th>Client Name</th>
                <th>Client Contact</th>
                <th>CCC Number</th>
                <th>Appointment Reason</th>
                <th>Requested Date</th>
                <th>Status</th>
                <th>Progress Date</th>
                <th>Progress By</th>
                <th>Date Resolved</th>
                <th>Resolved By</th>
                <th>ACTION</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="appointmentRequest in appointmentRequests">
                <td>{{appointmentRequest.clientName}}</td>
                <td>{{appointmentRequest.clientContact}}</td>
                <td>{{appointmentRequest.cccNumber}}</td>
                <td>{{appointmentRequest.appointmentReason}}</td>
                <td>{{appointmentRequest.requestedDate}}</td>
                <td>{{appointmentRequest.status}}</td>
                <td>{{appointmentRequest.progressDate}}</td>
                <td>{{appointmentRequest.progressBy}}</td>
                <td>{{appointmentRequest.dateResolved}}</td>
                <td>{{appointmentRequest.resolvedBy}}</td>
                <td>
                    <button ng-hide="appointmentRequest.progressDate != '' || appointmentRequest.dateResolved != ''" type="submit" ng-click="setAppointmentInProgress(appointmentRequest.uuid)" class="btn btn-primary">IN PROGRESS</button>&nbsp;
                    <button ng-hide="appointmentRequest.dateResolved != ''" type="submit" ng-click="setAppointmentRejected(appointmentRequest.uuid)" class="btn btn-warning">REJECT</button>&nbsp;
                    <button ng-hide="appointmentRequest.dateResolved != ''" type="submit" ng-click="setAppointmentApproved(appointmentRequest)" class="btn btn-warning">APPROVE</button>&nbsp;
                </td>
            </tr>
            </tbody>
        </table>
        <div class="ke-panel-content pagination">Page {{appointmentsCurrentPage}} of {{appointmentPages}} &nbsp;&nbsp;&nbsp;
            <span ng-repeat="pageIndex in createRange(appointmentPages);">
                <span  ng-show="appointmentsCurrentPage == pageIndex">{{pageIndex}}</span>
                <a href="#"  ng-show="appointmentsCurrentPage != pageIndex"
                   ng-click="appointmentPagination(pageIndex)">{{pageIndex}}</a> |
            </span>
        </div>
    </div>
</div>