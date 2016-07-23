package springbased.bean;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

/**
 * Created by zonghan on 7/20/16.
 */
@Entity
public class CopyTableDataRequest implements Serializable, Cloneable {

    @Id
    private ObjectId id = null;

    private String connectionUrl;

    private String username;

    private String password;

    private String schema;

    private String table;

    private String targetConnectionUrl;

    private String targetUsername;

    private String targetPassword;

    private String targetSchema;

    private RequestStatusEnum status = RequestStatusEnum.CREATED;

    private String idColumnName;

    private long migrationJobId;

    private long startId;

    private long endId;

    public CopyTableDataRequest() {

    }

    public CopyTableDataRequest(String connectionUrl, String username, String password, String schema, String table) {
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.schema = schema;
        this.table = table;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public CopyTableDataRequest setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
        return this;
    }

    public CopyTableDataRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public CopyTableDataRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public CopyTableDataRequest setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public CopyTableDataRequest setTable(String table) {
        this.table = table;
        return this;
    }

    public RequestStatusEnum getStatus() {
        return status;
    }

    public CopyTableDataRequest setStatus(RequestStatusEnum status) {
        this.status = status;
        return this;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new CopyTableDataRequest(
                this.connectionUrl, this.username, this.password, this.schema, this.table).setTargetConnectionUrl(
                this.targetConnectionUrl).setTargetUsername(this.targetUsername).setTargetPassword(
                this.targetPassword).setTargetSchema(this.targetSchema);
    }

    @Override
    public String toString() {
        return "CopyTableDataRequest{" +
                "connectionUrl='" + connectionUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                '}';
    }

    public String getTargetConnectionUrl() {
        return targetConnectionUrl;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public String getTargetPassword() {
        return targetPassword;
    }

    public String getTargetSchema() {
        return targetSchema;
    }

    public CopyTableDataRequest setTargetConnectionUrl(String targetConnectionUrl) {
        this.targetConnectionUrl = targetConnectionUrl;
        return this;
    }

    public CopyTableDataRequest setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
        return this;
    }

    public CopyTableDataRequest setTargetPassword(String targetPassword) {
        this.targetPassword = targetPassword;
        return this;
    }

    public CopyTableDataRequest setTargetSchema(String targetSchema) {
        this.targetSchema = targetSchema;
        return this;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public long getMigrationJobId() {
        return migrationJobId;
    }

    public long getStartId() {
        return startId;
    }

    public long getEndId() {
        return endId;
    }

    public CopyTableDataRequest setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
        return this;
    }

    public CopyTableDataRequest setMigrationJobId(long migrationJobId) {
        this.migrationJobId = migrationJobId;
        return this;
    }

    public CopyTableDataRequest setStartId(long startId) {
        this.startId = startId;
        return this;
    }

    public CopyTableDataRequest setEndId(long endId) {
        this.endId = endId;
        return this;
    }
}
