package web.shop.InternetShop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String address;
    private String number;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products;

    public int getTotalSum() {
        return products.stream().mapToInt(Product::getIntPrice).sum();
    }

    public int getCountProduct() {
        return products.size();
    }

    public Customer(String name, String surname, String address, String number) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) &&
                Objects.equals(surname, customer.surname) &&
                Objects.equals(address, customer.address) &&
                Objects.equals(number, customer.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, address, number);
    }
}