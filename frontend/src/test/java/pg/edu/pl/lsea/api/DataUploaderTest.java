package pg.edu.pl.lsea.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pg.edu.pl.lsea.entities.Aircraft;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataUploaderTest {

    @Mock
    private HttpClient mockClient;
    @Mock
    private HttpResponse<String> mockResponse;

    @Mock
    private ObjectMapper mockMapper;

    private DataUploader dataUploader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUploader = new DataUploader(mockClient, mockMapper);
    }

    @Test
    void sendAircraftsTest_CorrectData() throws Exception {
        // Arrange
        Aircraft aircraft = new Aircraft();
        aircraft.setIcao24("0004fc");
        aircraft.setModel("Eurofighter");
        aircraft.setOperator("Royal Air Force");
        aircraft.setOwner("Royal Air Force");

        List<Aircraft> aircrafts = List.of(aircraft);

        String json = "[{\"icao24\":\"0004fc\",\"model\":\"Eurofighter\",\"operator\":\"Royal Air Force\",\"owner\":\"Royal Air Force\"}]";
        when(mockMapper.writeValueAsString(aircrafts)).thenReturn(json);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);

        //Act
        dataUploader.sendAircrafts(aircrafts);

        //Assert
        verify(mockMapper).writeValueAsString(aircrafts);
        verify(mockClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(mockResponse).statusCode();
        assertEquals(200, mockResponse.statusCode());
    }

    @Test
    void sendAircraftsTest_Empty() throws Exception {
        // Arrange
        List<Aircraft> aircrafts = Collections.emptyList();

        //Act
        dataUploader.sendAircrafts(aircrafts);

        //Assert
        verify(mockMapper).writeValueAsString(aircrafts);
        verifyNoInteractions(mockClient);
    }

    @Test
    void sendAircraftsTest_SerializationError() throws Exception {
        // Arrange
        Aircraft aircraft = new Aircraft();
        aircraft.setIcao24("test");
        List<Aircraft> aircrafts = List.of(aircraft);

        when(mockMapper.writeValueAsString(aircrafts))
                .thenThrow(new JsonProcessingException("Serialization Failed") {
                });

        //Act and assert
        assertDoesNotThrow(() -> dataUploader.sendAircrafts(aircrafts));

        verify(mockMapper).writeValueAsString(aircrafts);
        verifyNoInteractions(mockClient);
    }

    @Test
    void sendAircraftsTest_HttpRequestError() throws Exception {
        // Arrange
        Aircraft aircraft = new Aircraft();
        aircraft.setIcao24("test");
        List<Aircraft> aircrafts = List.of(aircraft);

        when(mockMapper.writeValueAsString(aircrafts)).thenReturn("[]");
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Request failed"));
        //Act and assert
        assertDoesNotThrow(() -> dataUploader.sendAircrafts(aircrafts));

        verify(mockMapper).writeValueAsString(aircrafts);
        verify(mockClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verifyNoInteractions(mockResponse);
    }


}