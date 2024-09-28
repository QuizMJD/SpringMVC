package vn.hoidanit.laptopshop.controller.client;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UserService;

import java.util.List;

@Controller

public class HomePageController {
    public final ProductService ProductService;
    public final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public HomePageController(ProductService productService, UserService userService,PasswordEncoder passwordEncoder) {
        this.ProductService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product>products=this.ProductService.fetchAllProduct();
        model.addAttribute("product1", products);


        return "client/homepage/show";

    }
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegisterPage(Model model, @ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
                                     BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "client/auth/register";
        }

        User user= this.userService.UregisterDTOtoUser(registerDTO);
        user.setPassword(this.passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(this.userService.fetchRoleByName("ROLE_USER"));
        //save
        this.userService.handleSaveUser(user);

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String getLoginPage(Model model) {
//        model.addAttribute("loginUser", new RegisterDTO());
        return "client/auth/login";
    }
    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {
        return "client/auth/access-deny";
    }

}
