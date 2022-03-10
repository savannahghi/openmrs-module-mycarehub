function ServiceRequestsCtrl($scope, $routeParams, $serviceRequests, $location, $translate, $localeService) {

     $scope.maxSize = 10;
     $scope.pageSize = 10;
     $scope.currentPage = 1;
     $scope.totalItems = 0;
     $scope.screeningToolCurrentPage = 1;
     $scope.screeningToolTotalItems = 0;

    $serviceRequests.getServiceRequests("RED_FLAG", $scope.currentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.redFlagServiceRequests = serverData.objects;
        $scope.noOfPages = serverData.pages;
        $scope.totalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('currentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $serviceRequests.getServiceRequests("RED_FLAG", $scope.currentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.redFlagServiceRequests = serverData.objects;
                $scope.noOfPages = serverData.pages;
                $scope.totalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);

    $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolCurrentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.screeningToolTotalItems = serverData.objects;
        $scope.screeningToolNoOfPages = serverData.pages;
        $scope.screeningToolTotalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('screeningToolCurrentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolCurrentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.screeningToolTotalItems = serverData.objects;
                $scope.screeningToolNoOfPages = serverData.pages;
                $scope.screeningToolTotalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);
}