package springbased.bean;

public class ValidationResult {
  
  public static final int SUCCESS = 1;
  public static final int FAIL = -1;
  public static final int SUCCESSWITHWARNING = 2;
  public int getStatus() {
    return SUCCESS;
  }

  public String getCause() {
    return "";
  }
}