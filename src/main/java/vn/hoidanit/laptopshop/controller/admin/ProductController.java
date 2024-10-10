package vn.hoidanit.laptopshop.controller.admin;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Controller
public class ProductController {
    public final ProductService productService;
    public final UpdateService updateService;

    public ProductController(ProductService productService, UpdateService updateService) {
        this.productService = productService;
        this.updateService = updateService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model, @RequestParam("page")Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
            else {

            }
        }catch(Exception e){
            }


        Pageable pageable= PageRequest.of(page -1,5);
        Page<Product> products=this.productService.fetchAllProduct(pageable);
        List<Product> productList=products.getContent();
        model.addAttribute("product1", productList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",products.getTotalPages());
        return "admin/product/show";
    }
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String getCreateProductPage( @ModelAttribute("newProduct") @Valid Product pr ,
                                       BindingResult newUserbindingResult,
                                       @RequestParam("hoidanitFile") MultipartFile file) {
        // validate
        List<FieldError> errors = newUserbindingResult.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (">>>>>"+error.getField() + " - " + error.getDefaultMessage());
        }

        if(newUserbindingResult.hasErrors()) {
            return "admin/product/create";
        }


        //
        String imgP=this.updateService.handleSaveUpdateFile(file,"product");
        pr.setImage(imgP);
        this.productService.handleSaveProduct(pr);

        return "redirect:/admin/product";
    }
    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable Long id) {
    Product pr=productService.fetchProductById(id);
    model.addAttribute("product", pr);
    return "admin/product/detail";

    }
    @GetMapping("admin/product/update/{id}")
    public String getUpdateProductPage(Model model,@PathVariable Long id) {
        Product currentProduct=productService.fetchProductById(id);
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";

    }
    @PostMapping("admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("newProduct") @Valid Product pr, BindingResult bindingResult ,@RequestParam("hoidanitFile")MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product currentProduct =this.productService.fetchProductById(pr.getId());
        if(currentProduct !=null) {
        // update new image
        if (!file.isEmpty()) {
            String img = this.updateService.handleSaveUpdateFile(file, "product");
            currentProduct.setImage(img);
        }

            currentProduct.setName(pr.getName());
            currentProduct.setPrice(pr.getPrice());
            currentProduct.setQuantity(pr.getQuantity());
            currentProduct.setDetailDesc(pr.getDetailDesc());
            currentProduct.setShortDesc(pr.getShortDesc());
            currentProduct.setFactory(pr.getFactory());
            currentProduct.setTarget(pr.getTarget());
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
