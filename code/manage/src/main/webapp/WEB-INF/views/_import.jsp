<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="taglib.jsp" %>

<title><spring:message code="application.title"/></title>

<!-- jquery -->
<script type="text/javascript"
        src="${rootURL}assets/vendor/jquery-2.1.3/jquery-2.1.3.min.js"></script>

<!-- bootstrap depending on jquery -->
<link type="text/css" rel="stylesheet"
      href="${rootURL}assets/vendor/bootstrap-3.3.2/css/bootstrap.min.css"/>
<script type="text/javascript"
        src="${rootURL}assets/vendor/bootstrap-3.3.2/js/bootstrap.min.js"></script>

<!-- jquery-ui depending on jquery -->
<link type="text/css" rel="stylesheet"
      href="${rootURL}assets/vendor/jquery-ui-1.11.2/jquery-ui.min.css"/>
<link type="text/css" rel="stylesheet"
      href="${rootURL}assets/vendor/jquery-ui-1.11.2/jquery-ui.structure.min.css"/>
<link type="text/css" rel="stylesheet"
      href="${rootURL}assets/vendor/jquery-ui-1.11.2/jquery-ui.theme.min.css"/>
<script type="text/javascript"
        src="${rootURL}assets/vendor/jquery-ui-1.11.2/jquery-ui.min.js"></script>

<!-- jquery-validation depending on jquery -->
<script type="text/javascript"
        src="${rootURL}assets/vendor/jquery-validation-1.13.1/jquery.validate.min.js"></script>
<!-- jquery validation locale -->
<script type="text/javascript"
        src="${rootURL}assets/vendor/jquery-validation-1.13.1/localization/messages_${currentLocale}.min.js"></script>

<!-- DataTables depending on jquery -->
<link type="text/css" rel="stylesheet"
      href="${rootURL}assets/vendor/DataTables-1.10.4/css/jquery.dataTables.min.css"/>
<script type="text/javascript"
        src="${rootURL}assets/vendor/DataTables-1.10.4/js/jquery.dataTables.min.js"></script>

<!-- TableTools depending on DataTables jquery -->
<link type="text/css" rel="stylesheet"
      href="${rootURL}assets/vendor/TableTools-2.2.3/css/dataTables.tableTools.min.css"/>
<script type="text/javascript"
        src="${rootURL}assets/vendor/TableTools-2.2.3/js/dataTables.tableTools.min.js"></script>

<!-- select2 -->
<!--
<link type="text/css" rel="stylesheet"
href="${rootURL}assets/vendor/select2/css/select2.css"/>
<script type="text/javascript"
src="${rootURL}assets/vendor/select2/js/select2.min.js"></script>
-->

<!-- customized css and js -->
<link type="text/css" rel="stylesheet" href="${rootURL}assets/css/app.css"/>
<script type="text/javascript" src="${rootURL}assets/js/app.js"></script>
