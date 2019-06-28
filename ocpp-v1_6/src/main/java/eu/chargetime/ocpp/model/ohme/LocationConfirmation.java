package eu.chargetime.ocpp.model.ohme;

import javax.xml.bind.annotation.XmlRootElement;

import eu.chargetime.ocpp.model.Confirmation;

@XmlRootElement(name = "getConfirmationResponse")
public class LocationConfirmation implements Confirmation {


	@Override
	public boolean validate() {
		return true;
	}

}
