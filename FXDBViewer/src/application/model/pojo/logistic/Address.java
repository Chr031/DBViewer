package application.model.pojo.logistic;

public class Address {

	private String street;
	private String zip;
	private String city;
	private String countryCode;
	private String geoCoordinate;

	public Address() {
		super();
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getGeoCoordinate() {
		return geoCoordinate;
	}

	public void setGeoCoordinate(String geoCoordinate) {
		this.geoCoordinate = geoCoordinate;
	}

	@Override
	public String toString() {
		return  String.format("%1$s, %2$s %3$s (%4$s)", street, zip, city, countryCode);
	}
	
	
	

}
