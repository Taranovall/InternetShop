package web.shop.InternetShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.shop.InternetShop.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerByNameAndSurnameAndAddressAndNumber(String name, String surname, String address, String number);

    Customer findCustomerByName(String name);
}
