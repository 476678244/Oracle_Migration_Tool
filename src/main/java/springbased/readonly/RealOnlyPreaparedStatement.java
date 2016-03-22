package springbased.readonly;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class RealOnlyPreaparedStatement implements PreparedStatement {

  private PreparedStatement ps = null;
  
  public RealOnlyPreaparedStatement (PreparedStatement ps) {
    this.ps = ps;
  }

  @Override
  public ResultSet executeQuery(String paramString) throws SQLException {
    return ps.executeQuery();
  }

  @Override
  public int executeUpdate(String paramString) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() throws SQLException {
    ps.close();
  }

  @Override
  public int getMaxFieldSize() throws SQLException {
    return ps.getMaxFieldSize();
  }

  @Override
  public void setMaxFieldSize(int paramInt) throws SQLException {
    ps.setMaxFieldSize(paramInt);
  }

  @Override
  public int getMaxRows() throws SQLException {
    return ps.getMaxRows();
  }

  @Override
  public void setMaxRows(int paramInt) throws SQLException {
    ps.setMaxRows(paramInt);

  }

  @Override
  public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
    ps.setEscapeProcessing(paramBoolean);
  }

  @Override
  public int getQueryTimeout() throws SQLException {
    return ps.getQueryTimeout();
  }

  @Override
  public void setQueryTimeout(int paramInt) throws SQLException {
    ps.setQueryTimeout(paramInt);
  }

  @Override
  public void cancel() throws SQLException {
    ps.cancel();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return ps.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException {
    ps.clearWarnings();

  }

  @Override
  public void setCursorName(String paramString) throws SQLException {
    ps.setCursorName(paramString);
  }

  @Override
  public boolean execute(String paramString) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    return ps.getResultSet();
  }

  @Override
  public int getUpdateCount() throws SQLException {
    return ps.getUpdateCount();
  }

  @Override
  public boolean getMoreResults() throws SQLException {
    return ps.getMoreResults();
  }

  @Override
  public void setFetchDirection(int paramInt) throws SQLException {
    ps.setFetchDirection(paramInt);
  }

  @Override
  public int getFetchDirection() throws SQLException {
    return ps.getFetchDirection();
  }

  @Override
  public void setFetchSize(int paramInt) throws SQLException {
    ps.setFetchSize(paramInt);
  }

  @Override
  public int getFetchSize() throws SQLException {
    return ps.getFetchSize();
  }

  @Override
  public int getResultSetConcurrency() throws SQLException {
    return ps.getResultSetConcurrency();
  }

  @Override
  public int getResultSetType() throws SQLException {
    return ps.getResultSetType();
  }

  @Override
  public void addBatch(String paramString) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearBatch() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int[] executeBatch() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Connection getConnection() throws SQLException {
    return ps.getConnection();
  }

  @Override
  public boolean getMoreResults(int paramInt) throws SQLException {
    return ps.getMoreResults();
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    return ps.getGeneratedKeys();
  }

  @Override
  public int executeUpdate(String paramString, int paramInt)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(String paramString, int[] paramArrayOfInt)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(String paramString, String[] paramArrayOfString)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean execute(String paramString, int paramInt) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean execute(String paramString, int[] paramArrayOfInt)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean execute(String paramString, String[] paramArrayOfString)
      throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    return ps.getResultSetHoldability();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return ps.isClosed();
  }

  @Override
  public void setPoolable(boolean paramBoolean) throws SQLException {
    ps.setPoolable(paramBoolean);
  }

  @Override
  public boolean isPoolable() throws SQLException {
    return ps.isPoolable();
  }

  @Override
  public void closeOnCompletion() throws SQLException {
    ps.closeOnCompletion();
  }

  @Override
  public boolean isCloseOnCompletion() throws SQLException {
    return ps.isCloseOnCompletion();
  }

  @Override
  public <T> T unwrap(Class<T> paramClass) throws SQLException {
    return ps.unwrap(paramClass);
  }

  @Override
  public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
    return ps.isWrapperFor(paramClass);
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    return ps.executeQuery();
  }

  @Override
  public int executeUpdate() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNull(int paramInt1, int paramInt2) throws SQLException {
    ps.setNull(paramInt1, paramInt2);
  }

  @Override
  public void setBoolean(int paramInt, boolean paramBoolean)
      throws SQLException {
    ps.setBoolean(paramInt, paramBoolean);

  }

  @Override
  public void setByte(int paramInt, byte paramByte) throws SQLException {
    ps.setByte(paramInt, paramByte);

  }

  @Override
  public void setShort(int paramInt, short paramShort) throws SQLException {
    ps.setShort(paramInt, paramShort);

  }

  @Override
  public void setInt(int paramInt1, int paramInt2) throws SQLException {
    ps.setInt(paramInt1, paramInt2);

  }

  @Override
  public void setLong(int paramInt, long paramLong) throws SQLException {
    ps.setLong(paramInt, paramLong);

  }

  @Override
  public void setFloat(int paramInt, float paramFloat) throws SQLException {
    ps.setFloat(paramInt, paramFloat);

  }

  @Override
  public void setDouble(int paramInt, double paramDouble) throws SQLException {
    ps.setDouble(paramInt, paramDouble);

  }

  @Override
  public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
      throws SQLException {
    ps.setBigDecimal(paramInt, paramBigDecimal);

  }

  @Override
  public void setString(int paramInt, String paramString) throws SQLException {
    ps.setString(paramInt, paramString);

  }

  @Override
  public void setBytes(int paramInt, byte[] paramArrayOfByte)
      throws SQLException {
    ps.setBytes(paramInt, paramArrayOfByte);

  }

  @Override
  public void setDate(int paramInt, Date paramDate) throws SQLException {
    ps.setDate(paramInt, paramDate);

  }

  @Override
  public void setTime(int paramInt, Time paramTime) throws SQLException {
    ps.setTime(paramInt, paramTime);

  }

  @Override
  public void setTimestamp(int paramInt, Timestamp paramTimestamp)
      throws SQLException {
    ps.setTimestamp(paramInt, paramTimestamp);

  }

  @Override
  public void setAsciiStream(int paramInt1, InputStream paramInputStream,
      int paramInt2) throws SQLException {
    ps.setAsciiStream(paramInt1, paramInputStream, paramInt2);

  }

  @Override
  public void setUnicodeStream(int paramInt1, InputStream paramInputStream,
      int paramInt2) throws SQLException {
    ps.setUnicodeStream(paramInt1, paramInputStream, paramInt2);

  }

  @Override
  public void setBinaryStream(int paramInt1, InputStream paramInputStream,
      int paramInt2) throws SQLException {
    ps.setBinaryStream(paramInt1, paramInputStream, paramInt2);

  }

  @Override
  public void clearParameters() throws SQLException {
    ps.clearParameters();

  }

  @Override
  public void setObject(int paramInt1, Object paramObject, int paramInt2)
      throws SQLException {
    ps.setObject(paramInt1, paramObject, paramInt2);

  }

  @Override
  public void setObject(int paramInt, Object paramObject) throws SQLException {
    ps.setObject(paramInt, paramObject);

  }

  @Override
  public boolean execute() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addBatch() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCharacterStream(int paramInt1, Reader paramReader,
      int paramInt2) throws SQLException {
    ps.setCharacterStream(paramInt1, paramReader, paramInt2);
  }

  @Override
  public void setRef(int paramInt, Ref paramRef) throws SQLException {
    ps.setRef(paramInt, paramRef);

  }

  @Override
  public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
    ps.setBlob(paramInt, paramBlob);

  }

  @Override
  public void setClob(int paramInt, Clob paramClob) throws SQLException {
    ps.setClob(paramInt, paramClob);

  }

  @Override
  public void setArray(int paramInt, Array paramArray) throws SQLException {
    ps.setArray(paramInt, paramArray);

  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return ps.getMetaData();
  }

  @Override
  public void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
      throws SQLException {
    ps.setDate(paramInt, paramDate, paramCalendar);

  }

  @Override
  public void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
      throws SQLException {
    ps.setTime(paramInt, paramTime, paramCalendar);

  }

  @Override
  public void setTimestamp(int paramInt, Timestamp paramTimestamp,
      Calendar paramCalendar) throws SQLException {
    ps.setTimestamp(paramInt, paramTimestamp, paramCalendar);

  }

  @Override
  public void setNull(int paramInt1, int paramInt2, String paramString)
      throws SQLException {
    ps.setNull(paramInt1, paramInt2, paramString);

  }

  @Override
  public void setURL(int paramInt, URL paramURL) throws SQLException {
    ps.setURL(paramInt, paramURL);

  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    return ps.getParameterMetaData();
  }

  @Override
  public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
    ps.setRowId(paramInt, paramRowId);

  }

  @Override
  public void setNString(int paramInt, String paramString) throws SQLException {
    ps.setNString(paramInt, paramString);

  }

  @Override
  public void setNCharacterStream(int paramInt, Reader paramReader,
      long paramLong) throws SQLException {
    ps.setNCharacterStream(paramInt, paramReader, paramLong);

  }

  @Override
  public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
    ps.setNClob(paramInt, paramNClob);

  }

  @Override
  public void setClob(int paramInt, Reader paramReader, long paramLong)
      throws SQLException {
    ps.setClob(paramInt, paramReader, paramLong);

  }

  @Override
  public void setBlob(int paramInt, InputStream paramInputStream,
      long paramLong) throws SQLException {
    ps.setBlob(paramInt, paramInputStream, paramLong);

  }

  @Override
  public void setNClob(int paramInt, Reader paramReader, long paramLong)
      throws SQLException {
    ps.setNClob(paramInt, paramReader, paramLong);

  }

  @Override
  public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
    ps.setSQLXML(paramInt, paramSQLXML);

  }

  @Override
  public void setObject(int paramInt1, Object paramObject, int paramInt2,
      int paramInt3) throws SQLException {
    ps.setObject(paramInt1, paramObject, paramInt2, paramInt3);

  }

  @Override
  public void setAsciiStream(int paramInt, InputStream paramInputStream,
      long paramLong) throws SQLException {
    ps.setAsciiStream(paramInt, paramInputStream, paramLong);

  }

  @Override
  public void setBinaryStream(int paramInt, InputStream paramInputStream,
      long paramLong) throws SQLException {
    ps.setBinaryStream(paramInt, paramInputStream, paramLong);

  }

  @Override
  public void setCharacterStream(int paramInt, Reader paramReader,
      long paramLong) throws SQLException {
    ps.setCharacterStream(paramInt, paramReader, paramLong);

  }

  @Override
  public void setAsciiStream(int paramInt, InputStream paramInputStream)
      throws SQLException {
    ps.setAsciiStream(paramInt, paramInputStream);

  }

  @Override
  public void setBinaryStream(int paramInt, InputStream paramInputStream)
      throws SQLException {
    ps.setBinaryStream(paramInt, paramInputStream);

  }

  @Override
  public void setCharacterStream(int paramInt, Reader paramReader)
      throws SQLException {
    ps.setCharacterStream(paramInt, paramReader);

  }

  @Override
  public void setNCharacterStream(int paramInt, Reader paramReader)
      throws SQLException {
    ps.setNCharacterStream(paramInt, paramReader);

  }

  @Override
  public void setClob(int paramInt, Reader paramReader) throws SQLException {
    ps.setClob(paramInt, paramReader);

  }

  @Override
  public void setBlob(int paramInt, InputStream paramInputStream)
      throws SQLException {
    ps.setBlob(paramInt, paramInputStream);

  }

  @Override
  public void setNClob(int paramInt, Reader paramReader) throws SQLException {
    ps.setNClob(paramInt, paramReader);

  }

}
