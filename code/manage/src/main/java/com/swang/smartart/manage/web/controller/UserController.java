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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

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
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
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
        // formulate criteria query
        // if active == false means archive, no role
        // support ad hoc search on username only
        // support order on id and createdTime only
        User includedUser = new User();
        includedUser.setActive(true);

        List<User> users = userService.findByCriteria(
                includedUser,
                params.getSearch(),
                Integer.valueOf(params.getOffset()),
                Integer.valueOf(params.getMax()), params.getOrder(),
                ResourceProperties.JpaOrderDir.valueOf(params.getOrderDir()));

        // count total records
        Long recordsTotal = userService
                .countByCriteria(includedUser);

        // count records filtered
        Long recordsFiltered = userService
                .countByCriteria(includedUser, params.getSearch());

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

        User includedUser = new User();
        includedUser.setActive(true);

        List<User> users = userService.findByCriteria(
                includedUser,
                params.getSearch(),
                Integer.valueOf(params.getOffset()),
                Integer.valueOf(params.getMax()), params.getOrder(),
                ResourceProperties.JpaOrderDir.valueOf(params.getOrderDir()));

        // count total records
        Long recordsTotal = userService
                .countByCriteria(includedUser);
        // count records filtered
        Long recordsFiltered = userService
                .countByCriteria(includedUser, params.getSearch());

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

}
