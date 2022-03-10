<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Edit Patients" otherwise="/login.htm" redirect="/module/mycarehub/mycarehub.list"/>

<openmrs:htmlInclude file="/moduleResources/mycarehub/styles/bootstrap-4.5.3/css/bootstrap.css"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/jquery/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/mycarehub/styles/bootstrap-4.5.3/js/bootstrap.min.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular-route.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular-resource.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular-sanitize.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/ui-bootstrap/ui-bootstrap-3.0.6.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/ui-bootstrap/ui-bootstrap-tpls-3.0.6.min.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular-strap.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/custom/filters.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/custom/app.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular-translate.min.js" />
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular-translate-interpolation-messageformat.min.js" />
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/angular/angular-translate-loader-static-files.min.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/custom/controllers/AppointmentRequestsController.js"/>
<openmrs:htmlInclude file="/moduleResources/mycarehub/js/custom/controllers/ServiceRequestsController.js"/>

<div class="bootstrap-scope" ng-app="mycarehubModule">
    <div ng-view ></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>