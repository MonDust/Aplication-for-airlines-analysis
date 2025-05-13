package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pg.edu.pl.lsea.backend.entities.Operator;
import pg.edu.pl.lsea.backend.entities.analysis.Output;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.repositories.OperatorRepo;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FullGroupedTopNAnalysisTest {
    private FullGroupedTopNAnalysis analysis;
    private OperatorRepo operatorRepo;

    @BeforeEach
    void setUp() {
        analysis = mock(FullGroupedTopNAnalysis.class, CALLS_REAL_METHODS);
        operatorRepo = mock(OperatorRepo.class);
    }

    @Test
    void shouldReturnTopNOperatorsSortedByNumberOfFlights() {
        // Arrange
        Aircraft aircraft1 = mock(Aircraft.class);
        when(aircraft1.getFlights()).thenReturn(List.of(new Object(), new Object())); // 2 flights
        when(aircraft1.getIcao24()).thenReturn("ICAO1");

        Aircraft aircraft2 = mock(Aircraft.class);
        when(aircraft2.getFlights()).thenReturn(List.of(new Object())); // 1 flight
        when(aircraft2.getIcao24()).thenReturn("ICAO2");

        Operator operator1 = mock(Operator.class);
        when(operator1.getAircrafts()).thenReturn(List.of(aircraft1));

        Operator operator2 = mock(Operator.class);
        when(operator2.getAircrafts()).thenReturn(List.of(aircraft2));

        when(operatorRepo.findAll()).thenReturn(List.of(operator1, operator2));

        // Act
        List<Output> result = analysis.getGroupedTopNOperators(operatorRepo, 1);

        // Assert
        assertEquals(1, result.size());
        assertEquals("ICAO1", result.get(0).getIcao24());
        assertEquals(2, result.get(0).getValue());
    }

    @Test
    void shouldHandleOperatorWithNoAircrafts() {
        Operator operator = mock(Operator.class);
        when(operator.getAircrafts()).thenReturn(Collections.emptyList());

        when(operatorRepo.findAll()).thenReturn(List.of(operator));

        List<Output> result = analysis.getGroupedTopNOperators(operatorRepo, 1);

        assertEquals(1, result.size());
        assertEquals("UNKNOWN", result.get(0).getIcao24());
        assertEquals(0, result.get(0).getValue());
    }
}
