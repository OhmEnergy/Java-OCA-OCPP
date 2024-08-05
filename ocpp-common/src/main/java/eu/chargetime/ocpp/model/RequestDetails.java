package eu.chargetime.ocpp.model;

import java.util.Set;

import eu.chargetime.ocpp.utilities.MoreObjects;

/**
 * Holds additional details that should be included alongside the [Request] payload
 */
public class RequestDetails {

	public RequestDetails(){}

	public RequestDetails(Set<MessageState> states){
		this.states = states;
	}

	private Set<MessageState> states;

	public void setStates(Set<MessageState> states){
		this.states = states;
	}

	public Set<MessageState> getStates(){
		return states;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("invalidStates", states)
				.toString();
	}
}
