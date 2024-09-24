package vn.hoidanit.laptopshop.service;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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



}
