package com.hackerrank.emailsystem.controller;

import com.hackerrank.emailsystem.dao.EmailHistoryRepository;
import com.hackerrank.emailsystem.model.EmailHistory;
import com.hackerrank.emailsystem.model.User;
import com.hackerrank.emailsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * The Class AppController.
 *
 * @author smsarraf
 */
@Controller
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;


    /**
     * Login.
     *
     * @return the model and view
     */
    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    /**
     * Registration.
     *
     * @return the model and view
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    /**
     * Creates the new user.
     *
     * @param user the user
     * @param bindingResult the binding result
     * @return the model and view
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    /**
     * Home.
     *
     * @return the model and view
     */
    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    /**
     * Index.
     *
     * @param date the date
     * @return the model and view
     */
    @RequestMapping(value = "/admin/home/{date}", method = RequestMethod.GET)
    public ModelAndView index(@PathVariable(name = "date") String date) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.addObject("date", date);
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }

    /**
     * Index.
     *
     * @return the model and view
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage", "");
        modelAndView.setViewName("home");
        return modelAndView;
    }
	
    /**
     * Gets the all email history.
     *
     * @param req the req
     * @param resp the resp
     * @return the all email history
     */
    @RequestMapping(value="/getAllEmails")
    @ResponseBody
    public List<Map<?,?>> getAllEmailHistory(HttpServletRequest req, HttpServletResponse resp){
        return emailHistoryRepository.getAllEmailHistoryByDate();
    }

    /**
     * Gets the all email history by date.
     *
     * @param date the date
     * @return the all email history by date
     */
    @RequestMapping(value="/getAllEmailsByDate/{date}")
    @ResponseBody
    public List<EmailHistory> getAllEmailHistoryByDate(@PathVariable("date") String date){
        return emailHistoryRepository.getAllEmailHistoryByDate(date);
    }
}
