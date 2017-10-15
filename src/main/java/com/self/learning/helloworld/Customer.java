package com.self.learning.helloworld;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Cacheable
@Table(name = "customers")
@Entity
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;
    private Date createTime;
    private Date birth;
    private Set<Order> orders = new HashSet<Order>();

    public Customer() {
    }

    public Customer(String lastName, int age) {
        this.lastName = lastName;
        this.age = age;
    }

    //@TableGenerator(name = "id_generator",
    //        table = "id_generator",
    //        pkColumnName = "PK_NAME",
    //        pkColumnValue = "customer_id",
    //        valueColumnName = "PK_VALUE",
    //        allocationSize = 100
    //)
    //@GeneratedValue(strategy = GenerationType.TABLE,generator = "id_generator")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "last_name",length = 50,nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Transient
    public String getInfo() {
        return "lastName : " + lastName + " , age : " + age;
    }

    //@JoinColumn(name = "customer_id")
    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE},mappedBy = "customer")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createTime=" + createTime +
                ", birth=" + birth +
                '}';
    }
}
