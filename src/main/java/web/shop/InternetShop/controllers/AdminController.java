package web.shop.InternetShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import web.shop.InternetShop.entity.Customer;
import web.shop.InternetShop.entity.Product;
import web.shop.InternetShop.repository.ProductRepository;
import web.shop.InternetShop.service.CustomerService;
import web.shop.InternetShop.service.ProductService;

import java.util.List;

import static web.shop.InternetShop.controllers.ControllerUtills.fieldNotEmptyWithReflection;
import static web.shop.InternetShop.controllers.ControllerUtills.imageHandler;

@Controller
public class AdminController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @GetMapping("/admin/customers")
    public String orderList(Model model) {
        Iterable<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "html/admin/customers";
    }

    @GetMapping("/admin/customer-{id}")
    public String customer(@PathVariable(value = "id") Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "html/admin/customerInfo";
    }

    @GetMapping("/admin/products")
    public String products(Model model) {
        List<Product> allProducts = productRepository.findAll();
        Iterable<Product> p = allProducts;
        model.addAttribute("products", p);
        return "html/admin/allProducts";
    }

    @PostMapping("/admin/products")
    public String searchProducts(@RequestParam String searchQuery, Model model) {
        List<Product> allProducts = productRepository.findAll();
        allProducts.removeIf(product -> !product.getName().toLowerCase().contains(searchQuery.toLowerCase()));
        if (allProducts.size() > 0) {
            model.addAttribute("products", allProducts);
        } else {
            model.addAttribute("query", searchQuery);
        }
        return "html/admin/allProducts";
    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);
        if (product == null) {
            model.addAttribute("productNotExist", true);
        } else {
            model.addAttribute("product", product);
        }
        return "html/admin/editProduct";
    }

    @PostMapping("/admin/product/remove/{id}")
    public String removeProductPost(@PathVariable Long id) {
        productService.remove(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/product/edit/{id}")
    public String editProductPost(@PathVariable Long id,
                                  @RequestParam String name,
                                  @RequestParam String category,
                                  @RequestParam String producer,
                                  @RequestParam String price,
                                  @RequestParam MultipartFile photo,
                                  Model model) {
        Product product = productService.getProduct(id);
        if (!photo.isEmpty()) {
            byte[] imageInByte = imageHandler(photo, model);
            product.setPhoto(imageInByte);
        }
        product.setName(name);
        product.setCategory(category);
        product.setProducer(producer);
        product.setPrice(price);
        String page = fieldNotEmptyWithReflection(product,model,"html/admin/editProduct");
        if (page != null) {
            model.addAttribute("product",product);
            return page;
        }
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/add_product")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return "html/admin/addProduct";
    }

    @PostMapping("/admin/add_product")
    public String postAddProduct(@RequestParam String name,
                                 @RequestParam String category,
                                 @RequestParam String producer,
                                 @RequestParam String price,
                                 @RequestParam MultipartFile photo,
                                 Model model) {
        Product product = new Product(name, category, producer,price,imageHandler(photo,model));
        String page = fieldNotEmptyWithReflection(product, model, "html/admin/addProduct");
        if (page != null) {
            return page;
        }
        productRepository.save(product);
        return "redirect:/";
    }

}
