<div class="ke-panel-frame" ng-controller="AppointmentRequestsCtrl" ng-init="init()">
    <div class="ke-panel-heading">Appointment Requests
        <input type="text" name="appointmentsSearchString" ng-model="appointmentsSearchString"
               ng-change="searchAppointments()" style="width: 260px;margin-left:50px" placeholder="Search ..."/>
    </div>
    <div class="ke-panel-content">
        <table class="ke-table-vertical">
            <thead>
            <tr>
                <th>Client Name</th>
                <th>Client Contact</th>
                <th>CCC Number</th>
                <th>Appointment Reason</th>
                <th>Requested Date</th>
                <th>Status</th>
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
                <td>{{appointmentRequest.dateResolved}}</td>
                <td>{{appointmentRequest.resolvedBy}}</td>
                <td>
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