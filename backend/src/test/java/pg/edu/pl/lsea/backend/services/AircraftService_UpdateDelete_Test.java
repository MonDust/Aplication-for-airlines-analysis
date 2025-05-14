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
import pg.edu.pl.lsea.backend.repositories.*;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AircraftService_UpdateDelete_Test {

    @Mock
    AircraftRepo aircraftRepo;

    @Mock
    AircraftToResponseMapper aircraftToResponseMapper;

    @Mock
    OperatorRepo operatorRepo;

    @Mock
    ModelRepo modelRepo;

    @Mock
    FlightRepo flightRepo;

    @InjectMocks
    private AircraftService aircraftsService;

    // Mocked objects
    private Aircraft mockAircraft;
    private AircraftResponse mockAircraftResponse;

    /**
     * Initialize mocks and inject.
     */
    @BeforeEach
    void setup() {
        aircraftsService = new AircraftService(aircraftRepo, aircraftToResponseMapper, operatorRepo, modelRepo, flightRepo);
        Operator mockOperator = new Operator("Test Operator");
        Model mockModel = new Model("Test Model");

        // Mocked objects
        mockAircraft = new Aircraft("ICAO123", mockModel, mockOperator, "Test Owner");
        mockAircraft.setId(1L);

        mockAircraftResponse = new AircraftResponse(1L, "ICAO123", "Test Model", "Test Operator", "Test Owner");
    }
    @Test
    void update_successfulUpdate() {

        long flightId = 1L;
        String icao24 = "ICAO123";

        // Arrange
        AircraftResponse mockUpdateResReq = new AircraftResponse(flightId, icao24, "New Model", "New Operator", "New Owner");

        when(aircraftRepo.save(mockAircraft)).thenReturn(mockAircraft);
        when(aircraftToResponseMapper.apply(mockAircraft)).thenReturn(mockUpdateResReq);
        when(aircraftRepo.findByIcao24(icao24)).thenReturn(Optional.of(mockAircraft));

        // Act
        AircraftResponse response = aircraftsService.updateAircraft(icao24, mockUpdateResReq);

        // assert
        assertNotNull(response);
        assertEquals(mockUpdateResReq.icao24(), response.icao24());
        assertEquals(mockUpdateResReq.operator(), response.operator());
        assertEquals(mockUpdateResReq.model(), response.model());
        assertEquals(mockUpdateResReq.owner(), response.owner());

        verify(aircraftRepo).save(mockAircraft);
    }

    @Test
    void update_nonExistingAircraft_throwsException() {
        long flightId = 1L;
        String icao24 = "ICAO123";

        // Arrange
        AircraftResponse mockUpdateRequest = new AircraftResponse(flightId, icao24, "New Model", "New Operator", "New Owner");

        // assert
        assertThrows(ResourceNotFoundException.class, () -> {
            aircraftsService.updateAircraft(icao24, mockUpdateRequest);
        });

        verify(aircraftRepo, never()).save(any());
    }


    @Test
    void patch_successfulUpdate() {

        long flightId = 1L;
        String icao24 = "ICAO123";

        AircraftResponse mockPatchResReq = new AircraftResponse(flightId, icao24, "New Model", "New Operator", "New Owner");

        // Arrange
        when(aircraftRepo.save(mockAircraft)).thenReturn(mockAircraft);
        when(aircraftToResponseMapper.apply(mockAircraft)).thenReturn(mockPatchResReq);
        when(aircraftRepo.findByIcao24(icao24)).thenReturn(Optional.of(mockAircraft));

        // Act
        AircraftResponse response = aircraftsService.patchAircraft(icao24, mockPatchResReq);

        // Assert
        assertNotNull(response);
        assertEquals(mockPatchResReq.icao24(), response.icao24());
        assertEquals(mockPatchResReq.operator(), response.operator());
        assertEquals(mockPatchResReq.model(), response.model());
        assertEquals(mockPatchResReq.owner(), response.owner());

        verify(aircraftRepo).save(mockAircraft);
    }

    @Test
    void patch_nonExistingAircraft_throwsException() {
        long flightId = 1L;
        String icao24 = "ICAO123";

        AircraftResponse mockPatchRequest = new AircraftResponse(flightId, icao24, "New Model", "New Operator", "New Owner");

        assertThrows(ResourceNotFoundException.class, () -> {
            aircraftsService.patchAircraft(icao24, mockPatchRequest);
        });

        verify(aircraftRepo, never()).save(any());
    }
}