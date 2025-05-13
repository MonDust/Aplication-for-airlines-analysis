package pg.edu.pl.lsea.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.*;
import pg.edu.pl.lsea.backend.entities.analysis.Output;
import pg.edu.pl.lsea.backend.repositories.*;
import pg.edu.pl.lsea.backend.repositories.original.*;
import pg.edu.pl.lsea.backend.services.analysis.typesofanalysis.FullGroupedTopNAnalysis;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock private FlightRepo flightRepo;
    @Mock private FlightToResponseMapper flightToResponseMapper;
    @Mock private EnrichedFlightRepo enrichedFlightRepo;
    @Mock private EnrichedFlightToResponseMapper enrichedFlightToResponseMapper;
    @Mock private AircraftRepo aircraftRepo;
    @Mock private AircraftToResponseMapper aircraftToResponseMapper;

    @Mock private OperatorRepo operatorRepo;
    @Mock private ModelRepo modelRepo;

    @InjectMocks
    private AnalysisService analysisService;

    private FullGroupedTopNAnalysis mockAnalysis;

    @BeforeEach
    void setUp() {
        analysisService = new AnalysisService(
                flightRepo, flightToResponseMapper,
                enrichedFlightRepo, enrichedFlightToResponseMapper,
                operatorRepo, modelRepo,
                aircraftRepo, aircraftToResponseMapper
        );

        mockAnalysis = spy(new FullGroupedTopNAnalysis(
                flightRepo, flightToResponseMapper,
                enrichedFlightRepo, enrichedFlightToResponseMapper,
                aircraftRepo, aircraftToResponseMapper
        ));

        // inject the spy to replace the real one
        var analysisField = AnalysisService.class.getDeclaredFields()[0];
        analysisField.setAccessible(true);
        try {
            analysisField.set(analysisService, mockAnalysis);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnTopNOperatorsFromAnalysisFunction() {
        // Arrange
        List<Output> mockedResult = List.of(new Output("ICAO24", 123));
        when(mockAnalysis.getGroupedTopNOperators(operatorRepo, 1)).thenReturn(mockedResult);

        // Act
        List<Output> result = analysisService.getTopNOperatorWithNumberOfFlights(1);

        // Assert
        assertEquals(mockedResult, result);
        verify(mockAnalysis).getGroupedTopNOperators(operatorRepo, 1);
    }
}
