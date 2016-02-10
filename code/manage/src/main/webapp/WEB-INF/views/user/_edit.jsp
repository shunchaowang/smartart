<%@include file="../taglib.jsp" %>
<spring:message var="cancelAction" code="action.cancel.label"/>
<spring:message var="entity" code="user.label"/>
<spring:message code="edit.label" var="editLabel" arguments="${entity}"/>

<div id="edit-dialog" title="${editLabel}">
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
                <form class="form-horizontal" id="edit-form">
                    <input type="hidden" name="userId" id="userId"
                           value="${userCommand.id}"/>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="username">
                            <span class="required-indicator">*</span>
                            <spring:message code="username.label"/>
                        </label>

                        <div class="col-sm-4">
                            <input size="32" name="username" id="username" class="form-control"
                                   required="" value="${userCommand.username}" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="lastName">
                            <span class="required-indicator">*</span>
                            <spring:message code="lastName.label"/>
                        </label>

                        <div class="col-sm-4">
                            <input size="80" id="lastName" name="lastName"
                                   class="form-control"
                                   required="" value="${userCommand.lastName}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="firstName">
                            <span class="required-indicator">*</span>
                            <spring:message code="firstName.label"/>
                        </label>

                        <div class="col-sm-4">
                            <input size="80" id="firstName" name="firstName"
                                   class="form-control"
                                   required="" value="${userCommand.firstName}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="email">
                            <span class="required-indicator">*</span>
                            <spring:message code="email.label"/>
                        </label>

                        <div class="col-sm-4">
                            <input size="80" id="email" name="email"
                                   class="form-control"
                                   required="" value="${userCommand.email}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="remark">
                            <spring:message code="remark.label"/>
                        </label>

                        <div class="col-sm-4">
                            <textarea cols="100" rows="5" name="remark"
                                      id="remark" class="form-control">
                                ${userCommand.remark}
                            </textarea>
                        </div>
                    </div>
                    <!-- user status -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="userStatus">
                            <span class="required-indicator">*</span>
                            <spring:message code="status.label"/>
                        </label>

                        <div class="col-sm-4">
                            <select id="userStatus" class="form-control" name="userStatus"
                                    required="" value="${userCommand.userStatus}">
                                <c:forEach items="${userStatuses}" var="status">
                                    <c:choose>
                                        <c:when test="${status.id == userCommand.userStatus}">
                                            <option value="${status.id}" selected>
                                                    ${status.name}
                                            </option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${status.id}">
                                                    ${status.name}
                                            </option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class='form-group'>
                        <div class="col-sm-2 col-sm-offset-2">
                            <button class='btn btn-default' id='save-button' type="submit">
                                <spring:message code='action.save.label'/>
                            </button>
                        </div>
                        <div class="col-sm-2">
                            <button class='btn btn-default' id='cancel-button'>
                                <spring:message code='action.cancel.label'/>
                            </button>
                        </div>
                        <div class="col-sm-2">
                            <button class='btn btn-default' id='reset-button' type="reset">
                                <spring:message code='action.reset.label'/>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $("#edit-form").validate({
            rules: {
                lastName: {required: true, minlength: 3, maxlength: 32},
                firstName: {required: true, minlength: 3, maxlength: 32},
                email: {required: true, email: true, minlength: 3, maxlength: 32}
            }
        });
    });
</script>
