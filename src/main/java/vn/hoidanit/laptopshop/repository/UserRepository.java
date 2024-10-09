package vn.hoidanit.laptopshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User eric);

    void deleteById(Long id);

    List<User> findOneByEmail(String email);

    List<User> findAll();
    boolean existsByEmail(String email);
    User findByEmail(String email);
    //phan trang
    Page<User> findAll(Pageable page);

}
