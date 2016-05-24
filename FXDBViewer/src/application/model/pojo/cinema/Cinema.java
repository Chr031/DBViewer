package application.model.pojo.cinema;

import java.util.List;

import application.model.descriptor.annotations.LinkN1;
import application.model.descriptor.annotations.Reverse;

public class Cinema {

	private String name;
	private String address;
	
	

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
		return  name ;
	}

	
	
	
}
