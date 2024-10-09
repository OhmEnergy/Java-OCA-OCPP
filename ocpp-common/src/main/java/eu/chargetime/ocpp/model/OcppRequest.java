package eu.chargetime.ocpp.model;

public abstract class OcppRequest implements Request {

	/**
	 * Raw json that this message was parsed from
	 */
	private String rawMessageJson;

	@Override
	public void setRawMessageJson(String rawMessageJson){
		this.rawMessageJson = rawMessageJson;
	}

	@Override
	public String getRawMessageJson(){
		return rawMessageJson;
	}

	@Override
	public String getRequestId(){
		// id needs to be in the 2nd position of the array after a first item that's not a string.
		// this split logic won't work if the first item is also a string
		return rawMessageJson.split("\"")[1];
	}

}
