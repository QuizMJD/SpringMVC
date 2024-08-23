package vn.hoidanit.laptopshop.repository;

import vn.hoidanit.laptopshop.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User save(User hoidanit);
}
