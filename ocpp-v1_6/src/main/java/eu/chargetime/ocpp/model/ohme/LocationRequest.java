package eu.chargetime.ocpp.model.ohme;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.utilities.MoreObjects;

@XmlRootElement
@XmlType(propOrder = {"location_lat", "location_long", "location_prec"})
public class LocationRequest implements Request {

	private String location_lat;

	private String location_long;

	private String location_prec;

	private Collection<CellTower> cellTowers;
	/**
	 * The unique identifier of the request that was used when the request was transmitted over the network.
	 */
	private String requestId;

	@Override
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String getRequestId() {
		return requestId;
	}

	public Collection<CellTower> getCellTowers() {
		return cellTowers;
	}

	public void setCellTowers(Collection<CellTower> cellTowers) {
		this.cellTowers = cellTowers;
	}

	@Override
	public boolean transactionRelated() {
		return false;
	}

	@Override
	public boolean validate() {
		return true;
	}


	public String getLocation_lat() {
		return location_lat;
	}

	@XmlElement
	public void setLocation_lat(String location_lat) {
		this.location_lat = location_lat;
	}

	public String getLocation_long() {
		return location_long;
	}

	@XmlElement
	public void setLocation_long(String location_long) {
		this.location_long = location_long;
	}

	public String getLocation_prec() {
		return location_prec;
	}

	@XmlElement
	public void setLocation_prec(String location_prec) {
		this.location_prec = location_prec;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("location_lat", location_lat)
				.add("location_long", location_long)
				.add("location_prec", location_prec)
				.add("cellTowers", cellTowers)
				.add("isValid", validate())
				.toString();
	}

}
