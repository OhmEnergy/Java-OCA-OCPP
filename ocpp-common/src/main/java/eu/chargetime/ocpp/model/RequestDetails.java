package eu.chargetime.ocpp.model;

import eu.chargetime.ocpp.utilities.MoreObjects;

/**
 * Holds additional details that should be included alongside the [Request] payload
 */
public class RequestDetails {

	public RequestDetails(){}

	public RequestDetails(Boolean invalid, String rawJson){
		this.invalid = invalid;
		this.rawJson = rawJson;
	}

	private Boolean invalid;

	private String rawJson;

	public void setInvalid(Boolean invalid){
		this.invalid = invalid;
	}

	public Boolean getInvalid(){
		return invalid;
	}

	public void setRawJson(String rawJson){
		this.rawJson = rawJson;
	}

	public String getRawJson(){
		return rawJson;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("invalid", invalid)
				.add("rawJson", rawJson)
				.toString();
	}
}
