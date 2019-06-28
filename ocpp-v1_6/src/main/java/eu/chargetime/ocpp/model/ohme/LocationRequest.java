package eu.chargetime.ocpp.model.ohme;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import eu.chargetime.ocpp.model.Request;

@XmlRootElement
@XmlType(propOrder = {"location_lat", "location_long", "location_prec"})
public class LocationRequest implements Request {

	private String location_lat;

	private String location_long;

	private String location_prec;

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
}
