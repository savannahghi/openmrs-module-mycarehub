kenyaemrApp.controller('AppointmentRequestsCtrl',['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
/**
 * Initializes the controller
 * @param appId the current app id
 */
 $scope.init = function(appId) {
     $scope.appId = appId;
     $scope.maxSize = 10;
     $scope.pageSize = 10;
     $scope.appointmentsCurrentPage = 1;
     $scope.appointmentsTotalItems = 0;
     $scope.loadAppointmentRequests();
    };
    $scope.loadAppointmentRequests = function(){
        var params = {pageNumber:$scope.appointmentsCurrentPage, pageSize:$scope.pageSize}
        $http.get(ui.fragmentActionLink('mycarehub','myCareHubUtils','getAppointmentRequests',params))
            .success(function (response) {
                var serverData = response;
                $scope.appointmentRequests = serverData.objects;
                $scope.appointmentPages = serverData.pages;
                $scope.appointmentsTotalItems = serverData.totalItems;
            });
    }
}]);

kenyaemrApp.controller('HealthDiaryCtrl',['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
    /**
     * Initializes the controller
     * @param appId the current app id
     */
    $scope.init = function() {$scope.maxSize = 10;
        $scope.pageSize = 10;
        $scope.healthDiaryCurrentPage = 1;
        $scope.healthDiaryTotalItems = 0;
        $scope.loadHealthDiaries();
    };
    $scope.loadHealthDiaries = function(){
        var params = {pageNumber:$scope.healthDiaryCurrentPage, pageSize:$scope.pageSize}
        $http.get(ui.fragmentActionLink('mycarehub','myCareHubUtils','getHealthDiaries',params))
            .success(function (response) {
                var serverData = response;
                $scope.healthDiaries = serverData.objects;
                $scope.healthDiariesPages = serverData.pages;
                $scope.healthDiariesTotalItems = serverData.totalItems;
            });
    }
}]);

kenyaemrApp.controller('RedFlagsCtrl',['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
    /**
     * Initializes the controller
     * @param appId the current app id
     */
    $scope.init = function() {$scope.maxSize = 10;
        $scope.pageSize = 10;
        $scope.redFlagCurrentPage = 1;
        $scope.redFlagTotalItems = 0;
        $scope.loadRedFlags();
    };
    $scope.loadRedFlags = function(){
        var params = {pageNumber:$scope.redFlagCurrentPage, pageSize:$scope.pageSize, requestType:'RED_FLAG'}
        $http.get(ui.fragmentActionLink('mycarehub','myCareHubUtils','getRedFlagsByType',params))
            .success(function (response) {
                var serverData = response;
                $scope.redFlags = serverData.objects;
                $scope.redFlagsPages = serverData.pages;
                $scope.redFlagsTotalItems = serverData.totalItems;
            });
    }
}]);

kenyaemrApp.controller('ScreeningToolCtrl',['$scope', '$http', '$timeout', function($scope, $http, $timeout) {

    $scope.init = function() {$scope.maxSize = 10;
        $scope.pageSize = 10;
        $scope.screeningToolRedFlagCurrentPage = 1;
        $scope.screeningToolRedFlagTotalItems = 0;
        $scope.loadScreeningToolRedFlags();
    };
    $scope.loadScreeningToolRedFlags = function(){
        var params = {pageNumber:$scope.screeningToolRedFlagCurrentPage, pageSize:$scope.pageSize, requestType:'SCREENING_TOOL_RED_FLAG'}
        $http.get(ui.fragmentActionLink('mycarehub','myCareHubUtils','getRedFlagsByType',params))
            .success(function (response) {
                var serverData = response;
                $scope.screeningToolRedFlags = serverData.objects;
                $scope.screeningToolRedFlagPages = serverData.pages;
                $scope.screeningToolRedFlagTotalItems = serverData.totalItems;
            });
    }
}]);