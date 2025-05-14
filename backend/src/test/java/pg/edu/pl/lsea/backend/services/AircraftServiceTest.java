package pg.edu.pl.lsea.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.entities.Model;
import pg.edu.pl.lsea.backend.entities.Operator;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.repositories.ModelRepo;
import pg.edu.pl.lsea.backend.repositories.OperatorRepo;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AircraftServiceTest {
    @Mock
    private AircraftRepo aircraftRepo;
    @Mock
    private AircraftToResponseMapper aircraftToResponseMapper;
    @Mock
    private OperatorRepo operatorRepo;
    @Mock
    private ModelRepo modelRepo;
    @Mock
    private FlightRepo flightRepo;
    @InjectMocks
    private AircraftService aircraftService;

    // Mocked objects
    private Aircraft mockAircraft;
    private AircraftResponse mockAircraftResponse;

    /**
     * Initialize mocks and inject.
     */
    @BeforeEach
    void setup() {
        Model model = new Model("Boeing 737");
        Operator operator = new Operator("Delta Airlines");
        mockAircraft = new Aircraft("ICAO123", model, operator, "USA");
        mockAircraftResponse = new AircraftResponse(1L,"ICAO123", "Boeing 737", "Delta Airlines", "USA");
    }

    @Test
    void testGetAllAircrafts() {
        when(aircraftToResponseMapper.apply(any(Aircraft.class))).thenReturn(mockAircraftResponse);
        when(aircraftRepo.findAll()).thenReturn(List.of(mockAircraft));

        List<AircraftResponse> result = aircraftService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockAircraftResponse, result.get(0));
        verify(aircraftRepo).findAll();
    }

    @Test
    void testGetAircraftById_Success() {
        when(aircraftToResponseMapper.apply(mockAircraft)).thenReturn(mockAircraftResponse);
        when(aircraftRepo.findByIcao24("ICAO123")).thenReturn(Optional.of(mockAircraft));

        AircraftResponse result = aircraftService.getByIcao("ICAO123");

        assertNotNull(result);
        assertEquals(mockAircraftResponse, result);
        verify(aircraftRepo).findByIcao24("ICAO123");
    }

    @Test
    void testGetAircraftByIcao_NotFound() {
        when(aircraftRepo.findByIcao24("NOT_FOUND")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aircraftService.getByIcao("NOT_FOUND"));
    }


    @Test
    void testCreateAircraft_Correct() {
        when(modelRepo.findByName("Boeing 737")).thenReturn(Optional.of(new Model("Boeing 737")));
        when(operatorRepo.findByName("Delta Airlines")).thenReturn(Optional.of(new Operator("Delta Airlines")));
        when(aircraftRepo.save(any())).thenReturn(mockAircraft);
        when(aircraftToResponseMapper.apply(any())).thenReturn(mockAircraftResponse);

        AircraftResponse result = aircraftService.create(mockAircraftResponse);

        assertNotNull(result);
        assertEquals(mockAircraftResponse, result);
    }

    @Test
    void testCreateAircraft_AlreadyExists() {
        when(aircraftRepo.findByIcao24("ICAO123")).thenReturn(Optional.of(mockAircraft));
        AircraftResponse result = aircraftService.create(mockAircraftResponse);

        assertNull(result);
        verify(aircraftRepo).findByIcao24("ICAO123");
        verify(aircraftRepo, never()).save(any());
    }

    @Test
    void testCreateFlight_InvalidRequest() {
        AircraftResponse invalid = new AircraftResponse(1L,"ICAO123", "Boeing 737", "", "USA");
        AircraftResponse result = aircraftService.create(invalid);

        assertNull(result);
    }




    @Test
    void testCreateBulkAircrafts_AllValid() {
        when(aircraftToResponseMapper.apply(any(Aircraft.class))).thenReturn(mockAircraftResponse);
        List<AircraftResponse> requests = List.of(mockAircraftResponse);

        when(aircraftRepo.saveAll(any())).thenReturn(List.of(mockAircraft));

        List<AircraftResponse> result = aircraftService.createBulk(requests);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockAircraftResponse, result.get(0));
    }

    @Test
    void deleteAircraft() {
        when(aircraftRepo.findByIcao24("ICAO24")).thenReturn(Optional.of(mockAircraft));

        aircraftService.deleteAircraft("ICAO24");
    }

    @Test
    void deleteFlight_NotExists() {
        when(aircraftRepo.findByIcao24("ICAO24")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aircraftService.deleteAircraft("ICAO24"));
    }
}
