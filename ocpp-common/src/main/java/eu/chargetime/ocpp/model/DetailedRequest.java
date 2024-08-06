package eu.chargetime.ocpp.model;

public abstract class DetailedRequest implements Request {

  private RequestDetails details;

  public void setDetails(RequestDetails details){
    this.details = details;
  }
  public RequestDetails getDetails(){
    return details;
  }

}
