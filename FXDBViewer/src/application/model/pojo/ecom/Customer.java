package application.model.pojo.ecom;

import java.util.Set;

import application.model.descriptor.annotations.LinkNN;

public class Customer {

	private int id;
	private String name;
	private String address;
	private String email;
	
	@LinkNN(reverseName="CustomersAddresses")
	private Set<Address> addresses;

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return id + " - " + name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

}
