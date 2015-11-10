package net.chronos.timekeeper.data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "shift")
public class Shift {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "shift_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "shift_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "duration", nullable = false)
    private BigDecimal duration;

    @Column(name = "lunch_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lunchStartTime;

    @Column(name = "lunch_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lunchEndTime;

    @ManyToOne
    @JoinColumn(name = "shift_type_id", nullable = false)
    private ShiftType shiftType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public Date getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(Date lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public Date getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(Date lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }
}
