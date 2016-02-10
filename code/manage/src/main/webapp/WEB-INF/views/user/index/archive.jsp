<%@include file="../../taglib.jsp" %>

<spring:message code="user.label" var="userLabel"/>
<spring:message code="restore.confirm.message" arguments="${entity}" var="restoreMsg"/>
<spring:message code="action.delete.label" var="deleteLabel"/>
<spring:message code="action.cancel.label" var="cancelLabel"/>
<spring:message code="action.restore.label" var="restoreLabel"/>

<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li>
                <i class="glyphicon glyphicon-home"></i>
                <spring:message code="home.label"/>
            </li>
            <li>
                <i class="glyphicon glyphicon-list"></i>
                <spring:message code="manage.label" arguments="${userLabel}"/>
            </li>
            <li class="active">
                <i class="glyphicon glyphicon-list"></i>
                <spring:message code="archive.label" arguments="${userLabel}"/>
            </li>
        </ol>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <table class="table table-bordered" id="user-table">
                <thead>
                <tr>
                    <th><spring:message code="id.label"/></th>
                    <th><spring:message code="username.label"/></th>
                    <th><spring:message code="firstName.label"/></th>
                    <th><spring:message code="lastName.label"/></th>
                    <th><spring:message code="email.label"/></th>
                    <th><spring:message code="createdTime.label"/></th>
                    <th><spring:message code="status.label"/></th>
                    <th><spring:message code="action.operation.label"/></th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>
<div class="confirmDialog" id="confirm-dialog">
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var userTable = $('#user-table').DataTable({
            'language': {
                'url': "${dataTablesLanguage}"
            },
            'processing': true,
            'serverSide': true,
            'paging': true,
            "paginationType": "full_numbers",
            "order": [[0, "desc"]],
            "tableTools": {
                "sSwfPath": "${tableTools}",
                "aButtons": [
                    {
                        "sExtends": "copy",
                        "mColumns": [1, 2, 3, 4, 5, 6]
                    },
                    {
                        "sExtends": "xls",
                        "mColumns": [1, 2, 3, 4, 5, 6]
                    }
                ]
            },
            'ajax': {
                'url': "${rootURL}user/list/archive",
                'type': "GET",
                'dataType': 'json'
            },
            // MUST HAVE DATA ON COLUMNDEFS IF SERVER RESPONSE IS JSON ARRAY!!!
            'columnDefs': [
                {'name': 'id', 'targets': 0, 'visible': false, 'data': 'id'},
                {
                    'name': 'username', 'targets': 1, 'data': 'username',
                    'orderable': false
                },
                {
                    'name': 'firstName', 'targets': 2, 'data': 'firstName', 'searchable': false,
                    'orderable': false
                },
                {
                    'name': 'lastName', 'targets': 3, 'data': 'lastName', 'searchable': false,
                    'orderable': false
                },
                {
                    'name': 'email', 'targets': 4, 'data': 'email', 'searchable': false,
                    'orderable': false
                },
                {'name': 'createdTime', 'targets': 5, 'searchable': false, 'data': 'createdTime'},
                {
                    'name': 'userStatus', 'targets': 6, 'searchable': false,
                    'orderable': false, 'data': 'userStatus'
                },
                {
                    'name': 'operation', 'targets': 7, 'searchable': false, 'orderable': false,
                    'render': function (data, type, row) {
                        return '<button type="button" name="restore-button"'
                                + ' data-identity="' + row['username'] + '"'
                                + ' class="btn btn-default" value="' + row['id'] + '">' +
                                "${restoreLabel}"
                                + '</button>'
                                + '<button type="button" name="delete-button"'
                                + ' data-identity="' + row['username'] + '"'
                                + ' class="btn btn-default" value="' + row['id'] + '">' +
                                "${deleteLabel}"
                                + '</button>';
                    }
                }
            ]
        });

        // add live handler for restore button
        userTable.on('click', 'button[type=button][name=restore-button]', function (event) {
            event.preventDefault();
            var id = this.value;
            var username = $(this).data("username");
            $("#confirm-dialog").dialog({
                resizable: true,
                height: 'auto',
                width: 'auto',
                modal: true,
                open: function () {
                    var content = "${restoreMsg}" + ' ' + username;
                    $(this).html(content);
                },
                buttons: {
                    "${restoreLabel}": function () {
                        $(this).dialog("close");
                        $.ajax({
                            type: 'POST',
                            url: "${rootURL}user" + '/restore',
                            data: {id: id},
                            dataType: 'JSON',
                            error: function (error) {
                                alert('There was an error');
                            },
                            success: function (data) {
                                var alert = "<div class='alert alert-warning alert-dismissible' role='alert'>" +
                                        "<button type='button' class='close' data-dismiss='alert'>" +
                                        "<span aria-hidden='true'>&times;</span>" +
                                        "<span class='sr-only'>"
                                        + "<spring:message code='action.close.label'/> "
                                        + "</span></button>"
                                        + data.message + "</div>";
                                $('#notification').append(alert);
                                userTable.ajax.reload();
                            }
                        });
                    },
                    "${cancelLabel}": function () {
                        $(this).dialog("close");
                    }
                }
            });
        });

        // add live handler for delete button
        userTable.on('click', 'button[type=button][name=delete-button]', function (event) {
            event.preventDefault();
            var id = this.value;
            var username = $(this).data("username");
            $("#confirm-dialog").dialog({
                resizable: true,
                height: 'auto',
                width: 'auto',
                modal: true,
                open: function () {
                    var content = "${deleteMsg}" + ' ' + username;
                    $(this).html(content);
                },
                buttons: {
                    "${deleteLabel}": function () {
                        $(this).dialog("close");
                        $.ajax({
                            type: 'POST',
                            url: "${rootURL}user" + '/delete',
                            data: {id: id},
                            dataType: 'JSON',
                            error: function (error) {
                                alert('There was an error');
                            },
                            success: function (data) {
                                var alert = "<div class='alert alert-warning alert-dismissible' role='alert'>" +
                                        "<button type='button' class='close' data-dismiss='alert'>" +
                                        "<span aria-hidden='true'>&times;</span>" +
                                        "<span class='sr-only'>"
                                        + "<spring:message code='action.close.label'/> "
                                        + "</span></button>"
                                        + data.message + "</div>";
                                $('#notification').append(alert);
                                userTable.ajax.reload();
                            }
                        });
                    },
                    "${cancelLabel}": function () {
                        $(this).dialog("close");
                    }
                }
            });
        });
    });
</script>
