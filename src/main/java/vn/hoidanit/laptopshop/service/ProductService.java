package vn.hoidanit.laptopshop.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hoidanit.laptopshop.domain.*;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.service.specification.ProductSpecs;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
                          CartDetailRepository cartDetailRepository, UserService userService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
    }


    public Product handleSaveProduct(Product product) {
        return this.productRepository.save(product);
    }



    public Page<Product> fetchAllProduct(Pageable page) {
        return this.productRepository.findAll(page);
    }

//     public Page<Product> fetchProductsWithSpec(Pageable page, String name) {
//     return this.productRepository.findAll(ProductSpecs.nameLike(name), page);
//     }

//     case 1
//     public Page<Product> fetchProductsWithSpec(Pageable page, double min) {
//     return this.productRepository.findAll(ProductSpecs.minPrice(min), page);
//     }

    // case 2
//     public Page<Product> fetchProductsWithSpec(Pageable page, double max) {
//     return this.productRepository.findAll(ProductSpecs.maxPrice(max), page);
//     }

    // case 3
//     public Page<Product> fetchProductsWithSpec(Pageable page, String factory) {
//     return this.productRepository.findAll(ProductSpecs.matchFactory(factory),
//     page);
//     }

    // case 4
//     public Page<Product> fetchProductsWithSpec(Pageable page, List<String>
//     factory) {
//     return this.productRepository.findAll(ProductSpecs.matchListFactory(factory),
//     page);
//     }

    // case 5
    /*public Page<Product> fetchProductsWithSpec(Pageable page, String price) {
        // eg: price 10-toi-15-trieu
        if (price.equals("10-toi-15-trieu")) {
            double min = 10000000;
            double max = 15000000;
            return this.productRepository.findAll(ProductSpecs.matchPrice(min, max),
                    page);

        } else if (price.equals("15-toi-30-trieu")) {
            double min = 15000000;
            double max = 30000000;
            return this.productRepository.findAll(ProductSpecs.matchPrice(min, max),
                    page);
        } else
            return this.productRepository.findAll(page);
    }*/

    // case 6
    public Page<Product> fetchProductsWithSpec(Pageable page, List<String> price) {
        Specification<Product> combinedSpec = (root, query, criteriaBuilder) -> criteriaBuilder.disjunction();
        int count = 0;
        for (String p : price) {
            double min = 0;
            double max = 0;

            // Set the appropriate min and max based on the price range string
            switch (p) {
                case "10-toi-15-trieu":
                    min = 10000000;
                    max = 15000000;
                    count++;
                    break;
                case "15-toi-20-trieu":
                    min = 15000000;
                    max = 20000000;
                    count++;
                    break;
                case "20-toi-30-trieu":
                    min = 20000000;
                    max = 30000000;
                    count++;
                    break;
                // Add more cases as needed
            }

            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec);
            }
        }

        // Check if any price ranges were added (combinedSpec is empty)
        if (count == 0) {
            return this.productRepository.findAll(page);
        }

        return this.productRepository.findAll(combinedSpec, page);
    }


    public Product fetchProductById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }
    public void deleteProduct(Long id) {
        this.productRepository.deleteById(id);
    }
    public void handleAddProductToCart(String email, Long productId, HttpSession session){

        User user=this.userService.fetchUserByEmail(email);
        if(user != null){
            //check user có cart chưa? nếu chưa -> tạo mớI
            Cart cart=this.cartRepository.findByUser(user);
            if(cart==null){
                //tạo mới cart
                Cart newCart=new Cart();
                newCart.setUser(user);
                newCart.setSum(0);
                cart=this.cartRepository.save(newCart);

            }
            //lưu cart detail
            //tìm product by id
            Optional<Product> p=this.productRepository.findById(productId);
            if(p.isPresent()){
                Product realProduct=p.get();
                // check sp đã từng thêm từ giỏ hàng chưa
                CartDetail oldDetail=this.cartDetailRepository.findByCartAndProduct(cart,realProduct);
                if(oldDetail==null){
                    CartDetail cd=new CartDetail();
                    cd.setCart(cart);
                    cd.setProduct(realProduct);
                    cd.setPrice(realProduct.getPrice());
                    cd.setQuantity(1);
                    this.cartDetailRepository.save(cd);
                    // update cart sum
                    int s=cart.getSum()+1;
                    cart.setSum(s);
                    this.cartRepository.save(cart);
                    session.setAttribute("sum",s);
                }else{
                    oldDetail.setQuantity(oldDetail.getQuantity()+1);
                    this.cartDetailRepository.save(oldDetail);
                }


        }


        }


    }
   public Cart fetchByUser(User user){
        return this.cartRepository.findByUser(user);
   }
    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();

            Cart currentCart = cartDetail.getCart();
            // delete cart-detail
            this.cartDetailRepository.deleteById(cartDetailId);

            // update cart
            if (currentCart.getSum() > 1) {
                // update current cart
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);
                session.setAttribute("sum", s);
                this.cartRepository.save(currentCart);
            } else {
                // delete cart (sum = 1)
                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
            }
        }
    }




}
