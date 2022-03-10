function HealthDiaryCtrl($scope, $routeParams, $healthDiary, $location, $translate, $localeService) {

     $scope.maxSize = 10;
     $scope.pageSize = 10;
     $scope.currentPage = 1;
     $scope.totalItems = 0;

    $healthDiary.getHealthDiaries($scope.currentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.healthDiaries = serverData.objects;
        $scope.noOfPages = serverData.pages;
        $scope.totalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('currentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $healthDiary.getHealthDiaries($scope.currentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.healthDiaries = serverData.objects;
                $scope.noOfPages = serverData.pages;
                $scope.totalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);
}