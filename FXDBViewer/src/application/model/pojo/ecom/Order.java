package application.model.pojo.ecom;

import java.util.Set;

import application.model.descriptor.annotations.Calculated;
import application.model.descriptor.annotations.Link0N;
import application.model.descriptor.annotations.LinkN1;

public class Order {

	private int id;
	
	@Link0N(reverseName="orders")
	private Customer customer;

	@LinkN1(reverseName="referentOrder")
	private Set<OrderLine> items;

	public Order() {
	}

	public Order(int id) {
		this.id = id;
	}

	@Calculated
	public double orderValue() {
		double value = 0;
		for (OrderLine orderLine : items) {
			value += orderLine.lineValue();
		}
		return value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderLine> getItems() {
		return items;
	}

	public void setArticles(Set<OrderLine> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Order " + id;
	}

}
