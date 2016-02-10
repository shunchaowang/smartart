<!DOCTYPE html>
<%@include file="../taglib.jsp" %>

<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>
                <i class="glyphicon glyphicon-home"></i>
                <spring:message code="home.label"/>
            </li>
            <li class="active">
                <i class="glyphicon glyphicon-wrench"></i>
                <spring:message code="user.profile.label"/>
            </li>
        </ol>
    </div>
    <div class="row">
        <div class="col-sm-8">
            <form:form action="${rootURL}home/profile" method="POST"
                       commandName="userCommand" cssClass="form-horizontal"
                       id="edit-user-form">
                <form:input path="id" id="id" type="hidden" value="${userCommand.id}"/>

                <div class="form-group">
                    <label class="col-sm-2 control-label" for="username">
                        <spring:message code="username.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="80" path="username" id="username"
                                    cssClass="text"
                                    value="${userCommand.username}" readonly="true"/>
                    </div>
                </div>

                <!-- first name -->
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="firstName">
                        <spring:message code="firstName.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="80" path="firstName" id="firstName"
                                    cssClass="text"
                                    required=""
                                    value="${userCommand.firstName}"/>
                    </div>
                </div>
                <!-- last name -->
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="lastName">
                        <spring:message code="lastName.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="80" path="lastName" id="lastName"
                                    cssClass="text" required=""
                                    value="${userCommand.lastName}"/>
                    </div>
                </div>
                <!-- email -->
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="email">
                        <spring:message code="email.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="80" path="email" id="email" cssClass="text"
                                    required=""
                                    value="${userCommand.email}"/>
                    </div>
                </div>
                <!-- remark -->
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="remark">
                        <spring:message code="remark.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:textarea cols="100" rows="5" path="remark" id="remark"
                                       value="${userCommand.remark}"/>
                    </div>
                </div>
                <!-- buttons -->
                <div class='form-group'>
                    <div class="col-sm-2 col-sm-offset-2">
                        <button class='btn btn-default' id='create-button' type="submit">
                            <spring:message code='action.save.label'/>
                        </button>
                    </div>
                    <div class="col-sm-2">
                        <a href="${rootURL}">
                            <button type="button" class="btn btn-default">
                                <spring:message code="action.return.label"/>
                            </button>
                        </a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#edit-user-form').validate({
            rules: {
                firstName: {required: true, minlength: 3, maxlength: 32},
                lastName: {required: true, minlength: 3, maxlength: 32},
                email: {required: true, minlength: 3, maxlength: 32},
                userStatus: {required: true}
            }
        });
    });
</script>