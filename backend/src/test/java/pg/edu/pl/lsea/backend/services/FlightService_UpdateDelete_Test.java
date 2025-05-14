package pg.edu.pl.lsea.backend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightUpdateRequest;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.entities.original.Flight;
import pg.edu.pl.lsea.backend.repositories.AirportRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.RouteRepo;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightService_UpdateDelete_Test {

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

    @InjectMocks
    private FlightService flightService;

    // Mocked objects
    private Flight mockFlight;
    private FlightResponse mockFlightResponse;

    /**
     * Initialize mocks and inject.
     */
    @BeforeEach
    void setup() {
        flightService = new FlightService(flightRepo, flightToResponseMapper, enrichedFlightRepo, airportRepo, aircraftRepo, routeRepo);
        mockFlight = new Flight("ICAO123", 123456, 123457);
        mockFlight.setId(1L);
        mockFlightResponse = new FlightResponse(1L, "ICAO123", 123456, 123457, "AAA", "BBB");
    }

    @Test
    void updatePeriod_successfulUpdate() {

        long flightId = 1L;
        String icao24 = "ICAO123";
        int firstSeen = 123456;

        FlightResponse updateRequest = new FlightResponse(flightId, icao24, firstSeen, 222222, "AAA", "BBB");
        FlightResponse mockUpdateResponse = new FlightResponse(flightId, icao24, firstSeen, 222222, "AAA", "BBB");

        // Arrange
        when(flightRepo.save(mockFlight)).thenReturn(mockFlight);
        when(flightToResponseMapper.apply(mockFlight)).thenReturn(mockUpdateResponse);
        when(flightRepo.findById(flightId)).thenReturn(Optional.of(mockFlight));

        // Act
        FlightResponse response = flightService.update(flightId, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(updateRequest, response);
        verify(flightRepo).save(mockFlight);
    }

    @Test
    void updatePeriod_nonExistingFlight_throwsException() {
        long flightId = 1L;
        String icao24 = "ICAO123";
        int firstSeen = 123456;

        FlightResponse updateRequest = new FlightResponse(flightId, icao24, firstSeen, 123457, "AAA", "BBB");

        assertThrows(ResourceNotFoundException.class, () -> {
            flightService.update(flightId, updateRequest);
        });
        verify(flightRepo, never()).save(any());
    }

    @Test
    void patchPeriod_successfulUpdate() {

        long flightId = 1L;
        String icao24 = "ICAO123";
        int firstSeen = 123456;

        FlightUpdateRequest patchRequest = new FlightUpdateRequest(icao24, firstSeen, 222222, "AAA", "BBB");
        FlightResponse mockPatchResponse = new FlightResponse(flightId, icao24, firstSeen, 222222, "AAA", "BBB");

        // Arrange
        when(flightRepo.save(mockFlight)).thenReturn(mockFlight);
        when(flightToResponseMapper.apply(mockFlight)).thenReturn(mockPatchResponse);
        when(flightRepo.findByIcao24AndFirstSeen(icao24,firstSeen)).thenReturn(Optional.of(mockFlight));

        // Act
        FlightResponse response = flightService.patchByIcao24AndFirstSeen(icao24, firstSeen, patchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(patchRequest.firstSeen(), response.firstSeen());;
        assertEquals(patchRequest.lastSeen(), response.lastSeen());;
        assertEquals(patchRequest.icao24(), response.icao24());;
        assertEquals(patchRequest.lastSeen(), response.lastSeen());;
        assertEquals(patchRequest.departureAirport(), response.departureAirport());;
        assertEquals(patchRequest.arrivalAirport(), response.arrivalAirport());
        verify(flightRepo).save(mockFlight);
    }

    @Test
    void patchPeriod_nonExistingFlight_throwsException() {
        long flightId = 1L;
        String icao24 = "ICAO123";
        int firstSeen = 123456;

        FlightUpdateRequest patchRequest = new FlightUpdateRequest(icao24, firstSeen, 123457, "AAA", "BBB");

        assertThrows(ResourceNotFoundException.class, () -> {
            flightService.patchByIcao24AndFirstSeen(icao24, firstSeen, patchRequest);
        });
        verify(flightRepo, never()).save(any());
    }

    @Test
    void delete_existingFlight_deletesSuccessfully() {

        long flightId = 1L;

        // Arrange
        when(flightRepo.existsById(flightId)).thenReturn(true);

        // Act
        flightService.delete(flightId);

        // Assert
        verify(flightRepo).deleteById(flightId);
    }

    @Test
    void delete_nonExistingFlight_throwsException() {

        long flightId = 1L;

        when(flightRepo.existsById(flightId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            flightService.delete(flightId);
        });
        verify(flightRepo, never()).delete(any());
    }
}
