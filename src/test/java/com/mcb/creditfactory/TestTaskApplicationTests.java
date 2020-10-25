package com.mcb.creditfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcb.creditfactory.dto.AirplaneDto;
import com.mcb.creditfactory.dto.CarDto;
import com.mcb.creditfactory.model.Estimate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestTaskApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
        assertNotNull(objectMapper);
    }

    @Test
    public void saveCarAndGetInfo_Success() throws Exception {
        List<Estimate> estimateList = new ArrayList<>();
        estimateList.add(new Estimate(null, new BigDecimal(10000011), LocalDate.now()));
        estimateList.add(new Estimate(null, new BigDecimal(10000012), LocalDate.now()));
        estimateList.add(new Estimate(null, new BigDecimal(10000013), LocalDate.now()));
        CarDto car = new CarDto(null, "bmw", "x5", 250.0, (short) 2005, estimateList);

        MockHttpServletResponse saveResponse = mockMvc.perform(post("/collateral/save")
                .content(objectMapper.writerFor(CarDto.class).writeValueAsString(car))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        Long saveResponseId = objectMapper.readValue(saveResponse.getContentAsByteArray(), Long.class);
        CarDto carInfo = new CarDto();
        carInfo.setId(saveResponseId);

        MockHttpServletResponse infoResponse = mockMvc.perform(post("/collateral/info")
                .content(objectMapper.writerFor(CarDto.class).writeValueAsString(carInfo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        CarDto infoCar = objectMapper.readValue(infoResponse.getContentAsByteArray(), CarDto.class);
        assertCarDto(car, infoCar);
    }

    private void assertCarDto(CarDto requestCar, CarDto responseCar) {
        assertEquals(requestCar.getBrand(), responseCar.getBrand());
        assertEquals(requestCar.getModel(), responseCar.getModel());
        assertEquals(requestCar.getPower(), responseCar.getPower());
        assertEquals(requestCar.getYear(), responseCar.getYear());

        List<Estimate> requestEstimates = requestCar.getEstimateList();
        List<Estimate> responseEstimates = responseCar.getEstimateList();
        assertEquals(requestEstimates.size(), responseEstimates.size());
        for (int i = 0; i < requestEstimates.size(); i++) {
            assertEquals(requestEstimates.get(i).getValue(), responseEstimates.get(i).getValue());
            assertEquals(requestEstimates.get(i).getDate(), responseEstimates.get(i).getDate());
        }
    }

    @Test
    public void saveAirplaneAndGetInfo_Success() throws Exception {
        List<Estimate> estimateList = new ArrayList<>();
        estimateList.add(new Estimate(null, new BigDecimal(230000001), LocalDate.now()));
        estimateList.add(new Estimate(null, new BigDecimal(230000002), LocalDate.now()));
        estimateList.add(new Estimate(null, new BigDecimal(230000003), LocalDate.now()));
        AirplaneDto airplane = new AirplaneDto(null, "brand", "model", "manufacturer", (short) 2005, 200, 250, estimateList);

        MockHttpServletResponse saveResponse = mockMvc.perform(post("/collateral/save")
                .content(objectMapper.writerFor(AirplaneDto.class).writeValueAsString(airplane))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        Long saveResponseId = objectMapper.readValue(saveResponse.getContentAsByteArray(), Long.class);
        AirplaneDto airplaneInfo = new AirplaneDto();
        airplaneInfo.setId(saveResponseId);

        MockHttpServletResponse infoResponse = mockMvc.perform(post("/collateral/info")
                .content(objectMapper.writerFor(AirplaneDto.class).writeValueAsString(airplaneInfo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        AirplaneDto infoAirplane = objectMapper.readValue(infoResponse.getContentAsByteArray(), AirplaneDto.class);
        assertAirplaneDto(airplane, infoAirplane);
    }

    private void assertAirplaneDto(AirplaneDto requestCar, AirplaneDto responseCar) {
        assertEquals(requestCar.getBrand(), responseCar.getBrand());
        assertEquals(requestCar.getModel(), responseCar.getModel());
        assertEquals(requestCar.getManufacturer(), responseCar.getManufacturer());
        assertEquals(requestCar.getYear(), responseCar.getYear());
        assertEquals(requestCar.getFuelCapacity(), responseCar.getFuelCapacity());
        assertEquals(requestCar.getSeats(), responseCar.getSeats());

        List<Estimate> requestEstimates = requestCar.getEstimateList();
        List<Estimate> responseEstimates = responseCar.getEstimateList();
        assertEquals(requestEstimates.size(), responseEstimates.size());
        for (int i = 0; i < requestEstimates.size(); i++) {
            assertEquals(requestEstimates.get(i).getValue(), responseEstimates.get(i).getValue());
            assertEquals(requestEstimates.get(i).getDate(), responseEstimates.get(i).getDate());
        }
    }

    @Test
    public void saveCar_BadRequest_WrongEstimateValue() throws Exception {
        List<Estimate> estimateList = new ArrayList<>();
        estimateList.add(new Estimate(null, new BigDecimal(10000011), LocalDate.of(2017, Month.OCTOBER, 1)));
        estimateList.add(new Estimate(null, new BigDecimal(100000), LocalDate.of(2017, Month.OCTOBER, 2)));
        estimateList.add(new Estimate(null, new BigDecimal(100000), LocalDate.of(2017, Month.OCTOBER, 3)));
        CarDto car = new CarDto(null, "bmw", "x5", 250.0, (short) 2005, estimateList);

        mockMvc.perform(post("/collateral/save")
                .content(objectMapper.writerFor(CarDto.class).writeValueAsString(car))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveAirplane_BadRequest_WrongEstimateDate() throws Exception {
        List<Estimate> estimateList = new ArrayList<>();
        estimateList.add(new Estimate(null, new BigDecimal(230000001), LocalDate.of(2016, Month.OCTOBER, 1)));
        estimateList.add(new Estimate(null, new BigDecimal(230000002), LocalDate.of(2016, Month.OCTOBER, 2)));
        estimateList.add(new Estimate(null, new BigDecimal(230000003), LocalDate.of(2016, Month.OCTOBER, 3)));
        AirplaneDto airplane = new AirplaneDto(null, "brand", "model", "manufacturer", (short) 2005, 200, 250, estimateList);

        mockMvc.perform(post("/collateral/save")
                .content(objectMapper.writerFor(AirplaneDto.class).writeValueAsString(airplane))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
