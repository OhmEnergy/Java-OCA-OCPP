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
	public String getRequestId() {
		if (requestId == null && rawMessageJson != null) {
			/*
			 * a raw message JSON has this format: [2,"THE_ID_WE_CARE_ABOUT",StatusNotification,{}].
			 * This is a quick and dirty way to extract the ID without parsing the whose JSON message.
			 */
			String[] messageParts = rawMessageJson.split("\"");
			if (messageParts.length > 1) {
				requestId = messageParts[1];
			}
		}
		return requestId;
	}

}
