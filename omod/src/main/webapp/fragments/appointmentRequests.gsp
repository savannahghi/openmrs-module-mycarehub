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
                <th></th>
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
                    <button ng-hide="appointmentRequest.progressDate != '' || appointmentRequest.dateResolved != ''" type="submit" ng-click="appointmentRequest(redFlag.uuid)" class="btn btn-primary">IN PROGRESS</button>&nbsp;
                    <button ng-hide="appointmentRequest.dateResolved != ''" type="submit" ng-click="setAppointmentResolved(appointmentRequest.uuid)" class="btn btn-warning">RESOLVED</button>&nbsp;
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<div  class="row">
    <div class="col-lg-8">
        <ul ng-show="appointmentPages > 1" uib-pagination total-items="appointmentsTotalItems"
            ng-model="appointmentsCurrentPage" max-size="maxSize" ng-change="appointmentPagination(appointmentsCurrentPage)"
            items-per-page="pageSize" boundary-links="true" force-ellipses="true" class="pull-right"></ul>
    </div>
</div>