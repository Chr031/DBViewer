package application.model.descriptor.objectaccessor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import application.model.pojo.acl.Profile;
import application.model.pojo.acl.User;
import application.model.pojo.cinema.Film;
import application.model.pojo.cinema.Person;
import application.model.pojo.ecom.Customer;
import application.model.pojo.ecom.Order;


/**
 * 
 * TODO create specific test classes for this test
 * 
 * @author Christophe
 *
 */
@Ignore
public class ReverseDirectFieldAccessorTest {

	private static final Logger log = LogManager.getLogger(ReverseDirectFieldAccessorTest.class);

	@Test
	public void testOneToOneAccessor() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		User u1 = new User();
		u1.setUsername("ABC");
		u1.setPassword("DEF");

		User u2 = new User();
		u2.setUsername("ijk");
		u2.setPassword("mno");

		Profile p1 = new Profile();
		p1.setFirstName("A");
		p1.setLastName("B");
		p1.setEmail("a@b.c");

		Profile p2 = new Profile();
		p2.setFirstName("i");
		p2.setLastName("j");
		p2.setEmail("i@j.k");

		Field userField = Profile.class.getDeclaredField("user");
		Field profileField = User.class.getDeclaredField("profile");

		Accessor<Profile, User> profileUserAccessor = new ReverseDirectFieldAccessor<Profile, User>(userField);
		profileUserAccessor.set(p1, u1);
		//Assert.assertEquals("User has not been updated", p1, u1.getProfile());

		Accessor<User, Profile> userProfileAccessor = new ReverseDirectFieldAccessor<User, Profile>(profileField);
		userProfileAccessor.set(u2, p2);
		Assert.assertEquals("Profile has not been updated", u2, p2.getUser());

		log.debug("before : ");
		log.debug("p1.getUser() : " + p1 + " -> " + p1.getUser());
		//log.debug("u1.getProfile() : " + u1 + " -> " + u1.getProfile());
		log.debug("p2.getUser() : " + p2 + " -> " + p2.getUser());
		//log.debug("u2.getProfile() : " + u2 + " -> " + u2.getProfile());

		profileUserAccessor.set(p2, u1); // p2.setUser(u1)

		log.debug("after p2.setUser(u1) ");
		log.debug("p1.getUser() : " + p1 + " -> " + p1.getUser());
		//log.debug("u1.getProfile() : " + u1 + " -> " + u1.getProfile());
		log.debug("p2.getUser() : " + p2 + " -> " + p2.getUser());
		//log.debug("u2.getProfile() : " + u2 + " -> " + u2.getProfile());

		//Assert.assertEquals(p2, u1.getProfile());
		Assert.assertNull("P1 should have no user", p1.getUser());
		//Assert.assertNull("u2 should habe no profile", u2.getProfile());

	}

	@Test
	public void testOneToManyAccessor() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		Customer c1 = new Customer();
		c1.setId(1);
		c1.setName("A");

		Customer c2 = new Customer();
		c2.setId(2);
		c2.setName("B");

		Order[] orders = new Order[20];
		for (int i = 0; i < orders.length; i++) {
			orders[i] = new Order(i);
		}

		List<Order> orderList1_3 = new ArrayList<>();
		orderList1_3.add(orders[1]);
		orderList1_3.add(orders[2]);
		orderList1_3.add(orders[3]);
		orderList1_3.add(orders[8]);

		List<Order> orderList4_7 = new ArrayList<>();
		orderList4_7.add(orders[4]);
		orderList4_7.add(orders[5]);
		orderList4_7.add(orders[6]);
		orderList4_7.add(orders[7]);
		orderList4_7.add(orders[8]);

		// c1.setOrders(orderList);

		Field customerField = Order.class.getDeclaredField("customer");
		Field ordersField = Customer.class.getDeclaredField("orders");
		Accessor<Customer, Collection<Order>> customerOrdersAccessor = new ReverseDirectFieldAccessor<>(ordersField);

		customerOrdersAccessor.set(c1, orderList1_3);
		//Assert.assertEquals(orderList1_3, c1.getOrders());
		//Assert.assertEquals(orderList1_3.size(), c1.getOrders().size());
		for (Order o : orderList1_3) {
			Assert.assertEquals(c1, o.getCustomer());
		}

		customerOrdersAccessor.set(c2, orderList4_7);
		//Assert.assertEquals(orderList4_7, c2.getOrders());
		//Assert.assertEquals(orderList4_7.size(), c2.getOrders().size());
		for (Order o : orderList4_7) {
			Assert.assertEquals(c2, o.getCustomer());
		}

		customerOrdersAccessor.set(c1, new ArrayList<>(orderList4_7));
		//Assert.assertEquals(orderList4_7, c1.getOrders());
		//Assert.assertEquals(orderList4_7.size(), c1.getOrders().size());
		for (Order o : orderList1_3) {
			if (orderList4_7.contains(o))
				Assert.assertEquals(c1, o.getCustomer());
			else
				Assert.assertNull(o + " should have no customer", o.getCustomer());
		}
		for (Order o : orderList4_7) {
			Assert.assertEquals(c1, o.getCustomer());
		}

		//Assert.assertTrue(c2.getOrders() == null || c2.getOrders().size() == 0);

	}

	@Test
	public void testManyToOneAccessor() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		Customer c1 = new Customer();
		c1.setId(1);
		c1.setName("A");

		Customer c2 = new Customer();
		c2.setId(2);
		c2.setName("B");

		Order[] orders = new Order[20];
		for (int i = 0; i < orders.length; i++) {
			orders[i] = new Order(i);
		}

		Field customerField = Order.class.getDeclaredField("customer");
		Field ordersField = Customer.class.getDeclaredField("orders");
		Accessor<Order, Customer> orderCustomerAccessor = new ReverseDirectFieldAccessor<>(customerField);

		orderCustomerAccessor.set(orders[0], c1);
		orderCustomerAccessor.set(orders[1], c1);
		orderCustomerAccessor.set(orders[2], c1);

		for (int i = 0; i <= 2; i++) {
			//Assert.assertTrue(c1.getOrders().contains(orders[i]));
		}

		orderCustomerAccessor.set(orders[3], c2);
		orderCustomerAccessor.set(orders[4], c2);
		orderCustomerAccessor.set(orders[5], c2);
		for (int i = 3; i <= 5; i++) {
			//Assert.assertTrue(c2.getOrders().contains(orders[i]));
		}

		orderCustomerAccessor.set(orders[3], c1);
		//Assert.assertEquals(4, c1.getOrders().size());
		//Assert.assertEquals(2, c2.getOrders().size());
		for (int i = 0; i <= 3; i++) {
			//Assert.assertTrue(c1.getOrders().contains(orders[i]));
		}
		for (int i = 4; i <= 5; i++) {
			//Assert.assertTrue(c2.getOrders().contains(orders[i]));
		}

	}

	@Test
	public void testManyToManyAccessor() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			InstantiationException {

		Film f1 = new Film();
		f1.setTitle("La loi du marché");

		Film f2 = new Film();
		f2.setTitle("Goodbye and Good luck");

		Person a1 = new Person();
		a1.setFirstName("Vincent");
		a1.setLastName("Lindon");

		Person actors[] = new Person[10];
		for (int i = 0; i < actors.length; i++) {
			actors[i] = new Person();
			actors[i].setFirstName("a");
			actors[i].setLastName("" + i);
		}

		Field actorsField = Film.class.getDeclaredField("actors");
		Field actorInField = Person.class.getDeclaredField("actorIn");

		List<Person> actorList = new ArrayList<>();
		actorList.add(a1);
		actorList.add(actors[0]);

		Accessor<Film, Collection<Person>> filmActorsAccessor = new ReverseDirectFieldAccessor<>(actorsField);

		filmActorsAccessor.set(f1, actorList);
		//Assert.assertTrue(a1.getActorIn().contains(f1));
		//Assert.assertTrue(actors[0].getActorIn().contains(f1));

		List<Person> actorList2 = new ArrayList<>();
		actorList2.add(actors[1]);
		actorList2.add(actors[0]);
		actorList2.add(actors[2]);

		filmActorsAccessor.set(f1, actorList2);
		//Assert.assertTrue(a1.getActorIn() == null || !a1.getActorIn().contains(f1));
		//Assert.assertTrue(actors[0].getActorIn().contains(f1));
		//Assert.assertTrue(actors[1].getActorIn().contains(f1));
		//Assert.assertTrue(actors[2].getActorIn().contains(f1));

		filmActorsAccessor.set(f2, actorList2);
		//Assert.assertTrue(a1.getActorIn() == null || !a1.getActorIn().contains(f2));
		//Assert.assertTrue(actors[0].getActorIn().contains(f2));
		//Assert.assertTrue(actors[1].getActorIn().contains(f2));
		//Assert.assertTrue(actors[2].getActorIn().contains(f2));
		//Assert.assertTrue(actors[0].getActorIn().contains(f1));
		//Assert.assertTrue(actors[1].getActorIn().contains(f1));
		//Assert.assertTrue(actors[2].getActorIn().contains(f1));

		//Assert.assertEquals(2, actors[0].getActorIn().size());
		//Assert.assertEquals(2, actors[1].getActorIn().size());
		//Assert.assertEquals(2, actors[2].getActorIn().size());
	}

}
