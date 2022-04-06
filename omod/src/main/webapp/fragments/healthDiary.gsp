<div class="ke-panel-frame">
    <div class="ke-panel-heading">Health Diary</div>
    <div class="ke-panel-content"  ng-controller="HealthDiaryCtrl" ng-init="init()">
        <table class="ke-table-vertical">
            <thead>
            <tr>
                <th>CCC Number</th>
                <th>Mood</th>
                <th>Note</th>
                <th>Entry Type</th>
                <th>Shared On</th>
                <th>Created On</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="healthDiary in healthDiaries">
                <td>{{healthDiary.cccNumber}}</td>
                <td>{{healthDiary.mood}}</td>
                <td>{{healthDiary.note}}</td>
                <td>{{healthDiary.entryType}}</td>
                <td>{{healthDiary.sharedOn}}</td>
                <td>{{healthDiary.dateRecorded}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<div  class="row">
    <div class="col-lg-8">
        <ul ng-show="healthDiariesPages > 1" uib-pagination total-items="healthDiariesTotalItems" ng-model="healthDiaryCurrentPage" max-size="maxSize" ng-change="healthDiaryPagination(healthDiaryCurrentPage)"
            items-per-page="pageSize" boundary-links="true" force-ellipses="true" class="pull-right"></ul>
    </div>
</div>