package eu.chargetime.ocpp.model;

public abstract class DetailedRequest implements Request {

  /**
   * Unique request ID for each request
   */
  private String requestId;

  /**
   * Request details that holds all kinds of optional details for a request
   */
  private RequestDetails details;

  @Override
  public void setDetails(RequestDetails details){
    this.details = details;
  }

  @Override
  public RequestDetails getDetails(){
    return details;
  }

  @Override
  public void setRequestId(String requestId){
    this.requestId = requestId;
  }

  @Override
  public String getRequestId(){
    return requestId;
  }

}
