package vn.hoidanit.laptopshop.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;



@Controller
public class ItemController {
    private final ProductService productService;
    public ItemController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable long id, Model model) {
        Product pr=this.productService.fetchProductById(id);
        model.addAttribute("product", pr);

        return"client/product/detail";
    }
    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long productId=id;
        String email=(String)session.getAttribute("email");
        this.productService.handleAddProductToCart(email,productId);
        return "redirect:/";
    }
}
