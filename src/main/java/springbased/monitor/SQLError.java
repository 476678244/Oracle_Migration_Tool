package springbased.monitor;

public class SQLError {

  private String sql;
  
  private String exceptionMessage;

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public String getExceptionMessage() {
    return exceptionMessage;
  }

  public void setExceptionMessage(String exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((exceptionMessage == null) ? 0 : exceptionMessage.hashCode());
    result = prime * result + ((sql == null) ? 0 : sql.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SQLError other = (SQLError) obj;
    if (exceptionMessage == null) {
      if (other.exceptionMessage != null)
        return false;
    } else if (!exceptionMessage.equals(other.exceptionMessage))
      return false;
    if (sql == null) {
      if (other.sql != null)
        return false;
    } else if (!sql.equals(other.sql))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "SQLError [sql=" + sql + ", exceptionMessage=" + exceptionMessage
        + "]";
  }

}
