package pg.edu.pl.lsea.backend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pg.edu.pl.lsea.backend.entities.analysis.Output;
import pg.edu.pl.lsea.backend.repositories.ModelRepo;
import pg.edu.pl.lsea.backend.repositories.OperatorRepo;
import pg.edu.pl.lsea.backend.services.analysis.typesofanalysis.FullGroupedTopNAnalysis;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private FullGroupedTopNAnalysis analysisFunc;

    @Mock
    private OperatorRepo operatorRepo;

    @Mock
    private ModelRepo modelRepo;

    @InjectMocks
    private AnalysisService analysisService;

    @Test
    void getTopNOperatorWithNumberOfFlights_shouldReturnCorrectResults() {
        // Arrange
        int topN = 3;
        List<Output> expectedOutput = List.of(
                new Output("icao1", 100),
                new Output("icao2", 80)
        );
        when(analysisFunc.getGroupedTopNOperators(operatorRepo, topN)).thenReturn(expectedOutput);

        // Act
        List<Output> result = analysisService.getTopNOperatorWithNumberOfFlights(topN);

        // Assert
        assertEquals(expectedOutput, result);
        verify(analysisFunc).getGroupedTopNOperators(operatorRepo, topN);
    }

    @Test
    void getTopNModelWithNumberOfFlights_shouldReturnCorrectResults() {
        // Arrange
        int topN = 2;
        List<Output> expectedOutput = List.of(
                new Output("icaoM1", 120),
                new Output("icaoM2", 90)
        );
        when(analysisFunc.getGroupedTopNModels(modelRepo, topN)).thenReturn(expectedOutput);

        // Act
        List<Output> result = analysisService.getTopNModelWithNumberOfFlights(topN);

        // Assert
        assertEquals(expectedOutput, result);
        verify(analysisFunc).getGroupedTopNModels(modelRepo, topN);
    }

    @Test
    void getTopNPercentageOfLongFlights_shouldReturnCorrectResults() {
        // Arrange
        int topN = 5;
        List<Output> expectedOutput = List.of(new Output("icaoX", 45));
        when(analysisFunc.getTopNOperatorsPercentages(topN)).thenReturn(expectedOutput);

        // Act
        List<Output> result = analysisService.getTopNPercentageOfLongFlights_GroupedByOperator(topN);

        // Assert
        assertEquals(expectedOutput, result);
        verify(analysisFunc).getTopNOperatorsPercentages(topN);
    }

    @Test
    void getTopNAverageTimeGroupedByOperator_shouldReturnCorrectResults() {
        // Arrange
        int topN = 4;
        List<Output> expectedOutput = List.of(
                new Output("icaoY", 60)
        );
        when(analysisFunc.getAverageTimesForOperators(topN)).thenReturn(expectedOutput);

        // Act
        List<Output> result = analysisService.getTopNAverageTime_GroupedByOperator(topN);

        // Assert
        assertEquals(expectedOutput, result);
        verify(analysisFunc).getAverageTimesForOperators(topN);
    }

    @Test
    void getTopNOperatorWithNumberOfFlights_shouldHandleEmptyResultGracefully() {
        // Arrange
        int topN = 0;
        when(analysisFunc.getGroupedTopNOperators(operatorRepo, topN)).thenReturn(List.of());

        // Act
        List<Output> result = analysisService.getTopNOperatorWithNumberOfFlights(topN);

        // Assert
        assertEquals(0, result.size());
        verify(analysisFunc).getGroupedTopNOperators(operatorRepo, topN);
    }
}
