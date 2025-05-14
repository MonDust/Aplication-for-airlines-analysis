package pg.edu.pl.lsea.backend.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.entities.Airport;
import pg.edu.pl.lsea.backend.entities.Model;
import pg.edu.pl.lsea.backend.entities.Operator;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.original.Flight;
import pg.edu.pl.lsea.backend.repositories.*;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.Optional;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {
    @Mock
    private FlightRepo flightRepo;
    @Mock
    private EnrichedFlightRepo enrichedFlightRepo;
    @Mock
    private AirportRepo airportRepo;
    @Mock
    private AircraftRepo aircraftRepo;
    @Mock
    private RouteRepo routeRepo;
    @Mock
    private FlightToResponseMapper flightToResponseMapper;

    private FlightService flightService;

    // Mocked objects
    private Flight mockFlight;
    private Aircraft mockAircraft;
    private FlightResponse mockFlightResponse;

    /**
     * Initialize mocks and inject.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        flightService = new FlightService(flightRepo, flightToResponseMapper, enrichedFlightRepo, airportRepo, aircraftRepo, routeRepo);
        mockFlight = new Flight("ICAO123", 123456, 123457);
        mockFlightResponse = new FlightResponse(1L, "ICAO123", 123456, 123457, "AAA", "BBB");

        Model model = new Model("Boeing 737");
        Operator operator = new Operator("Delta Airlines");
        mockAircraft = new Aircraft("ICAO123", model, operator, "USA");

    }

    @Test
    void testGetAllFlights() {
        when(flightToResponseMapper.apply(any(Flight.class))).thenReturn(mockFlightResponse);
        when(flightRepo.findAll()).thenReturn(List.of(mockFlight));

        List<FlightResponse> result = flightService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockFlightResponse, result.get(0));
    }

    @Test
    void testGetFlightById_Success() {
        when(flightToResponseMapper.apply(any(Flight.class))).thenReturn(mockFlightResponse);
        when(flightRepo.findById(1L)).thenReturn(Optional.of(mockFlight));

        FlightResponse result = flightService.getById(1L);

        assertNotNull(result);
        assertEquals(mockFlightResponse, result);
    }

    @Test
    void testGetFlightById_NotFound() {
        when(flightRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightService.getById(99L));
    }

    @Test
    void testGetFlightsByIcao() {
        when(flightToResponseMapper.apply(any(Flight.class))).thenReturn(mockFlightResponse);
        when(flightRepo.findAllByIcao24("ICAO123")).thenReturn(List.of(mockFlight));

        List<FlightResponse> result = flightService.getByIcao("ICAO123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockFlightResponse, result.get(0));
    }

    @Test
    void testCreateFlight_NewFlight_ValidRequest() {
        when(flightRepo.findByIcao24AndFirstSeen(anyString(), anyInt())).thenReturn(Optional.empty());
        when(airportRepo.findByCode("AAA")).thenReturn(Optional.of(new Airport("AAA")));
        when(airportRepo.findByCode("BBB")).thenReturn(Optional.of(new Airport("BBB")));
        when(routeRepo.findByOriginAndDestination(any(), any())).thenReturn(Optional.empty());

        when(aircraftRepo.findByIcao24("ICAO123")).thenReturn(Optional.of(mockAircraft));

        when(flightRepo.save(any(Flight.class))).thenAnswer(i -> i.getArguments()[0]);
        when(enrichedFlightRepo.save(any())).thenReturn(null);
        when(flightToResponseMapper.apply(any(Flight.class))).thenReturn(mockFlightResponse);

        FlightResponse result = flightService.create(mockFlightResponse);

        assertNotNull(result);
        assertEquals(mockFlightResponse, result);
    }

    @Test
    void testCreateFlight_AlreadyExists() {
        when(flightRepo.findByIcao24AndFirstSeen("ICAO123", 123456)).thenReturn(Optional.of(mockFlight));

        FlightResponse result = flightService.create(mockFlightResponse);

        assertNull(result);
    }

    @Test
    void testCreateFlight_InvalidRequest() {
        FlightResponse invalid = new FlightResponse(2L,"ICAO123", 123456, 123457, "", "BBB");
        FlightResponse result = flightService.create(invalid);

        assertNull(result);
    }

    @Test
    void testCreateBulkFlights_AllValid() {
        when(flightToResponseMapper.apply(any(Flight.class))).thenReturn(mockFlightResponse);
        List<FlightResponse> requests = List.of(mockFlightResponse);

        when(airportRepo.findAll()).thenReturn(List.of());
        when(airportRepo.saveAll(any())).thenReturn(List.of());
        when(aircraftRepo.findByIcao24In(Set.of("ICAO123")))
                .thenReturn(List.of(mockAircraft));
        when(routeRepo.findAll()).thenReturn(List.of());
        when(routeRepo.saveAll(any())).thenReturn(List.of());
        when(flightRepo.findByIcao24AndFirstSeen(any(), anyInt())).thenReturn(Optional.empty());
        when(flightRepo.saveAll(any())).thenReturn(List.of(mockFlight));
        when(enrichedFlightRepo.saveAll(any())).thenReturn(null);

        List<FlightResponse> result = flightService.createBulk(requests);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockFlightResponse, result.get(0));
    }

    @Test
    void deleteFlight() {
        when(flightRepo.existsById(1L)).thenReturn(Boolean.TRUE);

        flightService.delete(1L);
    }

    @Test
    void deleteFlight_NotExists() {
        when(flightRepo.existsById(1L)).thenReturn(Boolean.FALSE);

        assertThrows(ResourceNotFoundException.class, () -> flightService.delete(1L));
    }

    @Test
    void deleteByTimeRange() {
        when(flightRepo.deleteByFirstSeenGreaterThanEqualAndLastSeenLessThanEqual(100,200)).thenReturn(Boolean.TRUE);
        boolean result = flightService.deleteByTimeRange(100, 200);

        assertTrue(result);
    }

}
