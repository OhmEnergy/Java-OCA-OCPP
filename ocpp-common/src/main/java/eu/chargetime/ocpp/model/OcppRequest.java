package eu.chargetime.ocpp.model;

public abstract class OcppRequest implements Request {

	/**
	 * The request id associated with this message
	 */
	private String requestId;

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
		// if requestId already set, get from here
		if(requestId != null){
			return requestId;
		}

		try {
			// id needs to be in the 2nd position of the array after a first item that's not a string.
			// this split logic won't work if the first item is also a string
			String requestId = rawMessageJson.split("\"")[1];

			// set requestId before returning it so that next time, it's simply returned from this getter
			this.requestId = requestId;
			return requestId;
		} catch (NullPointerException | IndexOutOfBoundsException exception){
			// only catch and return null for these 2 exception types, let everything else throw the exception
			return null;
		}
	}

}
