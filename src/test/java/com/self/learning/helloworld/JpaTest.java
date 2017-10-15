package com.self.learning.helloworld;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

public class JpaTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
    }

    @After
    public void destroy() {
        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void testSecondaryCache() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println(customer);

        entityTransaction.commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        Customer customer1 = entityManager.find(Customer.class, 1);
        System.out.println(customer1);
    }

    @Test
    public void testFunction() {
        String jpql = "select upper(c.email) from Customer c";
        List<String> resultList = entityManager.createQuery(jpql).getResultList();
        for (String objects : resultList) {
            System.out.println(objects);
        }
    }

    @Test
    public void testSubQuery() {
        String jpql = "from Order o where exists (from Customer c where o.customer=c and c.lastName=?)";
        List<Order> orders = entityManager.createQuery(jpql).setParameter(1, "aa").getResultList();
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    @Test
    public void testLeftOuterJoinFetch() {
        String jpql = "from Customer c left outer join fetch c.orders where c.id = ?";
        Customer customer = (Customer) entityManager.createQuery(jpql).setParameter(1, 1)
                .getSingleResult();
        System.out.println(customer.getLastName());
    }

    @Test
    public void testGroupBy() {
        String jpql = "select o.customer from Order o group by o.customer having count(*) > 1";
        List<Customer> customers = entityManager.createQuery(jpql).getResultList();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    @Test
    public void testOrderBy() {
        List customers = entityManager.createQuery("from Customer c where c.age > ? order by c.createTime")
                .setParameter(1, 10).getResultList();
        for (Object customer : customers) {
            System.out.println(customer);
        }
    }

    @Test
    public void testNativeQuery() {
        String sql = "select last_name,age from customers where age > ?";
        List<Object[]> customers = entityManager.createNativeQuery(sql).setParameter(1, 10).getResultList();
        for (Object[] obj : customers) {
            System.out.println("last_name:" + obj[0] + "\tage:" + obj[1]);
        }
    }

    @Test
    public void testNamedQuery() {
        List<Customer> customers = entityManager.createNamedQuery("queryCustomer").setParameter(1, 10).getResultList();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    @Test
    public void testPartlyProperties() {
        String jpql = "select new Customer(c.lastName,c.age) from Customer c where c.id = ?";
        List customers = entityManager.createQuery(jpql)
                .setParameter(1, 3)
                .getResultList();
        System.out.println(customers);
    }

    @Test
    public void testJPQL() {
        Query query = entityManager.createQuery("from Customer c where c.age > ?");
        List resultList = query.setParameter(1, 10).getResultList();
        System.out.println(resultList.size());
    }

    @Test
    public void testManyToManyFind() {
        //Item item = entityManager.find(Item.class, 1);
        //System.out.println(item.getItemName());
        //System.out.println(item.getCategories().size());

        Category category = entityManager.find(Category.class, 1);
        System.out.println(category.getCategoryName());
        System.out.println(category.getItems().size());
    }

    @Test
    public void testManyToManyPersist() {
        Item item1 = new Item();
        item1.setItemName("I-aa");
        Item item2 = new Item();
        item2.setItemName("I-bb");

        Category category1 = new Category();
        category1.setCategoryName("C-aa");
        Category category2 = new Category();
        category2.setCategoryName("C-bb");

        category1.getItems().add(item1);
        category1.getItems().add(item2);
        category2.getItems().add(item1);
        category2.getItems().add(item2);

        item1.getCategories().add(category1);
        item1.getCategories().add(category2);
        item2.getCategories().add(category1);
        item2.getCategories().add(category2);

        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.persist(item1);
        entityManager.persist(item2);
    }

    @Test
    public void testOneToOneFind2() {
        Manager mgr = entityManager.find(Manager.class, 1);
        System.out.println(mgr.getMgrName());
        System.out.println(mgr.getDept().getClass().getName());
    }

    @Test
    public void testOneToOneFind() {
        Department dept = entityManager.find(Department.class, 1);
        System.out.println(dept.getDeptName());
        System.out.println(dept.getMgr().getClass().getName());
    }

    @Test
    public void testOneToOnePersist() {
        Manager mgr = new Manager();
        mgr.setMgrName("M-aa");

        Department dept = new Department();
        dept.setDeptName("D-aa");

        dept.setMgr(mgr);
        mgr.setDept(dept);

        entityManager.persist(mgr);
        entityManager.persist(dept);
    }

    /**
     * 若在1的一端的@OneToMany 中使用mappedBy属性，则1的一端就不能再使用@JoinColumn
     */
    @Test
    public void testOneToManyBothPersist() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setCreateTime(new Date());
        customer.setLastName("bb");
        customer.setEmail("bb@163.com");
        customer.setAge(10);

        Order order1 = new Order();
        order1.setOrderName("Order-1");

        Order order2 = new Order();
        order2.setOrderName("Order-2");

        customer.getOrders().add(order1);
        customer.getOrders().add(order2);

        order1.setCustomer(customer);
        order2.setCustomer(customer);

        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);
    }

    @Test
    public void testOneToManyUpdate() {
        Customer customer = entityManager.find(Customer.class, 3);
        customer.getOrders().iterator().next().setOrderName("0rder-U");
    }

    @Test
    public void testOneToManyRemove() {
        Customer customer = entityManager.find(Customer.class, 2);
        entityManager.remove(customer);
    }

    @Test
    public void testOneToManyFind() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println(customer.getLastName());
        System.out.println(customer.getOrders().size());
        //System.out.println(customer.getOrders().getClass().getName());
    }

    /**
     * 单项1-n 关联关系保存时，一定会多出update语句。
     * 因为 n 的一端在插入时不会同时插入外键列
     */
    @Test
    public void testOneToManyPersist() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setCreateTime(new Date());
        customer.setLastName("bb");
        customer.setEmail("bb@163.com");
        customer.setAge(10);

        Order order1 = new Order();
        order1.setOrderName("Order-1");

        Order order2 = new Order();
        order2.setOrderName("Order-2");

        customer.getOrders().add(order1);
        customer.getOrders().add(order2);

        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);
    }

    @Test
    public void testManyToOneUpdate() {
        Order order = entityManager.find(Order.class, 2);
        order.setOrderName("Order-3");
    }

    /**
     * 默认使用左外连接，可以修改@ManyToOne(fetch = FetchType.LAZY) 改为懒加载方式
     */
/*    @Test
    public void testManyToOneFind() {
        Order order = entityManager.find(Order.class, 1);
        //System.out.println(order.getCustomer().getClass().getName());
        System.out.println(order.getCustomer().getLastName());
    }*/

/*    @Test
    public void testManyToOnePersist() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setCreateTime(new Date());
        customer.setLastName("bb");
        customer.setEmail("bb@163.com");
        customer.setAge(10);

        Order order1 = new Order();
        order1.setOrderName("Order-1");
        order1.setCustomer(customer);

        Order order2 = new Order();
        order2.setOrderName("Order-2");
        order2.setCustomer(customer);

        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);
    }*/
    @Test
    public void testMerge3() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setCreateTime(new Date());
        customer.setLastName("xieyonghong");
        customer.setEmail("xieyonghong@163.com");
        customer.setAge(10);

        customer.setId(4);
        Customer customer1 = entityManager.merge(customer);
        System.out.println(customer == customer1);
    }

    @Test
    public void testMerge2() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setCreateTime(new Date());
        customer.setLastName("xieyonghong");
        customer.setEmail("aa@163.com");
        customer.setAge(10);

        customer.setId(100);
        Customer customer1 = entityManager.merge(customer);
        System.out.println("customer#id:" + customer.getId());
        System.out.println("customer1#id:" + customer1.getId());
    }

    @Test
    public void testMerge1() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setCreateTime(new Date());
        customer.setLastName("xieyonghong");
        customer.setEmail("aa@163.com");
        customer.setAge(10);

        Customer customer1 = entityManager.merge(customer);
        System.out.println("customer#id:" + customer.getId());
        System.out.println("customer1#id:" + customer1.getId());
    }

    /**
     * 只能移除持久化对象,hibernate的delete方法还能移除游离对象
     */
    @Test
    public void testRemove() {
        //Customer customer = new Customer();
        //customer.setId(2);

        Customer customer = entityManager.find(Customer.class, 2);
        entityManager.remove(customer);
    }

    /**
     * 和Hibernate的save方法稍有不同，如果对象有ID，则会抛出异常
     */
    @Test
    public void testPersist() {
        Customer customer = new Customer();
        customer.setBirth(new Date());
        customer.setCreateTime(new Date());
        customer.setLastName("weizhiming");
        customer.setEmail("aa@163.com");
        customer.setAge(32);
        entityManager.persist(customer);
        System.out.println(customer.getId());
    }

    /**
     * 相当于Hibernate中的Load方法
     */
    @Test
    public void testGetReference() {
        Customer customer = entityManager.getReference(Customer.class, 1);
        System.out.println(customer.getClass().getName());
        System.out.println("------------------------------------");
        System.out.println(customer);
    }

    /**
     * 相当于Hibernate中的get方法
     */
    @Test
    public void testFind() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println("------------------------------------");
        System.out.println(customer);
    }
}
