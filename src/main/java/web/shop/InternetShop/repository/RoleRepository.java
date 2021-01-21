package web.shop.InternetShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.shop.InternetShop.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
