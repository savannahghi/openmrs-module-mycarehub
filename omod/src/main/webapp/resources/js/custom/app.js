var mycarehubModule = angular.module('mycarehubModule', ['ui.bootstrap', 'ngRoute', 'ngSanitize',  'pascalprecht.translate']);

mycarehubModule.
    config(['$routeProvider', '$compileProvider', '$translateProvider', function ($routeProvider, $compileProvider, $translateProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file):/);
        $routeProvider.
            when('/serviceRequests', {controller: AppointmentRequestsCtrl, templateUrl: '../../moduleResources/mycarehub/partials/serviceRequests.html'}).

            otherwise({redirectTo: '/serviceRequests'});
    }]
);

mycarehubModule.factory('$appointmentRequests', function ($http) {
        var getAppointmentRequests = function (pageNumber, pageSize) {
        return $http.get("appointmentRequests.json?pageNumber=" + pageNumber + "&pageSize=" + pageSize);
    };

    return {
        getAppointmentRequests: getAppointmentRequests
    }
});

mycarehubModule.factory('$healthDiary', function ($http) {
        var getHealthDiaries = function (pageNumber, pageSize) {
        return $http.get("healthDiaries.json?pageNumber=" + pageNumber + "&pageSize=" + pageSize);
    };

    return {
        getHealthDiaries: getHealthDiaries
    }
});

mycarehubModule.factory('$serviceRequests', function ($http) {
        var getServiceRequests = function (requestType, pageNumber, pageSize) {
        return $http.get("serviceRequests.json?requestType=" +requestType+ "&pageNumber=" + pageNumber + "&pageSize=" + pageSize);
    };

    return {
        getServiceRequests: getServiceRequests
    }
});