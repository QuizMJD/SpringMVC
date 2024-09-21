package vn.hoidanit.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;

import java.util.List;

@Controller

public class HomePageController {
    public final ProductService ProductService;

    public HomePageController(ProductService productService) {
        ProductService = productService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product>products=this.ProductService.getAllProduct();
        model.addAttribute("product1", products);


        return "client/homepage/show";

    }
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        return "client/auth/register";
    }

}
