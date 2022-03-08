function AppointmentRequestsCtrl($scope, $routeParams, $appointmentRequests, $location, $translate, $localeService) {

     $scope.maxSize = 10;
     $scope.pageSize = 10;
     $scope.currentPage = 1;
     $scope.totalItems = 0;

    $appointmentRequests.getAppointmentRequests($scope.currentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.appointmentRequests = serverData.objects;
        $scope.noOfPages = serverData.pages;
        $scope.totalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('currentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $appointmentRequests.getAppointmentRequests($scope.currentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.appointmentRequests = serverData.objects;
                $scope.noOfPages = serverData.pages;
                $scope.totalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);
}