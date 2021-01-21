package web.shop.InternetShop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Base64;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String category;
    private String producer;
    private String price;
    private byte[] photo;
    @ManyToMany(mappedBy = "products")
    private Set<Customer> customers;

    public Product(String name, String category, String producer, String price, byte[] photo) {
        this.name = name;
        this.category = category;
        this.producer = producer;
        this.price = price;
        this.photo = photo;
    }

    public int getIntPrice() {
        return Integer.parseInt(price);
    }

    public String getPhoto() {
        return "data:image/jpeg;base64," + Base64.getMimeEncoder().encodeToString(photo);
    }
}
