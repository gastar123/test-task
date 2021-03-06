package com.mcb.creditfactory.service.car;

import com.mcb.creditfactory.dto.CarDto;
import com.mcb.creditfactory.external.CollateralObject;
import com.mcb.creditfactory.external.CollateralType;
import com.mcb.creditfactory.model.Estimate;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public class CarAdapter implements CollateralObject {
    private CarDto car;

    @Override
    public BigDecimal getValue() {
        List<Estimate> estimates = car.getEstimateList();
        return estimates != null ? estimates.stream()
                .max(Comparator.comparing(Estimate::getDate))
                .map(Estimate::getValue)
                .orElse(null)
                : null;
    }

    @Override
    public Short getYear() {
        return car.getYear();
    }

    @Override
    public LocalDate getDate() {
        List<Estimate> estimates = car.getEstimateList();
        return estimates != null ? estimates.stream()
                .max(Comparator.comparing(Estimate::getDate))
                .map(Estimate::getDate)
                .orElse(null)
                : null;
    }

    @Override
    public CollateralType getType() {
        return CollateralType.CAR;
    }
}
