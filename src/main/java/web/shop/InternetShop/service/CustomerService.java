package web.shop.InternetShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.shop.InternetShop.entity.Customer;
import web.shop.InternetShop.entity.Product;
import web.shop.InternetShop.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void saveAllProducts(Customer customer, List<Product> products) {
        Customer customerFromDB = customerRepository.findCustomerByNameAndSurnameAndAddressAndNumber(
                customer.getName(),
                customer.getSurname(),
                customer.getAddress(),
                customer.getNumber());
        if (customerFromDB != null) {
            customer.setProducts(products);
            customer.setId(customerFromDB.getId());
            customerRepository.save(customer);
        } else {
            customer.setProducts(products);
            customerRepository.save(customer);
        }
    }

    public Customer getCustomer(Long id) {
        Optional<Customer> customerId = customerRepository.findById(id);
        return customerId.orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
