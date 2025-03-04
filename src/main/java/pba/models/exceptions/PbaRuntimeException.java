package pba.models.exceptions;

public class PbaRuntimeException extends RuntimeException {

  public String message;

  public PbaRuntimeException(String message) {
    super();
    this.message = message;
  }

  public PbaRuntimeException(String message, Throwable cause) {
    super(cause);
    this.message = message;
  }
}
