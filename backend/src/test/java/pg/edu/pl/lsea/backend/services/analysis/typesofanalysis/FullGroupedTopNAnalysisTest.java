package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pg.edu.pl.lsea.backend.entities.Operator;
import pg.edu.pl.lsea.backend.entities.Model;
import pg.edu.pl.lsea.backend.entities.analysis.Output;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.original.Flight;
import pg.edu.pl.lsea.backend.repositories.OperatorRepo;
import pg.edu.pl.lsea.backend.repositories.ModelRepo;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FullGroupedTopNAnalysisTest {

    // Dependencies needed by the FullGroupedTopNAnalysis constructor.
    @Mock private FlightRepo flightRepo;
    @Mock private FlightToResponseMapper flightToResponseMapper;
    @Mock private EnrichedFlightRepo enrichedFlightRepo;
    @Mock private EnrichedFlightToResponseMapper enrichedFlightToResponseMapper;
    @Mock private AircraftRepo aircraftRepo;
    @Mock private AircraftToResponseMapper aircraftToResponseMapper;

    // We will inject the constructor dependencies (even though they are not used in the grouping methods)
    @InjectMocks
    private FullGroupedTopNAnalysis analysis;

    // Mocks for method parameters that are not injected into FullGroupedTopNAnalysis.
    @Mock private OperatorRepo operatorRepo;
    @Mock private ModelRepo modelRepo;


    @Test
    void getGroupedTopNOperators_shouldReturnTopNOperatorsSortedByFlightCount() {
        // Arrange: Create three operators each with one or more aircraft.
        Operator op1 = new Operator("Lufthansa");
        Operator op2 = new Operator("Ryanair");
        Operator op3 = new Operator("Qatar Airways");

        // Create aircrafts and add flights to them
        Aircraft ac1 = new Aircraft("icaoL1", null, op1, "OwnerA");
        ac1.getFlights().addAll(Arrays.asList(new Flight(), new Flight(), new Flight())); // 3 flights

        Aircraft ac2 = new Aircraft("icaoR1", null, op2, "OwnerB");
        ac2.getFlights().addAll(Arrays.asList(new Flight(), new Flight())); // 2 flights

        Aircraft ac3 = new Aircraft("icaoQ1", null, op3, "OwnerC");
        ac3.getFlights().addAll(Arrays.asList(new Flight())); // 1 flight

        // Link aircraft with the operators
        op1.getAircrafts().add(ac1);
        op2.getAircrafts().add(ac2);
        op3.getAircrafts().add(ac3);

        // When operatorRepo.findAll() is called, return the list of operators.
        when(operatorRepo.findAll()).thenReturn(Arrays.asList(op1, op2, op3));

        int topN = 2;

        // Act: Invoke the method under test.
        List<Output> outputs = analysis.getGroupedTopNOperators(operatorRepo, topN);

        // Assert:
        // The top operator should be "icaoL1" (3 flights) and the next "icaoR1" (2 flights).
        assertEquals(2, outputs.size(), "Should return only top N (=2) operators");
        assertEquals("icaoL1", outputs.get(0).getIcao24(), "First output should be for Lufthansa with aircraft icaoL1");
        assertEquals(3, outputs.get(0).getValue(), "Lufthansa should have 3 flights");
        assertEquals("icaoR1", outputs.get(1).getIcao24(), "Second output should be for Ryanair with aircraft icaoR1");
        assertEquals(2, outputs.get(1).getValue(), "Ryanair should have 2 flights");
    }

    @Test
    void getGroupedTopNModels_shouldReturnTopNModelsSortedByFlightCount() {
        // Arrange: Create three models each with one or more aircraft.
        Model m1 = new Model("Boeing 737");
        Model m2 = new Model("Airbus A320");
        Model m3 = new Model("Embraer E190");

        // Create aircraft with flights and assign them to models.
        Aircraft ac1 = new Aircraft("icaoA1", m1, null, "Owner1");
        ac1.getFlights().addAll(Arrays.asList(new Flight(), new Flight(), new Flight())); // 3 flights

        Aircraft ac2 = new Aircraft("icaoB1", m2, null, "Owner3");
        ac2.getFlights().addAll(Arrays.asList(new Flight(), new Flight(), new Flight())); // 3 flights

        Aircraft ac3 = new Aircraft("icaoB2", m2, null, "Owner2");
        ac3.getFlights().add(new Flight()); // 1 flight -> total for m2: 4 flights

        Aircraft ac4 = new Aircraft("icaoE1", m3, null, "Owner4");
        // No flights for this aircraft

        // Add aircrafts to models.
        m1.getAircrafts().add(ac1); // Total 3 flights
        m2.getAircrafts().addAll(Arrays.asList(ac2, ac3)); // Total 4 flights
        m3.getAircrafts().add(ac4); // Total 0 flights

        // When modelRepo.findAll() is called, return the list of models.
        when(modelRepo.findAll()).thenReturn(Arrays.asList(m1, m2, m3));

        int topN = 2;

        // Act: Invoke the method under test.
        List<Output> outputs = analysis.getGroupedTopNModels(modelRepo, topN);

        // Assert:
        // The top operator should be "icaoB1" (4 flights) and the next "icaoA1" (3 flights).
        assertEquals(2, outputs.size(), "Should return only top N (=2) models");
        assertEquals("icaoB1", outputs.get(0).getIcao24(), "First output should be for Airbus A320 with aircraft icaoB1");
        assertEquals(4, outputs.get(0).getValue(), "Airbus A320 should have 4 flights");
        assertEquals("icaoA1", outputs.get(1).getIcao24(), "Second output should be for Boeing 737 with aircraft icaoA1");
        assertEquals(3, outputs.get(1).getValue(), "Boeing 737 should have 3 flights");
    }

    @Test
    void getGroupedTopNOperators_withEmptyOperatorRepo_shouldReturnEmptyList() {
        // Arrange: When operatorRepo.findAll() returns an empty list.
        when(operatorRepo.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Output> outputs = analysis.getGroupedTopNOperators(operatorRepo, 3);

        // Assert
        assertTrue(outputs.isEmpty(), "Empty operator repo should result in an empty output list");
    }

    @Test
    void getGroupedTopNModels_withEmptyModelRepo_shouldReturnEmptyList() {
        // Arrange: When modelRepo.findAll() returns an empty list.
        when(modelRepo.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Output> outputs = analysis.getGroupedTopNModels(modelRepo, 3);

        // Assert
        assertTrue(outputs.isEmpty(), "Empty model repo should result in an empty output list");
    }
}
