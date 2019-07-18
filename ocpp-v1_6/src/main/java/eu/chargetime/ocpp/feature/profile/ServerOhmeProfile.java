package eu.chargetime.ocpp.feature.profile;

import java.util.HashSet;
import java.util.UUID;

import eu.chargetime.ocpp.feature.Feature;
import eu.chargetime.ocpp.feature.LocationFeature;
import eu.chargetime.ocpp.feature.ProfileFeature;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.ohme.LocationRequest;


public class ServerOhmeProfile implements Profile {

	private HashSet<Feature> features;

	private ServerOhmeEventHandler handler;

	public ServerOhmeProfile(ServerOhmeEventHandler handler) {
		this.handler = handler;
		features = new HashSet<>();
		features.add(new LocationFeature(this));
	}

	@Override
	public ProfileFeature[] getFeatureList() {
		return features.toArray(new ProfileFeature[0]);
	}

	@Override
	public Confirmation handleRequest(UUID sessionIndex, Request request) {

		if (request instanceof LocationRequest) {
			return handler.handleLocationRequest(sessionIndex, (LocationRequest) request);
		}
		return null;

	}
}
