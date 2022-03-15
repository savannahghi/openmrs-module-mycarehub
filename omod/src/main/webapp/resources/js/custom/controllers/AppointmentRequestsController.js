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

    $scope.appointmentPagination = function (appointmentsCurrentPage) {
        $('#wait').show();
        $scope.appointmentsCurrentPage = appointmentsCurrentPage;
        $serviceRequests.getAppointmentRequests($scope.appointmentsCurrentPage, $scope.pageSize).
        then(function (response) {
            var serverData = response.data;
            $scope.appointmentRequests = serverData.objects;
            $scope.appointmentPages = serverData.pages;
            $scope.appointmentsTotalItems = serverData.totalItems;
            $('#wait').hide();
        });
    };

    $scope.$watch('appointmentsCurrentPage', function (newValue, oldValue) {
        if (newValue != oldValue) {

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

    $scope.healthDiaryPagination = function (healthDiaryCurrentPage) {
        $('#wait').show();
        $scope.healthDiaryCurrentPage = healthDiaryCurrentPage;
        $serviceRequests.getHealthDiaries($scope.healthDiaryCurrentPage, $scope.pageSize).
        then(function (response) {
            var serverData = response.data;
            $scope.healthDiaries = serverData.objects;
            $scope.healthDiariesPages = serverData.pages;
            $scope.healthDiariesTotalItems = serverData.totalItems;
            $('#wait').hide();
        });
    };
//Red Flags
    $serviceRequests.getServiceRequests("RED_FLAG", $scope.redFlagCurrentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.redFlags = serverData.objects;
        $scope.redFlagsPages = serverData.pages;
        $scope.redFlagsTotalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.redFlagPagination = function (redFlagCurrentPage) {
        $('#wait').show();
        $scope.redFlagCurrentPage = redFlagCurrentPage;
        $serviceRequests.getServiceRequests("RED_FLAG", $scope.redFlagCurrentPage, $scope.pageSize).
        then(function (response) {
            var serverData = response.data;
            $scope.redFlags = serverData.objects;
            $scope.redFlagsPages = serverData.pages;
            $scope.redFlagsTotalItems = serverData.totalItems;
            $('#wait').hide();
        });
    };

//Screening Tool Red Flags
    $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolRedFlagCurrentPage, $scope.pageSize).
    then(function (response) {
        var serverData = response.data;
        $scope.screeningToolRedFlags = serverData.objects;
        $scope.screeningToolRedFlagPages = serverData.pages;
        $scope.screeningToolRedFlagTotalItems = serverData.totalItems;
        $('#wait').hide();
    });

    $scope.screeningToolRedFlagPagination = function (screeningToolRedFlagCurrentPage) {
        $('#wait').show();
        $scope.screeningToolRedFlagCurrentPage = screeningToolRedFlagCurrentPage;
        $serviceRequests.getServiceRequests("SCREENING_TOOL_RED_FLAG", $scope.screeningToolRedFlagCurrentPage, $scope.pageSize).
        then(function (response) {
           var serverData = response.data;
           $scope.screeningToolRedFlags = serverData.objects;
           $scope.screeningToolRedFlagPages = serverData.pages;
           $scope.screeningToolRedFlagTotalItems = serverData.totalItems;
           $('#wait').hide();
        });
    };

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