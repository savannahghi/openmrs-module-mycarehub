function AppointmentRequestsCtrl($scope, $routeParams, $serviceRequests, $location, $translate) {

     $scope.maxSize = 10;
     $scope.pageSize = 10;
     $scope.appointmentsCurrentPage = 1;
     $scope.appointmentsTotalItems = 0;
     $scope.healthDiaryCurrentPage = 1;
     $scope.healthDiaryTotalItems = 0;
     $scope.redFlagCurrentPage = 1;
     $scope.redFlagTotalItems = 0;
     $scope.screeningToolRedFlagCurrentPage = 1;
     $scope.screeningToolRedFlagTotalItems = 0;

//Appointments
    $serviceRequests.getAppointmentRequests($scope.appointmentsCurrentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.appointmentRequests = serverData.objects;
        $scope.appointmentPages = serverData.pages;
        $scope.appointmentsTotalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('appointmentsCurrentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $serviceRequests.getAppointmentRequests($scope.appointmentsCurrentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.appointmentRequests = serverData.objects;
                $scope.appointmentPages = serverData.pages;
                $scope.appointmentsTotalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);

//Health Diaries
    $serviceRequests.getHealthDiaries($scope.healthDiaryCurrentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.healthDiaries = serverData.objects;
        $scope.healthDiariesPages = serverData.pages;
        $scope.healthDiariesTotalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('healthDiaryCurrentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $serviceRequests.getHealthDiaries($scope.healthDiaryCurrentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.healthDiaries = serverData.objects;
                $scope.healthDiariesPages = serverData.pages;
                $scope.healthDiariesTotalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);


//Red Flags
    $serviceRequests.getServiceRequests("RED_FLAG", $scope.redFlagCurrentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.redFlags = serverData.objects;
        $scope.redFlagsPages = serverData.pages;
        $scope.redFlagsTotalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('redFlagCurrentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $serviceRequests.getHealthDiaries($scope.redFlagCurrentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.redFlags = serverData.objects;
                $scope.redFlagsPages = serverData.pages;
                $scope.redFlagsTotalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);

//Screening Tool Red Flags
    $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolRedFlagCurrentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.screeningToolRedFlags = serverData.objects;
        $scope.screeningToolRedFlagPages = serverData.pages;
        $scope.screeningToolRedFlagTotalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.$watch('screeningToolRedFlagCurrentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolRedFlagCurrentPage, $scope.pageSize).
            then(function (response) {
                var serverData = response.data;
                $scope.screeningToolRedFlags = serverData.objects;
                $scope.screeningToolRedFlagPages = serverData.pages;
                $scope.screeningToolRedFlagTotalItems = serverData.totalItems;
                $('#wait').hide();
            });
        }
    }, true);

    //Setting status for redflags
     $scope.setRedFlagInProgress = function (uuid) {
         $serviceRequests.setRedFlagInProgress(uuid).
            then(function (response) {
                $('#wait').hide();
                $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolRedFlagCurrentPage, $scope.pageSize).
                then(function (response) {
                    var serverData = response.data;
                    $scope.screeningToolRedFlags = serverData.objects;
                    $scope.screeningToolRedFlagPages = serverData.pages;
                    $scope.screeningToolRedFlagTotalItems = serverData.totalItems;
                    $('#wait').hide();
                });

                $serviceRequests.getServiceRequests("RED_FLAG", $scope.redFlagCurrentPage, $scope.pageSize).
                then(function (response) {
                    var serverData = response.data;
                    $scope.redFlags = serverData.objects;
                    $scope.redFlagsPages = serverData.pages;
                    $scope.redFlagsTotalItems = serverData.totalItems;
                    $('#wait').hide();
                });
            },function (response) {
                $('#wait').hide();
            });
     };

     $scope.setRedFlagResolved = function (uuid) {
         $('#wait').show();
         $serviceRequests.setRedFlagResolved(uuid).
            then(function (response) {
                $('#wait').hide();
                $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolRedFlagCurrentPage, $scope.pageSize).
                then(function (response) {
                    var serverData = response.data;
                    $scope.screeningToolRedFlags = serverData.objects;
                    $scope.screeningToolRedFlagPages = serverData.pages;
                    $scope.screeningToolRedFlagTotalItems = serverData.totalItems;
                    $('#wait').hide();
                });

                $serviceRequests.getServiceRequests("RED_FLAG", $scope.redFlagCurrentPage, $scope.pageSize).
                then(function (response) {
                    var serverData = response.data;
                    $scope.redFlags = serverData.objects;
                    $scope.redFlagsPages = serverData.pages;
                    $scope.redFlagsTotalItems = serverData.totalItems;
                    $('#wait').hide();
                });
            },function (response) {
                $('#wait').hide();
            });
     };

     //Setting status for appointments
      $scope.setAppointmentInProgress = function (uuid) {
          $serviceRequests.setAppointmentInProgress(uuid).
             then(function (response) {
                $('#wait').hide();
                $serviceRequests.getAppointmentRequests($scope.appointmentsCurrentPage, $scope.pageSize).
                then(function (response) {
                    var serverData = response.data;
                    $scope.appointmentRequests = serverData.objects;
                    $scope.appointmentPages = serverData.pages;
                    $scope.appointmentsTotalItems = serverData.totalItems;
                    $('#wait').hide();
                });
             },function (response) {
                 $('#wait').hide();
             });
      };

      $scope.setAppointmentResolved = function (uuid) {
          $('#wait').show();
          $serviceRequests.setAppointmentResolved(uuid).
             then(function (response) {
                $('#wait').hide();
                $serviceRequests.getAppointmentRequests($scope.appointmentsCurrentPage, $scope.pageSize).
                then(function (response) {
                    var serverData = response.data;
                    $scope.appointmentRequests = serverData.objects;
                    $scope.appointmentPages = serverData.pages;
                    $scope.appointmentsTotalItems = serverData.totalItems;
                    $('#wait').hide();
                });
             },function (response) {
                 $('#wait').hide();
             });
      };
}