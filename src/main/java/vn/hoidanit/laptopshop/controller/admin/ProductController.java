package vn.hoidanit.laptopshop.controller.admin;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UpdateService;

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
    public String getCreateProductPage(Model model, @ModelAttribute("newProduct") @Valid Product product1 ,
                                       BindingResult newUserbindingResult,
                                       @RequestParam("hoidanitFile") MultipartFile file) {
        // validate
        List<FieldError> errors = newUserbindingResult.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (">>>>>"+error.getField() + " - " + error.getDefaultMessage());
        }

        if(newUserbindingResult.hasErrors()) {
            return "/admin/product/create";
        }


        //
        String imgProduct=this.updateService.handleSaveUpdateFile(file,"product");
        product1.setImage(imgProduct);
        this.productService.handleSaveProduct(product1);

        return "redirect:/admin/product";
    }
    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable Long id) {
    Product product=productService.getProductById(id);
    model.addAttribute("product", product);
    return "admin/product/detail";

    }
    @GetMapping("admin/product/update/{id}")
    public String getUpdateProductPage(Model model,@PathVariable Long id) {
        Product currentProduct=productService.getProductById(id);
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";

    }
    @PutMapping("admin/product/update/")
    public String UpdateUser(@Valid Product pr, BindingResult bindingResult,MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "/admin/product/update";
        }
        Product currentProduct =this.productService.getProductById(pr.getId());
        // update new image
        if (!file.isEmpty()) {
            String img = this.updateService.handleSaveUpdateFile(file, "product");
            currentProduct.setImage(img);
        }

        if(currentProduct!=null) {
            currentProduct.setName(pr.getName());
            currentProduct.setPrice(pr.getPrice());
            currentProduct.setDetailDesc(pr.getDetailDesc());
            currentProduct.setShortDesc(pr.getShortDesc());
            currentProduct.setQuantity(pr.getQuantity());
            currentProduct.setFactory(pr.getFactory());
            currentProduct.setTarget(pr.getTarget());
            currentProduct.setImage(pr.getImage());
            this.productService.handleSaveProduct(currentProduct);
        }
        return "redirect:/admin/product";

    }
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable Long id) {
//        Product currentProduct=productService.getProductById(id);
        model.addAttribute("deleteProduct", new Product() );
        return "admin/product/delete";
    }

//    @PostMapping("/admin/product/delete/")
//    public String postDeleteProduct(Product pr) {
//       this.productService.deleteProduct(pr.getId());
//       return "redirect:/admin/product";
//    }
@PostMapping("/admin/product/delete")
public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product pr) {
    this.productService.deleteProduct(pr.getId());
    return "redirect:/admin/product";
}





}
