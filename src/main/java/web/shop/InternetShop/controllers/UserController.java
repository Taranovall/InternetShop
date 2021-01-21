package web.shop.InternetShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import web.shop.InternetShop.entity.User;
import web.shop.InternetShop.repository.UserRepository;
import web.shop.InternetShop.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "html/auth/login";
    }

    @GetMapping("/login")
    public String login() {
        return "html/auth/login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userForm",new User());
        return "html/auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") @Validated User userForm, BindingResult bindingResult, Model model){

        if (userForm.getUsername().length() < 6 || userForm.getUsername().length() > 32) {
            model.addAttribute("username",true);
            return "html/auth/registration";
        }
        if (userRepository.findByUsername(userForm.getUsername()) != null) {
            model.addAttribute("usernameDuplicate",true);
            return "html/auth/registration";
        }

        if (userForm.getPassword().length() < 8 || userForm.getPassword().length() > 32) {
            model.addAttribute("password",true);
            return "html/auth/registration";
        }

        if (!userForm.getPasswordConfirm().equals(userForm.getPassword())) {
            model.addAttribute("passwordConfirm",true);
            return "html/auth/registration";
        }

        if (bindingResult.hasErrors()){
            return "html/auth/registration";
        }
        userService.saveUser(userForm);
        return "redirect:/";
    }



}
