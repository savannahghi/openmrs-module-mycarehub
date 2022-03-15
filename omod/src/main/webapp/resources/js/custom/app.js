var mycarehubModule = angular.module('mycarehubModule', ['ui.bootstrap', 'ngRoute', 'ngSanitize',  'pascalprecht.translate']);

mycarehubModule.
    config(['$routeProvider', '$compileProvider', '$translateProvider', function ($routeProvider, $compileProvider, $translateProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file):/);
        $routeProvider.
            when('/serviceRequests', {controller: AppointmentRequestsCtrl, templateUrl: '../../moduleResources/mycarehub/partials/serviceRequests.html'}).

            otherwise({redirectTo: '/serviceRequests'});
    }]
);

mycarehubModule.factory('$serviceRequests', function ($http) {
    var getServiceRequests = function (requestType, pageNumber, pageSize) {
        return $http.get("serviceRequests.json?requestType=" +requestType+ "&pageNumber=" + pageNumber + "&pageSize=" + pageSize);
    };

    var getAppointmentRequests = function (pageNumber, pageSize) {
        return $http.get("appointmentRequests.json?pageNumber=" + pageNumber + "&pageSize=" + pageSize);
    };

    var getHealthDiaries = function (pageNumber, pageSize) {
        return $http.get("healthDiaries.json?pageNumber=" + pageNumber + "&pageSize=" + pageSize);
    };

    var setRedFlagInProgress = function(uuid){
        return $http.post("setRedFlagInProgress.json?uuid=" + uuid);
    }

    var setRedFlagResolved = function(uuid){
        return $http.post("setRedFlagResolved.json?uuid=" + uuid);
    }
    
    var setAppointmentInProgress = function(uuid){
        return $http.post("setAppointmentInProgress.json?uuid=" + uuid);
    }

    var setAppointmentResolved = function(uuid){
        return $http.post("setAppointmentResolved.json?uuid=" + uuid);
    }

    return {
        getServiceRequests: getServiceRequests,
        getAppointmentRequests: getAppointmentRequests,
        getHealthDiaries: getHealthDiaries,
        setRedFlagInProgress: setRedFlagInProgress,
        setRedFlagResolved: setRedFlagResolved,
        setAppointmentInProgress: setAppointmentInProgress,
        setAppointmentResolved: setAppointmentResolved
    }
});