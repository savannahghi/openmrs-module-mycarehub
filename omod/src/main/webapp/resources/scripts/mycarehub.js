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

    $scope.appointmentPagination = function (appointmentsCurrentPage) {
        $scope.appointmentsCurrentPage = appointmentsCurrentPage;
        $scope.loadAppointmentRequests();
    };

    $scope.createRange = function(max){
        rangeItems = [];
        for(var i = 1; i <= max; i++){
            rangeItems.push(i);
        }
        return rangeItems;
    };

    $scope.setAppointmentResolved = function(uuid){
        params={uuid:uuid}
        return $http.post(ui.fragmentActionLink('mycarehub','myCareHubUtils','setAppointmentResolved',params));
    }

    $scope.setAppointmentInProgress = function(uuid){
        params={uuid:uuid}
        return $http.post(ui.fragmentActionLink('mycarehub','myCareHubUtils','setAppointmentInProgress',params))
            .success(function(){
                $scope.loadAppointmentRequests();
            });
    }

    $scope.setAppointmentRejected = function (uuid) {
        $('#wait').show();
        $scope.setAppointmentResolved(uuid).
        then(function (response) {
            $scope.loadAppointmentRequests();
        },function (response) {
            console.log("Error rejecting appointment request");
        });
    };

    $scope.setAppointmentApproved = function (appointmentRequest) {
        $scope.setAppointmentResolved(appointmentRequest.uuid).
        then(function (response) {
            var params = {encounterId: appointmentRequest.appointmentUuid,
                appId: "kenyaemr.medicalEncounter", returnUrl: window.location.href};
            ui.navigate(ui.pageLink("kenyaemr", "editForm", params));
        },function (response) {
            console.log("Error approving appointment request");
        });


    };
}]);

kenyaemrApp.controller('HealthDiaryCtrl',['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
    /**
     * Initializes the controller
     * @param appId the current app id
     */
    $scope.init = function() {$scope.maxSize = 10;
        $scope.pageSize = 20;
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

                console.log("$scope.healthDiariesPages: "+$scope.healthDiariesPages);
                console.log("$scope.healthDiariesTotalItems: "+$scope.healthDiariesTotalItems);
            });
    }
    $scope.healthDiaryPagination = function (healthDiaryCurrentPage) {
        $scope.healthDiaryCurrentPage = healthDiaryCurrentPage;
        $scope.loadHealthDiaries();
    };

    $scope.createRange = function(max){
        rangeItems = [];
        for(var i = 1; i <= max; i++){
            rangeItems.push(i);
        }
        return rangeItems;
    };
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

    $scope.redFlagPagination = function (redFlagCurrentPage) {
        $scope.redFlagCurrentPage = redFlagCurrentPage;
        $scope.loadRedFlags();
    };

    $scope.createRange = function(max){
        rangeItems = [];
        for(var i = 1; i <= max; i++){
            rangeItems.push(i);
        }
        return rangeItems;
    };

    $scope.setRedFlagInProgress = function(uuid){
        params={uuid:uuid}
        return $http.post(ui.fragmentActionLink('mycarehub','myCareHubUtils','setRedFlagInProgress',params))
            .success(function(){
                $scope.loadRedFlags();
            }).error(function(){
                console.log("Error setting red flag status");
            });
    }

    $scope.setRedFlagResolved = function(uuid){
        params={uuid:uuid}
        return $http.post(ui.fragmentActionLink('mycarehub','myCareHubUtils','setRedFlagResolved',params))
            .success(function(){
                $scope.loadRedFlags();
            }).error(function(){
                console.log("Error setting red flag status");
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

    $scope.screeningToolRedFlagPagination = function (screeningToolRedFlagCurrentPage) {
        $scope.screeningToolRedFlagCurrentPage = screeningToolRedFlagCurrentPage;
        $scope.loadScreeningToolRedFlags();
    };

    $scope.createRange = function(max){
        rangeItems = [];
        for(var i = 1; i <= max; i++){
            rangeItems.push(i);
        }
        return rangeItems;
    };

    $scope.setRedFlagInProgress = function(uuid){
        params={uuid:uuid}
        return $http.post(ui.fragmentActionLink('mycarehub','myCareHubUtils','setRedFlagInProgress',params))
            .success(function(){
                $scope.loadScreeningToolRedFlags();
            }).error(function(){
                console.log("Error setting setting screening tool red flag status");
            });
    }

    $scope.setRedFlagResolved = function(uuid){
        params={uuid:uuid}
        return $http.post(ui.fragmentActionLink('mycarehub','myCareHubUtils','setRedFlagResolved',params))
            .success(function(){
                $scope.loadScreeningToolRedFlags();
            }).error(function(){
                console.log("Error setting screening tool red flag status");
            });
    }
}]);