package com.mcb.creditfactory.service.airplane;

import com.mcb.creditfactory.dto.AirplaneDto;
import com.mcb.creditfactory.external.CollateralObject;
import com.mcb.creditfactory.external.CollateralType;
import com.mcb.creditfactory.model.Estimate;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public class AirplaneAdapter implements CollateralObject {

    private AirplaneDto airplane;

    @Override
    public BigDecimal getValue() {
        List<Estimate> estimates = airplane.getEstimateList();
        return estimates != null ? estimates.stream()
                .max(Comparator.comparing(Estimate::getDate))
                .map(Estimate::getValue)
                .orElse(null)
                : null;
    }

    @Override
    public Short getYear() {
        return airplane.getYear();
    }

    @Override
    public LocalDate getDate() {
        List<Estimate> estimates = airplane.getEstimateList();
        return estimates != null ? estimates.stream()
                .max(Comparator.comparing(Estimate::getDate))
                .map(Estimate::getDate)
                .orElse(null)
                : null;
    }

    @Override
    public CollateralType getType() {
        return CollateralType.AIRPLANE;
    }

}