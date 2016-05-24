package application.model.pojo.logistic;

public class Defect extends Characteristic {

	private String transportDamage;
	private String claimsRelative;
	private String accidentRelative;
	
	public Defect() {
		super();
		
	}

	public String getTransportDamage() {
		return transportDamage;
	}

	public void setTransportDamage(String transportDamage) {
		this.transportDamage = transportDamage;
	}

	public String getClaimsRelative() {
		return claimsRelative;
	}

	public void setClaimsRelative(String claimsRelative) {
		this.claimsRelative = claimsRelative;
	}

	public String getAccidentRelative() {
		return accidentRelative;
	}

	public void setAccidentRelative(String accidentRelative) {
		this.accidentRelative = accidentRelative;
	}
	
	

}
