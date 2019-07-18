package eu.chargetime.ocpp.feature;

import eu.chargetime.ocpp.feature.profile.Profile;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.ohme.LocationConfirmation;
import eu.chargetime.ocpp.model.ohme.LocationRequest;

public class LocationFeature extends ProfileFeature {
	public LocationFeature(Profile ownerProfile) {
		super(ownerProfile);
	}

	@Override
	public Class<? extends Request> getRequestType() {
		return LocationRequest.class;
	}

	@Override
	public Class<? extends Confirmation> getConfirmationType() {
		return LocationConfirmation.class;
	}

	@Override
	public String getAction() {
		return "Location";
	}
}
