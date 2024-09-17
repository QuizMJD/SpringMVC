package vn.hoidanit.laptopshop.controller.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {
    private final UserService userService;
    private final ServletContext servletContext;


    public UserController(UserService userService, ServletContext servletContext) {
        this.userService = userService;
        this.servletContext = servletContext;

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
    public String createUserPage(Model model, @ModelAttribute("newUser") User hoidanit,
                                 @RequestParam("hoidanitFile") MultipartFile file) {

        try {
            byte[] bytes  = file.getBytes();

        String rootPath = this.servletContext.getRealPath("/resources/images");

        File dir = new File(rootPath + File.separator + "avatar");
        if (!dir.exists())
            dir.mkdirs();

        // Create the file on server
        File serverFile = new File(dir.getAbsolutePath() + File.separator +
                +System.currentTimeMillis() + "-" + file.getOriginalFilename());

        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        this.userService.handleSaveUser(hoidanit);

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
