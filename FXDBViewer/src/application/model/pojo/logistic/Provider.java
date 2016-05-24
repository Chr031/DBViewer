package application.model.pojo.logistic;

import java.util.Set;

public class Provider {

	private String id;
	private String name;

	private ContactInformation contact;
	private Set<Address> location;
	private Set<Characteristic> characteristic;
	private Set<SLA> offers;
	private Set<Service> provides;
	private Set<Capability> capability;

	public Provider() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContactInformation getContact() {
		return contact;
	}

	public void setContact(ContactInformation contact) {
		this.contact = contact;
	}

	public Set<Address> getLocation() {
		return location;
	}

	public void setLocation(Set<Address> location) {
		this.location = location;
	}

	public Set<Characteristic> getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(Set<Characteristic> characteristic) {
		this.characteristic = characteristic;
	}

	public Set<SLA> getOffers() {
		return offers;
	}

	public void setOffers(Set<SLA> offers) {
		this.offers = offers;
	}

	public Set<Service> getProvides() {
		return provides;
	}

	public void setProvides(Set<Service> provides) {
		this.provides = provides;
	}

	public Set<Capability> getCapability() {
		return capability;
	}

	public void setCapability(Set<Capability> capability) {
		this.capability = capability;
	}

}
