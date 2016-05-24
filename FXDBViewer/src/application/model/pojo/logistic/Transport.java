package application.model.pojo.logistic;

import java.util.Set;

public class Transport extends Capability {

	private OperationDistance operationDistance;
	private TransportationMode transportationMode;

	private Set<Relation> route;

	private Fleet fleet;

	public Transport() {
		super();

	}

	public TransportationMode getTransportationMode() {
		return transportationMode;
	}

	public void setTransportationMode(TransportationMode transportationMode) {
		this.transportationMode = transportationMode;
	}

	public OperationDistance getOperationDistance() {
		return operationDistance;
	}

	public void setOperationDistance(OperationDistance operationDistance) {
		this.operationDistance = operationDistance;
	}

	public Set<Relation> getRoute() {
		return route;
	}

	public void setRoute(Set<Relation> route) {
		this.route = route;
	}

	public Fleet getFleet() {
		return fleet;
	}

	public void setFleet(Fleet fleet) {
		this.fleet = fleet;
	}
}
