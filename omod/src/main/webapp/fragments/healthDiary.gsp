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
        <div class="ke-panel-content pagination">Page {{healthDiaryCurrentPage}} of {{healthDiariesPages}} &nbsp;&nbsp;&nbsp;
            <span ng-repeat="pageIndex in createRange(healthDiariesPages);">
                    <span  ng-show="healthDiaryCurrentPage == pageIndex">{{pageIndex}}</span>
                    <a href="#"  ng-show="healthDiaryCurrentPage != pageIndex"
                           ng-click="healthDiaryPagination(pageIndex)">{{pageIndex}}</a> |
            </span>
        </div>
    </div>
</div>