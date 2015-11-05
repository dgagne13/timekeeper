package net.chronos.timekeeper.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employment_status")
public class EmploymentStatus {
    public static final String ACTIVE_STATUS = "Active";

    @Id
    protected Long id;
    @Column(nullable = false)
    protected String status;
    @Column(nullable = false)
    protected Boolean payable;
    @Column(name = "accrues_time", nullable = false)
    protected Boolean accruesTime;

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getPayable() {
        return payable;
    }

    public Boolean getAccruesTime() {
        return accruesTime;
    }
}
