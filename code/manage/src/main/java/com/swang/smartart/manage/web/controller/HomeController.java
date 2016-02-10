package com.swang.smartart.manage.web.controller;

import com.swang.smartart.core.exception.MissingRequiredFieldException;
import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.exception.NotUniqueException;
import com.swang.smartart.core.persistence.entity.User;
import com.swang.smartart.core.service.UserService;
import com.swang.smartart.manage.config.SecurityUser;
import com.swang.smartart.manage.web.vo.PasswordCommand;
import com.swang.smartart.manage.web.vo.UserCommand;
import com.swang.smartart.manage.web.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by swang on 3/11/2015.
 */
@Controller
public class HomeController {

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = {"/", "/index"})
    public ModelAndView home(Model model) {
        model.addAttribute("_view", "index");
        return new ModelAndView("main");
    }

    // for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView view = new ModelAndView();
        view.setViewName("403");
        return view;
    }

    @RequestMapping(value = "/home/password", method = RequestMethod.GET)
    public String changePassword(Model model) {

        PasswordCommand passwordCommand = new PasswordCommand();
        model.addAttribute("passwordCommand", passwordCommand);
        model.addAttribute("_view", "home/password");
        return "main";
    }

    @RequestMapping(value = "/home/password", method = RequestMethod.POST)
    public String updatePassword(@ModelAttribute("passwordCommand") PasswordCommand passwordCommand,
                                 Model model) {

        SecurityUser securityUser = UserResource.getCurrentUser();
        if (securityUser == null) {
            return "403";
        }
        Locale locale = LocaleContextHolder.getLocale();

        if (!passwordCommand.getPassword().equals(passwordCommand.getConfirmPassword())) {
            model.addAttribute("message",
                    messageSource.getMessage("password.not.match.message", null, locale));
            model.addAttribute("_view", "home/password");
            return "main";
        }

        if (!passwordEncoder.matches(passwordCommand.getCurrentPassword(), securityUser
                .getPassword())) {
            model.addAttribute("message",
                    messageSource.getMessage("password.not.correct.message", null, locale));
            model.addAttribute("_view", "home/password");
            return "main";
        }
        User user = null;
        try {
            user = userService.get(securityUser.getId());
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
            throw new BadRequestException("400", "Cannot find user.");
        }
        user.setPassword(passwordEncoder.encode(passwordCommand.getPassword()));
        try {
            user = userService.update(user);
        } catch (MissingRequiredFieldException e) {
            e.printStackTrace();
            throw new BadRequestException("400", "Missing fields.");
        } catch (NotUniqueException e) {
            e.printStackTrace();
            throw new BadRequestException("400", "Not unique.");
        }
        securityUser.setPassword(user.getPassword());
        model.addAttribute("_view", "index");
        return "main";
    }

    @RequestMapping(value = "/home/profile", method = RequestMethod.GET)
    public String profile(Model model) {

        User user = null;
        try {
            user = userService.get(UserResource.getCurrentUser().getId());
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
            throw new BadRequestException("400", "Cannot find user.");
        }

        // create command user and add to model and view
        UserCommand userCommand = createUserCommand(user);
        model.addAttribute("userCommand", userCommand);
        model.addAttribute("_view", "home/profile");
        return "main";
    }

    /**
     * Save a user.
     *
     * @param userCommand
     * @return
     */
    @RequestMapping(value = "/home/profile", method = RequestMethod.POST)
    public String updateProfile(Model model, @ModelAttribute("userCommand") UserCommand
            userCommand) {
        Locale locale = LocaleContextHolder.getLocale();

        // if the email is change we need to check uniqueness
        User user = null;
        try {
            user = userService.get(userCommand.getId());
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
        }
        if (user == null) {
            throw new BadRequestException("400", "User " + userCommand.getId() + " not found.");
        }

        // create command user and add to model and view
        if (!userCommand.getEmail().equals(user.getEmail())) {
            User emailUser = userService.findByEmail(userCommand.getEmail());
            if (emailUser != null) {
                // get locale and messages
                String fieldLabel = messageSource.getMessage("email.label", null, locale);
                model.addAttribute("message",
                        messageSource.getMessage("not.unique.message",
                                new String[]{fieldLabel, userCommand.getEmail()}, locale));
                model.addAttribute("userCommand", userCommand);
                model.addAttribute("_view", "home/profile");
                return "main";
            }
        }

        // pass uniqueness check create the user
        editUser(user, userCommand);

        // user is a user edited by merchant admin
        try {
            userService.update(user);
            String fieldLabel = messageSource.getMessage("user.label", null, locale);
            model.addAttribute("message",
                    messageSource.getMessage("updated.message",
                            new String[]{fieldLabel, userCommand.getUsername()}, locale));
        } catch (MissingRequiredFieldException e) {
            e.printStackTrace();
            String fieldLabel = messageSource.getMessage("user.label", null, locale);
            model.addAttribute("message",
                    messageSource.getMessage("not.updated.message",
                            new String[]{fieldLabel, userCommand.getUsername()}, locale));
        } catch (NotUniqueException e) {
            e.printStackTrace();
            String fieldLabel = messageSource.getMessage("user.label", null, locale);
            model.addAttribute("message",
                    messageSource.getMessage("not.updated.message",
                            new String[]{fieldLabel, userCommand.getUsername()}, locale));
        }
        return "redirect:/index";
    }

    // create UserCommand from User
    private UserCommand createUserCommand(User user) {
        UserCommand userCommand = new UserCommand();
        userCommand.setId(user.getId());
        userCommand.setUsername(user.getUsername());
        userCommand.setFirstName(user.getFirstName());
        userCommand.setLastName(user.getLastName());
        userCommand.setEmail(user.getEmail());
        userCommand.setActive(user.getActive());
        Locale locale = LocaleContextHolder.getLocale();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        userCommand.setCreatedTime(dateFormat.format(user.getCreatedTime()));
        userCommand.setRemark(user.getRemark());

        if (user.getUserStatus() != null) {
            userCommand.setUserStatus(user.getUserStatus().getId());
            userCommand.setUserStatusName(user.getUserStatus().getName());
        }
        return userCommand;
    }

    // edit a User from a UserCommand
    private void editUser(User user, UserCommand userCommand) {
        user.setFirstName(userCommand.getFirstName());
        user.setLastName(userCommand.getLastName());
        user.setEmail(userCommand.getEmail());
        user.setRemark(userCommand.getRemark());
    }
}
