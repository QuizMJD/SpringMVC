package vn.hoidanit.laptopshop.controller.admin;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UpdateService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {
    private final UserService userService;
    private final UpdateService updateService;
    private final PasswordEncoder passwordEncoder;



    public UserController(UserService userService, UpdateService updateService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.updateService = updateService;
        this.passwordEncoder = passwordEncoder;

    }

     @RequestMapping("/")
     public String getHomePage(Model model) {
     List<User> arrUser = this.userService.getAllUsersByEmail("qui@gmail.com");
     System.out.println(arrUser);

     model.addAttribute("eric", "test");
     model.addAttribute("hoidanit", "from controller with model");
     return "hello";
     }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users1", users);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetail(Model model, @PathVariable Long id) {
        // System.out.println("check id" + id);
        // model.addAttribute("id", id);
        // return "admin/user/show";
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/detail"; // Trả về view chi tiết của user

    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping(value = "/admin/user/create")
    public String createUserPage(Model model, @ModelAttribute("newUser") @Valid User hoidanit,
                                 BindingResult bindingResult,
                                 @RequestParam("hoidanitFile") MultipartFile file) {
    // validate
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (error.getObjectName() + " - " + error.getDefaultMessage());
        }


        //
    String avatar=this.updateService.handleSaveUpdateFile(file,"avatar");
    String hashPassword=this.passwordEncoder.encode(hoidanit.getPassword());
    hoidanit.setAvatar(avatar);
    hoidanit.setPassword(hashPassword);
    hoidanit.setRole(this.userService.getRoleByName(hoidanit.getRole().getName()));
    this.userService.handleSaveUser(hoidanit);

        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String UpdateUser(Model model, @PathVariable Long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update"; // Trả về view chi tiết của user
    }

    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @RequestMapping(value = "/admin/user/update", method = RequestMethod.POST)
    public String PostUpdateUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        User currentUser = this.userService.getUserById(hoidanit.getId());
        if (currentUser != null) {
            currentUser.setAddress(hoidanit.getAddress());
            currentUser.setFullName(hoidanit.getFullName());
            currentUser.setPhone(hoidanit.getPhone());
            this.userService.handleSaveUser(currentUser);
        }

        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        // model.addAttribute("id", id);
        // User user = new User();
        // user.setId(id);
        // lấy id cách 1
        model.addAttribute("newUser", new User());
        return "/admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        this.userService.deleteUser(hoidanit.getId());
        return "redirect:/admin/user";
    }

}
