<!DOCTYPE html>
<%@include file="../taglib.jsp" %>
<spring:message code="user.label" var="entity"/>
<spring:message code="username.cannot.contain.slash.message" var="noSlashMsg"/>

<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>
                <i class="glyphicon glyphicon-home"></i>
                <spring:message code="home.label"/>
            </li>
            <li>
                <i class="glyphicon glyphicon-list"></i>
                <spring:message code="index.label" arguments="${entity}"/>
            </li>
            <li class="active">
                <i class="glyphicon glyphicon-wrench"></i>
                <spring:message code="create.label" arguments="${entity}"/>
            </li>
        </ol>
    </div>
    <div class="row">
        <div class="col-sm-8">
            <form:form action="${rootURL}user/create" method="POST"
                       commandName="userCommand" cssClass="form-horizontal"
                       id="new-user-form">
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="username">
                        <span class="required-indicator">*</span>
                        <spring:message code="username.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="32" path="username" id="username"
                                    cssClass="form-control" required=""
                                    placeholder="Username"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="email">
                        <span class="required-indicator">*</span>
                        <spring:message code="email.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="32" path="email" id="email" cssClass="form-control"
                                    required=""
                                    placeholder="Email"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="firstName">
                        <span class="required-indicator">*</span>
                        <spring:message code="firstName.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="32" path="firstName" id="firstName"
                                    cssClass="form-control"
                                    required=""
                                    placeholder="First Name"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="lastName">
                        <span class="required-indicator">*</span>
                        <spring:message code="lastName.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:input size="32" path="lastName" id="lastName" cssClass="form-control"
                                    required=""
                                    placeholder="Last Name"/>
                    </div>
                </div>
                <!-- user status -->
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="userStatus">
                        <span class="required-indicator">*</span>
                        <spring:message code="status.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:select path="userStatus" id="userStatus" cssClass="form-control"
                                     required="">
                            <c:forEach items="${userStatuses}" var="status">
                                <form:option value="${status.id}">${status.name}</form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="remark">
                        <spring:message code="remark.label"/>
                    </label>

                    <div class="col-sm-4">
                        <form:textarea cols="100" rows="5" path="remark" id="remark"
                                       cssClass="form-control"/>
                    </div>
                </div>
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
        $.validator.addMethod("usernameCheck", function (value, element) {

            // return true if the value does not contain /
            return value.indexOf("/") < 0;
        }, "${noSlashMsg}");
        $('#new-user-form').validate({
            rules: {
                username: {required: true, minlength: 3, maxlength: 32, usernameCheck: true},
                firstName: {required: true, minlength: 1, maxlength: 32},
                lastName: {required: true, minlength: 1, maxlength: 32},
                email: {required: true, email: true, minlength: 3, maxlength: 32},
                userStatus: {required: true}
            }
        });
    });
</script>
