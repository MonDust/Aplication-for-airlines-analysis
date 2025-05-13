package pg.edu.pl.lsea.files;

import org.junit.jupiter.api.Test;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvDataReaderTest {

    @Test
    void readAircraftsTest_CorrectData() throws Exception {
        //Arrange
        String testFileContent = "\"0004fc\",\"ZK379\",\"EUROFIGHTER\",\"Eurofighter\",\"Typhoon T3\",\"EUFI\",\"BT024\",\"\",\"L2J\",\"Royal Air Force\",\"VORTEX\",\"SHF\",\"\",\"Royal Air Force\"";

        File tempFileCorrect = File.createTempFile("tempFileCorrect_Aircrafts", ".csv");
        Files.writeString(tempFileCorrect.toPath(), testFileContent);

        //Act
        CsvDataReader testDataLoader = new CsvDataReader();
        List<Aircraft> result = testDataLoader.readAircrafts(tempFileCorrect);

        //Assert
        assertEquals(1, result.size());
        assertEquals("0004fc", result.getFirst().getIcao24());
        assertEquals("Eurofighter", result.getFirst().getModel());
        assertEquals("Royal Air Force", result.getFirst().getOperator());
        assertEquals("Royal Air Force", result.getFirst().getOwner());
        tempFileCorrect.delete();
    }

    @Test
    void readAircraftsTest_IncorrectData() throws Exception {
        //Arrange
        String testFileContent = "Invalid Data";
        File tempFileIncorrect = File.createTempFile("tempFileIncorrect_Aircrafts", ".csv");
        Files.writeString(tempFileIncorrect.toPath(), testFileContent);

        //Act
        CsvDataReader testDataLoader = new CsvDataReader();
        List<Aircraft> result = testDataLoader.readAircrafts(tempFileIncorrect);

        //Assert
        assertEquals(0, result.size());
        tempFileIncorrect.delete();
    }

    @Test
    void readAircraftsTest_EmptyFile() throws Exception {
        //Arrange
        String testFileContent = "";
        File tempFileEmpty = File.createTempFile("tempFileEmpty_Aircrafts", ".csv");
        Files.writeString(tempFileEmpty.toPath(), testFileContent);

        //Act
        CsvDataReader testDataLoader = new CsvDataReader();
        List<Aircraft> result = testDataLoader.readAircrafts(tempFileEmpty);

        //Assert
        assertEquals(0, result.size());
        tempFileEmpty.delete();
    }

    @Test
    void readAircraftsTest_Header() throws Exception {
        //Arrange
        String testFileContent = """
                "icao24","registration","manufacturericao","manufacturername","model","typecode","serialnumber","linenumber","icaoaircrafttype","operator","operatorcallsign","operatoricao","operatoriata","owner","testreg","registered","reguntil","status","built","firstflightdate","seatconfiguration","engines","modes","adsb","acars","notes","categoryDescription"
                """;

        File tempFileHeader = File.createTempFile("tempFileHeader_Aircrafts", ".csv");
        Files.writeString(tempFileHeader.toPath(), testFileContent);

        //Act
        CsvDataReader testDataLoader = new CsvDataReader();
        List<Aircraft> result = testDataLoader.readAircrafts(tempFileHeader);

        //Assert
        assertEquals(0, result.size());
        tempFileHeader.delete();
    }

    @Test
    void readFlightsTest_CorrectData() throws Exception {
        // Arrange
        String testFileContent = "\"0004fc\",16226599,16226632,\"\",EDDM,EGVN,\"\",\"\",\"\"";
        File tempFile = File.createTempFile("tempFileCorrect_Flights", ".csv");
        Files.writeString(tempFile.toPath(), testFileContent);

        // Act
        CsvDataReader reader = new CsvDataReader();
        List<Flight> result = reader.readFlights(tempFile);

        // Assert
        assertEquals(1, result.size());
        Flight flight = result.getFirst();
        assertEquals("0004fc", flight.getIcao24());
        assertEquals(16226599, flight.getFirstSeen());
        assertEquals(16226632, flight.getLastSeen());
        assertEquals("EDDM", flight.getDepartureAirport());
        assertEquals("EGVN", flight.getArrivalAirport());
        tempFile.delete();
    }

    @Test
    void readFlightsTest_HeaderOnly() throws Exception {
        // Arrange
        String testFileContent = "\"icao24\",\"firstSeen\",\"lastSeen\",\"callsign\",\"estDepartureAirport\",\"estArrivalAirport\",\"estDepartureAirportHorizDistance\",\"estDepartureAirportVertDistance\",\"estArrivalAirportHorizDistance\"";
        File tempFile = File.createTempFile("tempFileHeader_Flights", ".csv");
        Files.writeString(tempFile.toPath(), testFileContent);

        // Act
        CsvDataReader reader = new CsvDataReader();
        List<Flight> result = reader.readFlights(tempFile);

        // Assert
        assertEquals(0, result.size());
        tempFile.delete();
    }

    @Test
    void readFlightsTest_IncorrectData() throws Exception {
        // Arrange
        String testFileContent = "Invalid,Data,Not,Enough,Columns";
        File tempFile = File.createTempFile("tempFileIncorrect_Flights", ".csv");
        Files.writeString(tempFile.toPath(), testFileContent);

        // Act
        CsvDataReader reader = new CsvDataReader();
        List<Flight> result = reader.readFlights(tempFile);

        // Assert
        assertEquals(0, result.size());
        tempFile.delete();
    }

    @Test
    void readFlightsTest_EmptyFile() throws Exception {
        // Arrange
        String testFileContent = "";
        File tempFile = File.createTempFile("tempFileEmpty_Flights", ".csv");
        Files.writeString(tempFile.toPath(), testFileContent);

        // Act
        CsvDataReader reader = new CsvDataReader();
        List<Flight> result = reader.readFlights(tempFile);

        // Assert
        assertEquals(0, result.size());
        tempFile.delete();
    }

    @Test
    void readFlightsTest_MissingTimes() throws Exception {
        // Arrange
        String testFileContent = "\"0004fc\",\"\",\"\",\"\",EDDM,EGVN,\"\",\"\",\"\"";
        File tempFile = File.createTempFile("tempFileMissingTimes_Flights", ".csv");
        Files.writeString(tempFile.toPath(), testFileContent);

        // Act
        CsvDataReader reader = new CsvDataReader();
        List<Flight> result = reader.readFlights(tempFile);

        // Assert
        assertEquals(1, result.size());
        Flight flight = result.getFirst();
        assertEquals(0, flight.getFirstSeen());
        assertEquals(0, flight.getLastSeen());
        tempFile.delete();
    }

}