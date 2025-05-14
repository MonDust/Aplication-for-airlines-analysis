package pg.edu.pl.lsea.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightUpdateRequest;
import pg.edu.pl.lsea.backend.entities.Model;
import pg.edu.pl.lsea.backend.entities.Operator;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.original.Flight;
import pg.edu.pl.lsea.backend.services.FlightService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
@Import(FlightControllerTest.TestConfig.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightService flightService;

    private ObjectMapper objectMapper;

    FlightResponse flight;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FlightService flightService() {
            return mock(FlightService.class);
        }
    }

    @BeforeEach
    void setup() {
        flight = new FlightResponse(1L, "ICAO123", 123456, 123457, "AAA", "BBB");
        objectMapper = new ObjectMapper();
    }

    @Test
    void getFlights_returnsListOfFlights() throws Exception {
        when(flightService.getAll()).thenReturn(List.of(flight));

        mockMvc.perform(get("/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getFlight_returnsSingleFlight() throws Exception {
        when(flightService.getById(1L)).thenReturn(flight);

        mockMvc.perform(get("/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.icao24").value("ICAO123"));
    }

    @Test
    void getFlightsByIcao_returnsMatchingFlights() throws Exception {
        when(flightService.getByIcao("ICAO123")).thenReturn(List.of(flight));

        mockMvc.perform(get("/flights/icao24/ICAO123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].icao24").value("ICAO123"));
    }

    @Test
    void createFlight_returnsCreatedFlight() throws Exception {
        when(flightService.create(any())).thenReturn(flight);

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.icao24").value("ICAO123"));
    }

    @Test
    void createFlights_returnsCreatedFlights() throws Exception {
        when(flightService.createBulk(any())).thenReturn(List.of(flight));

        mockMvc.perform(post("/flights/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(flight))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
