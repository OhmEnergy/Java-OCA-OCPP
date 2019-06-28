package eu.chargetime.ocpp.feature.profile;

import java.util.UUID;

import eu.chargetime.ocpp.model.ohme.LocationConfirmation;
import eu.chargetime.ocpp.model.ohme.LocationRequest;

public interface ServerOhmeEventHandler {

	LocationConfirmation handleLocationRequest(UUID sessionIndex, LocationRequest request);

}
