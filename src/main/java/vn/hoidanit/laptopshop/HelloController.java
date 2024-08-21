package vn.hoidanit.laptopshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "Hello World update";
    }

    @GetMapping("/user")
    public String usePage() {
        return "Only user can access this page";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "Only admin can access this page";
    }
}
