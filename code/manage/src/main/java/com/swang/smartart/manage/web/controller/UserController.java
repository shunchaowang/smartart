package com.swang.smartart.manage.web.controller;

import com.swang.smartart.core.exception.MissingRequiredFieldException;
import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.exception.NotUniqueException;
import com.swang.smartart.core.persistence.entity.Role;
import com.swang.smartart.core.persistence.entity.User;
import com.swang.smartart.core.persistence.entity.UserStatus;
import com.swang.smartart.core.service.RoleService;
import com.swang.smartart.core.service.UserService;
import com.swang.smartart.core.service.UserStatusService;
import com.swang.smartart.core.util.ResourceProperties;
import com.swang.smartart.manage.util.DataTablesParams;
import com.swang.smartart.manage.util.JsonUtil;
import com.swang.smartart.manage.web.exception.BadRequestException;
import com.swang.smartart.manage.web.exception.IntervalServerException;
import com.swang.smartart.manage.web.exception.RemoteAjaxException;
import com.swang.smartart.manage.web.vo.UserCommand;
import com.swang.smartart.manage.web.vo.table.DataTablesResultSet;
import com.swang.smartart.manage.web.vo.table.DataTablesUser;
import com.swang.smartart.manage.web.vo.table.JsonResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by swang on 3/20/2015.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private MessageSource messageSource;
    @Resource
    private UserService userService;
    @Resource
    private UserStatusService userStatusService;
    @Resource
    private RoleService roleService;
    @Resource
    private PasswordEncoder passwordEncoder;

    // index view
    // Role and Authority are the same, hasRole and hasAuthority are the same as well.
    // hasAuthority('ROLE_ADMIN') has same result with hasRole('ROLE_ADMIN')
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = {"/indexAll"}, method = RequestMethod.GET)
    public String indexAll(Model model) {
        model.addAttribute("_view", "user/indexAll");
        return "main";
    }

    // archive view
    @PreAuthorize("isAuthenticated() and hasPermission('', 'PERMISSION_ADMIN')")
    @RequestMapping(value = {"/indexArchive"}, method = RequestMethod.GET)
    public String indexArchive(Model model) {
        model.addAttribute("_view", "user/indexArchive");
        return "main";
    }

    // ajax for DataTables
    @RequestMapping(value = "/listAll", method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listAll(HttpServletRequest request) {

        DataTablesParams params = new DataTablesParams(request);

        if (params.getOffset() == null || params.getMax() == null
                || params.getOrder() == null || params.getOrderDir() == null) {
            throw new BadRequestException("400", "Bad Request.");
        }

        CriteriaBuilder builder = userService.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<User> root = countQuery.from(User.class);
        countQuery.select(builder.count(root));
        // we only cares about user inactive
        Predicate activePredicate = builder.equal(root.<Boolean>get("active"),
                builder.literal(true));
        countQuery.where(activePredicate);
        Long recordsTotal = userService.count(countQuery);

        // count records filtered
        String likeSearch = "%" + params.getSearch() + "%";

        // get all paths for the query
        Path<String> usernamePath = root.get("username");
        Path<String> firstNamePath = root.get("firstName");
        Path<String> lastNamePath = root.get("lastName");
        Path<String> emailPath = root.get("email");

        // create the predicate expression for all the path
        Predicate usernamePredicate = builder.like(usernamePath, likeSearch);
        Predicate firstNamePredicate = builder.like(firstNamePath, likeSearch);
        Predicate lastNamePredicate = builder.like(lastNamePath, likeSearch);
        Predicate emailPredicate = builder.like(emailPath, likeSearch);

        Predicate searchPredicate = builder.or(usernamePredicate, firstNamePredicate,
                lastNamePredicate, emailPredicate);
        countQuery.where(activePredicate, searchPredicate);
        Long recordsFiltered = userService.count(countQuery);

        // get all users
        CriteriaQuery<User> userQuery = builder.createQuery(User.class);
        Root<User> userRoot = userQuery.from(User.class);
        userQuery.select(userRoot);
        userQuery.where(activePredicate, searchPredicate);

        // formulate orderBy
        String order = params.getOrder();
        if (StringUtils.isBlank(order)) {
            order = "id";
        }
        String orderDirParam = params.getOrderDir();
        if (StringUtils.isBlank(orderDirParam)) {
            orderDirParam = ResourceProperties.JPA_ORDER_DESC;
        }

        userQuery.orderBy(orderUserBy(builder, root, order, orderDirParam));
        List<User> users = userService.
                find(userQuery, Integer.valueOf(params.getOffset()), Integer.valueOf(params.getMax()));

        if (users == null || recordsTotal == null || recordsFiltered == null) {
            throw new RemoteAjaxException("500", "Internal Server Error.");
        }

        List<DataTablesUser> dataTablesUsers = new ArrayList<>();

        for (User user : users) {
            DataTablesUser tableUser = new DataTablesUser(user);
            dataTablesUsers.add(tableUser);
        }

        DataTablesResultSet<DataTablesUser> result = new DataTablesResultSet<>();
        result.setData(dataTablesUsers);
        result.setRecordsFiltered(recordsFiltered.intValue());
        result.setRecordsTotal(recordsTotal.intValue());
        logger.debug("All users: " + JsonUtil.toJson(result));
        return JsonUtil.toJson(result);
    }

    // ajax for DataTables
    @RequestMapping(value = "/listArchive", method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listArchive(HttpServletRequest request) {

        DataTablesParams params = new DataTablesParams(request);

        if (params.getOffset() == null || params.getMax() == null
                || params.getOrder() == null || params.getOrderDir() == null) {
            throw new BadRequestException("400", "Bad Request.");
        }

        CriteriaBuilder builder = userService.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<User> root = countQuery.from(User.class);
        countQuery.select(builder.count(root));
        // we only cares about user inactive
        Predicate activePredicate = builder.equal(root.<Boolean>get("active"),
                builder.literal(false));
        countQuery.where(activePredicate);
        Long recordsTotal = userService.count(countQuery);

        // count records filtered
        String likeSearch = "%" + params.getSearch() + "%";

        // get all paths for the query
        Path<String> usernamePath = root.get("username");
        Path<String> firstNamePath = root.get("firstName");
        Path<String> lastNamePath = root.get("lastName");
        Path<String> emailPath = root.get("email");

        // create the predicate expression for all the path
        Predicate usernamePredicate = builder.like(usernamePath, likeSearch);
        Predicate firstNamePredicate = builder.like(firstNamePath, likeSearch);
        Predicate lastNamePredicate = builder.like(lastNamePath, likeSearch);
        Predicate emailPredicate = builder.like(emailPath, likeSearch);

        Predicate searchPredicate = builder.or(usernamePredicate, firstNamePredicate,
                lastNamePredicate, emailPredicate);
        countQuery.where(activePredicate, searchPredicate);
        Long recordsFiltered = userService.count(countQuery);

        // get all users
        CriteriaQuery<User> userQuery = builder.createQuery(User.class);
        Root<User> userRoot = userQuery.from(User.class);
        userQuery.select(userRoot);
        userQuery.where(activePredicate, searchPredicate);

        // formulate orderBy
        String order = params.getOrder();
        if (StringUtils.isBlank(order)) {
            order = "id";
        }
        String orderDirParam = params.getOrderDir();
        if (StringUtils.isBlank(orderDirParam)) {
            orderDirParam = ResourceProperties.JPA_ORDER_DESC;
        }

        userQuery.orderBy(orderUserBy(builder, root, order, orderDirParam));
        List<User> users = userService.
                find(userQuery, Integer.valueOf(params.getOffset(), Integer.valueOf(params.getMax())));

        if (users == null || recordsTotal == null || recordsFiltered == null) {
            throw new RemoteAjaxException("500", "Internal Server Error.");
        }

        List<DataTablesUser> dataTablesUsers = new ArrayList<>();

        for (User user : users) {
            DataTablesUser tableUser = new DataTablesUser(user);
            dataTablesUsers.add(tableUser);
        }

        DataTablesResultSet<DataTablesUser> result = new DataTablesResultSet<>();
        result.setData(dataTablesUsers);
        result.setRecordsFiltered(recordsFiltered.intValue());
        result.setRecordsTotal(recordsTotal.intValue());

        return JsonUtil.toJson(result);
    }


    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {

        User user;
        try {
            user = userService.get(id);
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
            throw new BadRequestException("400", "User  " + id + " not found.");
        }
        UserCommand userCommand = new UserCommand(user);
        model.addAttribute("userCommand", userCommand);

        model.addAttribute("_view", "user/show");
        return "main";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        UserCommand command = new UserCommand();
        model.addAttribute("userCommand", command);
        List<UserStatus> userStatuses = userStatusService.getAll();
        model.addAttribute("userStatuses", userStatuses);

        model.addAttribute("_view", "user/create");
        return "main";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@ModelAttribute("userCommand") UserCommand userCommand) {

        // message locale
        Locale locale = LocaleContextHolder.getLocale();
        //TODO verify required fields

        Role role = null;
        try {
            role = roleService.findByCode(ResourceProperties.ROLE_USER_CODE);
        } catch (NoSuchEntityException e) {
            logger.info("Cannot find user role.");
            e.printStackTrace();
        }

        User user = createUser(userCommand, role);
        try {
            userService.create(user);
        } catch (MissingRequiredFieldException e) {
            e.printStackTrace();
            throw new IntervalServerException("500", e.getMessage());
        } catch (NotUniqueException e) {
            e.printStackTrace();
            throw new IntervalServerException("500", e.getMessage());
        }
        return "redirect:/user/index/all";
    }

    /**
     * Edit a user.
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(HttpServletRequest request) {

        String userId = request.getParameter("userId");
        if (StringUtils.isBlank(userId)) {
            throw new BadRequestException("400", "User id is blank.");
        }
        Long id = Long.valueOf(userId);
        User user;
        try {
            user = userService.get(id);
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
            throw new BadRequestException("400", "User " + id + " not found.");
        }

        UserCommand userCommand = new UserCommand(user);
        ModelAndView view = new ModelAndView("user/_edit");
        view.addObject("userCommand", userCommand);
        view.addObject("userStatuses", userStatusService.getAll());
        return view;
    }

    /**
     * Save a user.
     *
     * @param request request form client
     * @return message
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String update(HttpServletRequest request) {
        JsonResponse response = new JsonResponse();
        Locale locale = LocaleContextHolder.getLocale();
        String label = messageSource.getMessage("user.label", null, locale);
        User user = editUser(request);
        try {
            user = userService.update(user);
        } catch (NotUniqueException e) {
            e.printStackTrace();
            String notSavedMessage = messageSource.getMessage("not.saved.message",
                    new String[]{label, user.getUsername()}, locale);
            response.setMessage(notSavedMessage);
            throw new BadRequestException("400", e.getMessage());
        } catch (MissingRequiredFieldException e) {
            e.printStackTrace();
            String notSavedMessage = messageSource.getMessage("not.saved.message",
                    new String[]{label, user.getUsername()}, locale);
            response.setMessage(notSavedMessage);
            throw new BadRequestException("400", e.getMessage());
        }

        String message = messageSource.getMessage("saved.message",
                new String[]{label, user.getUsername()}, locale);
        response.setMessage(message);
        return JsonUtil.toJson(response);
    }

    // create a new User from a UserCommand
    private User createUser(UserCommand userCommand, Role role) {
        User user = new User();
        if (userCommand.getId() != null) {
            user.setId(userCommand.getId());
        }
        user.setUsername(userCommand.getUsername());
        user.setPassword(passwordEncoder.encode(ResourceProperties.INITIAL_PASSWORD));
        user.setActive(true);
        user.setFirstName(userCommand.getFirstName());
        user.setLastName(userCommand.getLastName());
        user.setEmail(userCommand.getEmail());
        user.setRemark(userCommand.getRemark());
        // we set user to be active right now
        if (role != null) {
            user.setRoles(new HashSet<>());
            user.getRoles().add(role);
        }

        // set UserStatus
        UserStatus userStatus = null;
        try {
            userStatus = userStatusService.get(userCommand.getUserStatus());
        } catch (NoSuchEntityException e) {
            logger.info("Cannot find user status " + userCommand.getUserStatus());
            e.printStackTrace();
        }
        user.setUserStatus(userStatus);
        return user;
    }

    // edit a User from a UserCommand
    private User editUser(HttpServletRequest request) {

        User user;
        try {
            user = userService.get(Long.valueOf(request.getParameter("id")));
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
            throw new BadRequestException("400", e.getMessage());
        }
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setRemark(request.getParameter("remark"));
        Long userStatusId = Long.valueOf(request.getParameter("userStatus"));
        UserStatus userStatus;
        try {
            userStatus = userStatusService.get(userStatusId);
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
            throw new IntervalServerException("500", e.getMessage());
        }
        user.setUserStatus(userStatus);
        return user;
    }

    private Order orderUserBy(CriteriaBuilder builder, Root<User> root, String order, String orderDir) {
        Order orderBy = null;
        Path<Long> idPath = root.get("id");
        Path<Date> createdTimePath = root.get("createdTime");
        Path<String> usernamePath = root.get("username");
        Path<String> firstNamePath = root.get("firstName");
        Path<String> lastNamePath = root.get("lastName");
        Path<String> emailPath = root.get("email");
        switch (orderDir) {
            case "ASC":
                switch (order) {
                    case "id":
                        orderBy = builder.asc(idPath);
                        break;
                    case "username":
                        orderBy = builder.asc(usernamePath);
                        break;
                    case "firstName":
                        orderBy = builder.asc(firstNamePath);
                        break;
                    case "lastName":
                        orderBy = builder.asc(lastNamePath);
                        break;
                    case "email":
                        orderBy = builder.asc(emailPath);
                        break;
                    case "createdTime":
                        orderBy = builder.asc(createdTimePath);
                        break;
                    default:
                        orderBy = builder.asc(idPath);
                }
                break;
            case "DESC":
                switch (order) {
                    case "id":
                        orderBy = builder.desc(idPath);
                        break;
                    case "username":
                        orderBy = builder.desc(usernamePath);
                        break;
                    case "firstName":
                        orderBy = builder.desc(firstNamePath);
                        break;
                    case "lastName":
                        orderBy = builder.desc(lastNamePath);
                        break;
                    case "email":
                        orderBy = builder.desc(emailPath);
                        break;
                    case "createdTime":
                        orderBy = builder.desc(createdTimePath);
                        break;
                    default:
                        orderBy = builder.desc(idPath);
                }
                break;
            default:
                orderBy = builder.desc(idPath);
                break;
        }

        return orderBy;
    }
}
