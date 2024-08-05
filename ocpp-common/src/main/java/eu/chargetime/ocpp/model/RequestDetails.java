package eu.chargetime.ocpp.model;

/**
 * Holds additional details that should be included alongside the [Request] payload
 */
public class RequestDetails {

	public RequestDetails(){}

	public RequestDetails(Boolean invalid){
		this.invalid = invalid;
	}

	private Boolean invalid;

	public void setInvalid(Boolean invalid){
		this.invalid = invalid;
	}

	public Boolean getInvalid(){
		return invalid;
	}
}
