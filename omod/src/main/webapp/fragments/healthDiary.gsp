<div class="ke-panel-frame"  ng-controller="HealthDiaryCtrl" ng-init="init()">
    <div class="ke-panel-heading">Health Diary
            <input type="text" name="healthDiarySearchString" ng-model="healthDiarySearchString"
                   ng-change="loadHealthDiaries()" style="width: 300px;margin-left:50px" placeholder="Search using CCC, mood or note."/>
    </div>
    <div class="ke-panel-content">
        <table class="ke-table-vertical">
            <thead>
            <tr>
                <th>Client Name</th>
                <th>Client Contact</th>
                <th>CCC Number</th>
                <th>Mood</th>
                <th>Note</th>
                <th>Shared On</th>
                <th>Created On</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="healthDiary in healthDiaries">
                <td>{{healthDiary.clientName}}</td>
                <td>{{healthDiary.clientContact}}</td>
                <td>{{healthDiary.cccNumber}}</td>
                <td>{{healthDiary.mood}}</td>
                <td>{{healthDiary.note}}</td>
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