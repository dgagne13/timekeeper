package net.chronos.timekeeper.data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "position")
public class Position {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "max_hourly_rate")
    private BigDecimal maximumHourlyRate;

    @Column(name = "min_hourly_rate")
    private BigDecimal minimumHourlyRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getMaximumHourlyRate() {
        return maximumHourlyRate;
    }

    public void setMaximumHourlyRate(BigDecimal maximumHourlyRate) {
        this.maximumHourlyRate = maximumHourlyRate;
    }

    public BigDecimal getMinimumHourlyRate() {
        return minimumHourlyRate;
    }

    public void setMinimumHourlyRate(BigDecimal minimumHourlyRate) {
        this.minimumHourlyRate = minimumHourlyRate;
    }

}
