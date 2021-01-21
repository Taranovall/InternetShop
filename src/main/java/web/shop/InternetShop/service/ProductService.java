package web.shop.InternetShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.shop.InternetShop.entity.Product;
import web.shop.InternetShop.repository.ProductRepository;

import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository rep;

    public Set<String> getAllCategories() {
        Set<String> allCtg = new LinkedHashSet<>();
        List<Product> list = rep.findAll();
        for (Product p : list) {
            allCtg.add(p.getCategory());
        }
        return allCtg;
    }

    public Set<String> getAllProducers() {
        Set<String> allProd = new LinkedHashSet<>();
        List<Product> list = rep.findAll();
        for (Product p : list) {
            allProd.add(p.getProducer());
        }
        return allProd;
    }

    public Product getProduct(Long id) {
        Optional<Product> productId = rep.findById(id);
        return productId.orElse(null);
    }

    public boolean remove(Long id) {
        if (rep.existsById(id)) {
            rep.deleteById(id);
            return true;
        }
        return false;
    }


}
