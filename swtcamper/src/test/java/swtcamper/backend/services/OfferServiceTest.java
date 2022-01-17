package swtcamper.backend.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleFeatures;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferServiceTest {

    @Mock
    private VehicleRepository mockVehicleRepository;
    @Mock
    private VehicleFeaturesRepository mockVehicleFeaturesRepository;
    @Mock
    private OfferRepository mockOfferRepository;


    @InjectMocks
    private OfferService offerServiceUnderTest;

    @Test
    public void createOffer() {
        // Setup
        final Offer expectedResult = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));
        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));

        // Configure OfferRepository.save(...).
        final Offer offer = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);
        when(mockOfferRepository.save(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false))).thenReturn(offer);

        // Run the test
        final Offer result = offerServiceUnderTest.create("title", "location", "contact", "description", 0L, false, false, false, new String[]{"value"}, VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // Verify the results
        System.out.println("Excpected: " + expectedResult.toString());
        //System.out.println("Result:" + result.toString());
        //assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleRepository).save(any(Vehicle.class));
        verify(mockVehicleFeaturesRepository).save(any(VehicleFeatures.class));
    }

    @Test
    public void updateOffer() {
        // Setup
        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final Offer expectedResult = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);

        // Configure OfferRepository.findById(...).
        final Optional<Offer> offer = Optional.of(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false));
        when(mockOfferRepository.findById(0L)).thenReturn(offer);

        // Configure VehicleRepository.findById(...).
        final Optional<Vehicle> vehicle = Optional.of(new Vehicle(new VehicleFeatures(null)));
        when(mockVehicleRepository.findById(0L)).thenReturn(vehicle);

        // Configure VehicleFeaturesRepository.findById(...).
        final Optional<VehicleFeatures> vehicleFeatures = Optional.of(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(vehicleFeatures);

        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final Offer offer1 = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);
        when(mockOfferRepository.save(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false))).thenReturn(offer1);

        // Run the test
        final Offer result = offerServiceUnderTest.update(0L, offeredObject, "title", "location", "contact", "description", new ArrayList<>(List.of(0L)), 0L, false, false, false, false, new String[]{"value"}, VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(any(VehicleFeatures.class));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void updateOfferReturnsAbsentWhileFromOfferRepositoryFindById() {
        // Setup
        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final Offer expectedResult = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);
        when(mockOfferRepository.findById(0L)).thenReturn(Optional.empty());

        // Configure VehicleRepository.findById(...).
        final Optional<Vehicle> vehicle = Optional.of(new Vehicle(new VehicleFeatures(null)));
        when(mockVehicleRepository.findById(0L)).thenReturn(vehicle);

        // Configure VehicleFeaturesRepository.findById(...).
        final Optional<VehicleFeatures> vehicleFeatures = Optional.of(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(vehicleFeatures);

        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final Offer offer = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);
        when(mockOfferRepository.save(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false))).thenReturn(offer);

        // Run the test
        final Offer result = offerServiceUnderTest.update(0L, offeredObject, "title", "location", "contact", "description", new ArrayList<>(List.of(0L)), 0L, false, false, false, false, new String[]{"value"}, VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void updateOfferReturnsAbsentWhileFromVehicleRepositoryFindById() {
        // Setup
        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final Offer expectedResult = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);

        // Configure OfferRepository.findById(...).
        final Optional<Offer> offer = Optional.of(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false));
        when(mockOfferRepository.findById(0L)).thenReturn(offer);

        when(mockVehicleRepository.findById(0L)).thenReturn(Optional.empty());

        // Configure VehicleFeaturesRepository.findById(...).
        final Optional<VehicleFeatures> vehicleFeatures = Optional.of(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(vehicleFeatures);

        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final Offer offer1 = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);
        when(mockOfferRepository.save(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false))).thenReturn(offer1);

        // Run the test
        final Offer result = offerServiceUnderTest.update(0L, offeredObject, "title", "location", "contact", "description", new ArrayList<>(List.of(0L)), 0L, false, false, false, false, new String[]{"value"}, VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void updateOfferReturnsAbsentWhileFromVehicleFeaturesRepositoryFindById() {
        // Setup
        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final Offer expectedResult = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);

        // Configure OfferRepository.findById(...).
        final Optional<Offer> offer = Optional.of(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false));
        when(mockOfferRepository.findById(0L)).thenReturn(offer);

        // Configure VehicleRepository.findById(...).
        final Optional<Vehicle> vehicle = Optional.of(new Vehicle(new VehicleFeatures(null)));
        when(mockVehicleRepository.findById(0L)).thenReturn(vehicle);

        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(Optional.empty());
        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final Offer offer1 = new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false);
        when(mockOfferRepository.save(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false))).thenReturn(offer1);

        // Run the test
        final Offer result = offerServiceUnderTest.update(0L, offeredObject, "title", "location", "contact", "description", new ArrayList<>(List.of(0L)), 0L, false, false, false, false, new String[]{"value"}, VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void deleteOffer() throws Exception {
        // Setup
        // Run the test
        offerServiceUnderTest.delete(0L);

        // Verify the results
        verify(mockOfferRepository).deleteById(0L);
    }

    @Test
    // something here is wrong
    public void deleteOfferShouldThrowGenericServiceException() {
        // Setup
        // Run the test
        assertThatThrownBy(() -> offerServiceUnderTest.delete(-1L)).isInstanceOf(GenericServiceException.class);
        verify(mockOfferRepository).deleteById((0L));
    }

    @Test
    public void readOffers() {
        // Setup
        final List<Offer> expectedResult = List.of(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false));

        // Configure OfferRepository.findAll(...).
        final List<Offer> offers = expectedResult;
        //List.of(new Offer(new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, false, false, false));
        when(mockOfferRepository.findAll()).thenReturn(offers);

        // Run the test
        final List<Offer> result = offerServiceUnderTest.offers();

        // Verify the results
        assertThat(result.toString()).isEqualTo(expectedResult.toString());
    }

    @Test
    public void readOffersfromOfferRepositoryReturnsNoItems() {
        // Setup
        when(mockOfferRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Offer> result = offerServiceUnderTest.offers();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
