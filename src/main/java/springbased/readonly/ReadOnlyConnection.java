package springbased.readonly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReadOnlyConnection {

  private Connection con = null;

  public ReadOnlyConnection(Connection con) {
    this.con = con;
  }

  public PreparedStatement prepareStatement(String paramString)
      throws SQLException {
    PreparedStatement ps = this.con.prepareStatement(paramString);
    RealOnlyPreaparedStatement readOnlyPs = new RealOnlyPreaparedStatement(ps);
    return readOnlyPs;
  }
  
  public Connection getConnection() {
    return this.con;
  }

  public void close() throws SQLException {
    this.con.close();
  }
  
  public boolean isReadOnly() {
    try {
      return this.con.isReadOnly();
    } catch (SQLException e) {
      return false;
    }
  }
}
