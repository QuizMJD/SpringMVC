package vn.hoidanit.laptopshop.controller.client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.service.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


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
        this.productService.handleAddProductToCart(email,productId,session);
        return "redirect:/";
    }
    @GetMapping("/cart")
    public String getCart(Model model,  HttpServletRequest request) {
       User currentUser = new User();
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("id");
        currentUser.setId(userId);
        Cart cart=this.productService.fetchByUser(currentUser);
        List<CartDetail>cartDetails=cart==null?new ArrayList<CartDetail>(): cart.getCartDetails();
        double totalPrice=0;
        for (CartDetail cd:cartDetails){
            totalPrice+=cd.getPrice()*cd.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        return "client/cart/show";
    }
    @PostMapping("/delete-cart-product/{id}")
    public String deleteCartDetail(@PathVariable long id, HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        long cartDetailId = id;
        this.productService.handleRemoveCartDetail(cartDetailId, session);
        return "redirect:/cart";
    }

    @GetMapping("/products")
    public String getProductPage(Model model, ProductCriteriaDTO productCriteriaDTO


    ) {

        int page = 1;
        try {
            if (productCriteriaDTO.getPage().isPresent()) {
                // convert from String to int
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
            // TODO: handle exception
        }


        Pageable pageable = PageRequest.of(page - 1, 60);

////         String name = nameOptional.isPresent() ? nameOptional.get() : "";
//         Page<Product> prs = this.productService.fetchProductsWithSpec(pageable,
//         name);
        Page<Product> prs = this.productService.fetchAllProduct(pageable);

//         case 1
//         double min = minOptional.isPresent() ? Double.parseDouble(minOptional.get()) : 0;
//         Page<Product> prs = this.productService.fetchProductsWithSpec(pageable, min);

        // case 2
//         double max = maxOptional.isPresent() ? Double.parseDouble(maxOptional.get()) : 0;
//         Page<Product> prs = this.productService.fetchProductsWithSpec(pageable, max);

        // case 3
//         String factory = factoryOptional.isPresent() ? factoryOptional.get() : "";
//         Page<Product> prs = this.productService.fetchProductsWithSpec(pageable,
//         factory);

        // case 4
//         List<String> factory = Arrays.asList(factoryOptional.get().split(","));
//         Page<Product> prs = this.productService.fetchProductsWithSpec(pageable,
//         factory);

//         case 5
//         String price = priceOptional.isPresent() ? priceOptional.get() : "";
//         Page<Product> prs = this.productService.fetchProductsWithSpec(pageable,
//         price);

        // case 6
//        List<String> price = Arrays.asList(priceOptional.get().split(","));
//        Page<Product> prs = this.productService.fetchProductsWithSpec(pageable, price);
//

        List<Product> products = prs.getContent();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prs.getTotalPages());
        return "client/product/show";
    }

}




