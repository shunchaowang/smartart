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
                <spring:message code="user.change.password.label" />
            </li>
        </ol>
    </div>
    <div class="row">
        <div class="col-sm-8">
            <form:form class="form-horizontal" method="post"
                       commandName="passwordCommand"
                       action="${rootURL}home/password"
                       id="change-password">
                <form:input path="id" type='hidden' value="${passwordCommand.id}"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <span class="required-indicator">*</span>
                        <spring:message code="currentPassword.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input type="password" size="32" path="currentPassword"
                                    class="text"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <span class="required-indicator">*</span>
                        <spring:message code="password.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input type="password" size="32" path="password"
                                    id="password"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <span class="required-indicator">*</span>
                        <spring:message code="confirmPassword.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input type="password" size="32" path="confirmPassword"
                                    id="confirmPassword"/>
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
                        <button class='btn btn-default' id='reset-button' type="reset">
                            <spring:message code='action.reset.label'/>
                        </button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $("#change-password").validate({
            rules: {
                currentPassword: {
                    required: true,
                    minlength: 5,
                    maxlength: 32
                },
                password: {
                    required: true,
                    minlength: 5,
                    maxlength: 32
                },
                confirmPassword: {
                    required: true,
                    minlength: 5,
                    maxlength: 32,
                    equalTo: "#password"
                }
            }
        });
    });
</script>
