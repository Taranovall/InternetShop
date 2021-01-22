package web.shop.InternetShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.shop.InternetShop.entity.Customer;
import web.shop.InternetShop.entity.Product;
import web.shop.InternetShop.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void saveAllProducts(Customer customer, List<Product> products) {
        Customer customerFromDB = customerRepository.findCustomerByName(customer.getName());
        List<Product> prod = new ArrayList<>();
        if (customerFromDB != null) {
            customer.setSurname(customerFromDB.getSurname());
            customer.setAddress(customerFromDB.getAddress());
            customer.setNumber(customerFromDB.getNumber());

            if (customer.equals(customerFromDB)) {
                customer.setId(customerFromDB.getId());
                prod.addAll(customerFromDB.getProducts());
                prod.addAll(products);
                customer.setProducts(prod);
            }
        } else {
            prod.addAll(products);
            customer.setProducts(prod);
        }
            customerRepository.save(customer);
    }

    public Customer getCustomer(Long id) {
        Optional<Customer> customerId = customerRepository.findById(id);
        return customerId.orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
