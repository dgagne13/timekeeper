package net.chronos.timekeeper.dto;

import net.chronos.timekeeper.data.Shift;

import java.math.BigDecimal;
import java.util.Date;

public class ShiftDTO {
    private Long shiftId;
    private Long employeeId;
    private Date startTime;
    private Date endTime;
    private Date lunchStartTime;
    private Date lunchEndTime;
    private String shiftType;
    private BigDecimal duration;

    public ShiftDTO() {};
    public ShiftDTO(Shift shift) {
        this.shiftId = shift.getId();
        this.employeeId = shift.getEmployeeId();
        this.shiftType = shift.getShiftType().getValue();
        this.startTime = shift.getStartTime();
        this.lunchStartTime = shift.getLunchStartTime();
        this.lunchEndTime = shift.getLunchStartTime();
        this.endTime = shift.getEndTime();
        this.duration = shift.getDuration();
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
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

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }
}
