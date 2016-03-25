package springbased.bean;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class MigrationJob {

  @Id
  private ObjectId id;
  
  private long jobId;
  
  @Reference
  private ConnectionInfo source;
  
  @Reference
  private ConnectionInfo target;
  
  private Date startTime;
  
  private Date endTime;

  private String sourceSchema;
  
  private String targetSchema;
  
  private StatusEnum status; 
  
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ConnectionInfo getSource() {
    return source;
  }

  public void setSource(ConnectionInfo source) {
    this.source = source;
  }

  public ConnectionInfo getTarget() {
    return target;
  }

  public void setTarget(ConnectionInfo target) {
    this.target = target;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public long getJobId() {
    return jobId;
  }

  public void setJobId(long jobId) {
    this.jobId = jobId;
  }

  public String getSourceSchema() {
    return sourceSchema;
  }

  public void setSourceSchema(String sourceSchema) {
    this.sourceSchema = sourceSchema;
  }

  public String getTargetSchema() {
    return targetSchema;
  }

  public void setTargetSchema(String targetSchema) {
    this.targetSchema = targetSchema;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (jobId ^ (jobId >>> 32));
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
    MigrationJob other = (MigrationJob) obj;
    if (jobId != other.jobId)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "MigrationJob [id=" + id + ", source=" + source + ", target="
        + target + ", startTime=" + startTime + ", endTime=" + endTime + "]";
  }
  
}
