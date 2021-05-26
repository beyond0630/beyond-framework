package com.beyond.event.driven.model.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Table: amq_outbox_unconfirmed, RabbitMQ 未收到服务端确认的消息
 *
 * @author MyBatisGenerator
 */
@Entity
@Table(name = "outbox_unconfirmed")
public class OutboxUnconfirmed implements Serializable {
    /**
     * Field: id, ID [PK][FK|amq_outbox_message.message_id]
     */
    @Id
    private String id;

    /**
     * Field: retries, 已重试次数
     */
    private Integer retries;

    /**
     * Field: next_retry, 下次重试时间
     */
    private Date nextRetry;

    /**
     * Field: created_at, 记录创建时间
     */
    private Date createdAt;

    /**
     * Field: updated_at, 最后更新时间
     */
    private Date updatedAt;

    /**
     * Field: row_version, 记录行的版本号 [RowVersion]
     */
    @Version
    private Integer rowVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table amq_outbox_unconfirmed
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * Getter of field: id, ID [PK][FK|amq_outbox_message.message_id]
     */
    public String getId() {
        return id;
    }

    /**
     * Setter of field: id, ID [PK][FK|amq_outbox_message.message_id]
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * Getter of field: retries, 已重试次数
     */
    public Integer getRetries() {
        return retries;
    }

    /**
     * Setter of field: retries, 已重试次数
     */
    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    /**
     * Getter of field: next_retry, 下次重试时间
     */
    public Date getNextRetry() {
        return nextRetry;
    }

    /**
     * Setter of field: next_retry, 下次重试时间
     */
    public void setNextRetry(Date nextRetry) {
        this.nextRetry = nextRetry;
    }

    /**
     * Getter of field: created_at, 记录创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter of field: created_at, 记录创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter of field: updated_at, 最后更新时间
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter of field: updated_at, 最后更新时间
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Getter of field: row_version, 记录行的版本号 [RowVersion]
     */
    public Integer getRowVersion() {
        return rowVersion;
    }

    /**
     * Setter of field: row_version, 记录行的版本号 [RowVersion]
     */
    public void setRowVersion(Integer rowVersion) {
        this.rowVersion = rowVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amq_outbox_unconfirmed
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", retries=" + retries +
                ", nextRetry=" + nextRetry +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", rowVersion=" + rowVersion +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}