<%@ page import="com.swang.smartart.manage.web.controller.UserResource" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="taglib.jsp" %>

<sec:authorize access="isAuthenticated()">
    <!-- navbar -->

    <!-- define domain related vars -->
    <spring:message code='user.label' var="user"/>
    <spring:message code='admin.label' var="admin"/>
    <spring:message code='log.label' var="log"/>
    <spring:message code='system.label' var="system"/>
    <spring:message code='permission.label' var="permission"/>

    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${rootURL}">
                    <!--<img alt="Brand" src="">-->
                    <spring:message code="application.name"/>
                </a>
            </div>
            <!-- end of navbar header -->

            <!-- use this if going with role based authorization -->
            <sec:authorize access="hasAuthority('ROLE_ADMIN')">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="#">
                            <spring:message code="admin.label"/>
                        </a>
                    </li>
                </ul>
            </sec:authorize>

            <!-- use this if going with permission based authorization -->
            <c:set var="menus" value="<%=UserResource.getMenu()%>" scope="session"/>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <ul class="nav navbar-nav">
                    <c:forEach items="${menus}" var="menu">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-expanded="false">
                                <spring:message code="menu.${menu.name}.label"/>
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu" role="menu">
                                <c:forEach items="${menu.subMenus}" var="subMenu">
                                    <li>
                                        <a href="${rootURL}${subMenu.target}">
                                            <spring:message code="subMenu.${subMenu.name}.label"/>
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>

                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a class="dropdown-toggle" role="button" data-toggle="dropdown"
                           data-target="#" href="#">
                            <!-- TODO: Only show menu items based on permissions (e.g., Guest has no account page) -->
                            <i class="icon icon-user"></i>
                            <sec:authentication property="principal.username"/>
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="${rootURL}logout">
                                    <i class="icon icon-off"></i>
                                    <spring:message code="user.logout.label"/>
                                </a>
                            </li>
                            <li>
                                <a href="${rootURL}home/password">
                                    <spring:message code="user.change.password.label"/>
                                </a>
                            </li>
                            <li>
                                <a href="${rootURL}home/profile">
                                    <spring:message code="user.profile.label"/>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <!-- end of user profile -->
            </div>
            <!-- end of navbar-collapse -->
        </div>
        <!-- end of container-fluid -->
    </nav>

</sec:authorize>

<!-- end navbar-collapse -->


