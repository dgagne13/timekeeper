package net.chronos.timekeeper.repository;

import net.chronos.timekeeper.data.ShiftType;
import org.springframework.data.repository.Repository;

public interface ShiftTypeRepository extends Repository<ShiftType, Long> {
    ShiftType findByValue(String value);
}
