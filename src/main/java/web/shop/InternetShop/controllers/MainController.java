package web.shop.InternetShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import web.shop.InternetShop.entity.Customer;
import web.shop.InternetShop.entity.Product;
import web.shop.InternetShop.repository.ProductRepository;
import web.shop.InternetShop.service.CustomerService;
import web.shop.InternetShop.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static web.shop.InternetShop.controllers.ControllerUtills.fieldNotEmptyWithReflection;


@Controller
@SessionScope
public class MainController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    private List<Product> productsInCart = new ArrayList<>();

    @GetMapping("/")
    public String mainPage(Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        addAllAttributes(model);
        return "html/mainPage";
    }

    @GetMapping("/order")
    public String orderGet(Model model) {
        orderModel(model);
        return "html/orderProducts";
    }

    @PostMapping("/order")
    public String order(@RequestParam String name,
                        @RequestParam String surname,
                        @RequestParam String address,
                        @RequestParam String number,
                        Model model) {
        Customer customer = new Customer(name, surname, address, number);
        String page = fieldNotEmptyWithReflection(customer, model, "html/orderProducts");
        if (page != null) {
            orderModel(model);
            model.addAttribute("nullField", true);
            return page;
        }
        customerService.saveAllProducts(customer, productsInCart);
        productsInCart.clear();
        return "redirect:/";
    }


    @PostMapping("/")
    public String searchProducts(@RequestParam String searchQuery,
                                 @RequestParam Optional<String> producer,
                                 @RequestParam Optional<String> category,
                                 Model model) {
        List<Product> allProducts = productRepository.findAll();
        allProducts.removeIf(product -> !product.getName().toLowerCase().contains(searchQuery.toLowerCase()));
        producer.ifPresent(s -> allProducts.removeIf(product -> !product.getProducer().equals(s)));
        category.ifPresent(s -> allProducts.removeIf(product -> !product.getCategory().equals(s)));
        if (allProducts.size() > 0) {
            model.addAttribute("products", allProducts);
        } else {
            model.addAttribute("query", searchQuery);
        }
        addAllAttributes(model);
        return "html/mainPage";
    }


    @GetMapping("/category/{ctg}")
    public String showCatalog(@PathVariable(value = "ctg") String category, Model model) {
        Iterable<Product> product = productRepository.getProductsByCategory(category);
        model.addAttribute("products", product);
        addAllAttributes(model);
        return "html/mainPage";
    }

    @PostMapping("/put/{id}")
    public String putIntoShoppingCart(@PathVariable(value = "id") Long id) {
        Product product = productService.getProduct(id);
        product.setCustomers(null);
        productsInCart.add(product);
        return "redirect:/";
    }

    @PostMapping("/removeFromCart/{id}")
    public String removeFromCart(@PathVariable(value = "id") Long id) {
        Product product = productService.getProduct(id);
        product.setCustomers(null);
        productsInCart.remove(product);
        return "redirect:/";
    }

    private void addAllAttributes(Model model) {
        Iterable<String> allCtg = productService.getAllCategories();
        Iterable<String> allProd = productService.getAllProducers();
        model.addAttribute("allCtg", allCtg)
                .addAttribute("allProd", allProd)
                .addAttribute("allProdIn", productsInCart);
    }

    private void orderModel(Model model) {
        Iterable<Product> prod = productsInCart;
        int totalSum = productsInCart.stream().mapToInt(Product::getIntPrice).sum();
        model.addAttribute("products", prod)
                .addAttribute("customerForm", new Customer())
                .addAttribute("totalSum", totalSum);
    }


}





