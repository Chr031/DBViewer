package application.model.pojo.logistic;

public class Storage extends Capability {

	private String capacity;
	
	private String typeOfStorage ;
	
	private Address location;
	
	public Storage() {
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getTypeOfStorage() {
		return typeOfStorage;
	}

	public void setTypeOfStorage(String typeOfStorage) {
		this.typeOfStorage = typeOfStorage;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	
}
