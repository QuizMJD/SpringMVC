package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UpdateService;
import vn.hoidanit.laptopshop.service.UserService;

import java.util.List;

@Controller
public class ProductController {
    public final ProductService productService;
    public final UpdateService updateService;

    public ProductController(ProductService productService, UpdateService updateService) {
        this.productService = productService;
        this.updateService = updateService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> products=this.productService.getAllProduct();
        model.addAttribute("product1", products);
        return "admin/product/show";
    }
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String getCreateProductPage(Model model,@ModelAttribute("newProduct") Product product1 ,@RequestParam("hoidanitFile") MultipartFile file) {
        String imgProduct=this.updateService.handleSaveUpdateFile(file,"product");
        product1.setImage(imgProduct);
        this.productService.handleSaveProduct(product1);

        return "redirect:/admin/product";
    }
}
