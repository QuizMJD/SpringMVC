package vn.hoidanit.laptopshop.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;

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

    public List<Product>fetchAllProduct(){
        return this.productRepository.findAll();
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



}
