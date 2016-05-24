package application.model.pojo.ecom;


public class Address {

	private String Street;
	private String City;
	private String Country;	

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}


	@Override
	public String toString() {
		return  Street + " " + City + " | " + Country ;
	}
	
	

}
