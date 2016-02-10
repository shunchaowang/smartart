<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.swang.smartart.manage.web.controller.UserResource" %>
<!DOCTYPE html>
<%@include file="taglib.jsp" %>

<c:set var="username" value="<%=UserResource.getCurrentUser().getUsername()%>" scope="session"/>

<div id="content">
    <div id="content-header">
        <div id="breadcrumb">
            <a href="${rootURL}">
                <i class="icon icon-home"></i>
                <spring:message code="home.label"/>
            </a>
        </div>
    </div>

    <!-- close of content-header -->

    <h2>Welcome ${username}
    </h2>

    <h2>Authorities <%=UserResource.getCurrentUser().getAuthorities()%>
    </h2>

    <h2>Controller: ${controller}</h2>

    <h2>Action: ${action}</h2>

    <h2>View: ${_view}</h2>

    <h3>Name: <sec:authentication property="name"/></h3>

    <h3>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <a href="${rootURL}admin">Administration</a>
        </sec:authorize>
    </h3>

    <h3>
        PageContext: ${pageContext.request.contextPath}
        <br>
        rootURL: ${rootURL}
    </h3>

    <a href="${rootURL}user/index/all">User Table</a><br/>
    <a href="${rootURL}user/list/all">User in Json Array</a>

    <p><a href="${rootURL}logout">Logout</a></p>

</div>
