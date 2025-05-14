package pg.edu.pl.lsea.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.services.AircraftService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AircraftController.class)
@Import(AircraftControllerTest.TestConfig.class)
class AircraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AircraftService aircraftService;

    private ObjectMapper objectMapper;

    AircraftResponse aircraft;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AircraftService aircraftService() {
            return mock(AircraftService.class);
        }
    }

    @BeforeEach
    void setup() {
        aircraft = new AircraftResponse(1L, "ICAO123", "Boeing 737", "Delta Airlines", "USA");
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAircrafts_returnsListOfAircrafts() throws Exception {
        when(aircraftService.getAll()).thenReturn(List.of(aircraft));

        mockMvc.perform(get("/aircrafts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].icao24").value("ICAO123"));
    }

    @Test
    void getAircraft_returnsSingleAircraft() throws Exception {
        when(aircraftService.getByIcao("ICAO123")).thenReturn(aircraft);

        mockMvc.perform(get("/aircrafts/ICAO123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.icao24").value("ICAO123"))
                .andExpect(jsonPath("$.model").value("Boeing 737"));
    }


    @Test
    void createAircraft_returnsCreatedAircraft() throws Exception {
        when(aircraftService.create(any())).thenReturn(aircraft);

        mockMvc.perform(post("/aircrafts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aircraft)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.icao24").value("ICAO123"))
                .andExpect(jsonPath("$.model").value("Boeing 737"));
    }

    @Test
    void createBulkAircrafts_returnsCreatedAircrafts() throws Exception {
        when(aircraftService.createBulk(any())).thenReturn(List.of(aircraft));

        mockMvc.perform(post("/aircrafts/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(aircraft))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].icao24").value("ICAO123"));
    }

}
