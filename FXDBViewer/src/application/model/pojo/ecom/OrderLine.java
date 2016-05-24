package application.model.pojo.ecom;

import application.model.descriptor.annotations.Calculated;
import application.model.descriptor.annotations.Link1N;

public class OrderLine {

	private int id;
	
	@Link1N(reverseName="orders")
	private Item item;
	private int quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return id + " - " + item + " x " + quantity;
	}

	@Calculated
	public double lineValue() {
		return item.getPrice() * quantity;
	}

}
