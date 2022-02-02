package swtcamper.backend.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.UserRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfferServiceTest {

    @Mock
    private OfferService mockOfferService;
    @Mock
    private ModelMapper mockModelMapper;
    @Autowired
    private VehicleRepository mockVehicleRepository;
    @Autowired
    private VehicleFeaturesRepository mockVehicleFeaturesRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerServiceUnderTest;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createOffer() throws GenericServiceException {
        // given
        User creator = userService.create("Tester", "password", "a@a.de","123456789", "Tester", "Tester", UserRole.OPERATOR, true);

        final Offer expectedResult = new Offer(creator, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));
        //when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));
        //when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));

        // Configure OfferRepository.save(...).
        //final User user1 = new User();
        //final Offer offer = new Offer(user1, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));
        //when(offerRepository.save(new Offer(new User(), new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))))).thenReturn(offer);

        // when
        final Offer result = offerServiceUnderTest.create(creator, "title", "location", "contact", "particularities", 0L, new ArrayList<>(List.of("value")), VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // then
        assertThat(result).isEqualTo(expectedResult);
        // verify(mockVehicleRepository).save(any(Vehicle.class));
        // verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
    }

    @Test
    public void updateOffer() {
        // given
        final User creator = new User();

        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final User user = new User();
        final Offer expectedResult = new Offer(user, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));

        // Configure OfferRepository.findById(...).
        final User user1 = new User();
        final Optional<Offer> offer = Optional.of(new Offer(user1, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))));
        when(offerRepository.findById(0L)).thenReturn(offer);

        // Configure VehicleRepository.findById(...).
        final Optional<Vehicle> vehicle = Optional.of(new Vehicle(new VehicleFeatures(null)));
        when(mockVehicleRepository.findById(0L)).thenReturn(vehicle);

        // Configure VehicleFeaturesRepository.findById(...).
        final Optional<VehicleFeatures> vehicleFeatures = Optional.of(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(vehicleFeatures);

        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final User user2 = new User();
        final Offer offer1 = new Offer(user2, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));
        when(offerRepository.save(new Offer(new User(), new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))))).thenReturn(offer1);

        // when
        //final Offer result = offerServiceUnderTest.update(0L, creator, offeredObject, "title", "location", "contact", "particularities", new ArrayList<>(List.of(0L)), 0L, false, new ArrayList<>(List.of("value")), VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // then
        //assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void updateOfferReturnsAbsentWhileFromOfferRepositoryFindById() {
        // given
        final User creator = new User();

        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final User user = new User();
        final Offer expectedResult = new Offer(user, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));
        when(offerRepository.findById(0L)).thenReturn(Optional.empty());

        // Configure VehicleRepository.findById(...).
        final Optional<Vehicle> vehicle = Optional.of(new Vehicle(new VehicleFeatures(null)));
        when(mockVehicleRepository.findById(0L)).thenReturn(vehicle);

        // Configure VehicleFeaturesRepository.findById(...).
        final Optional<VehicleFeatures> vehicleFeatures = Optional.of(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(vehicleFeatures);

        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final User user1 = new User();
        final Offer offer = new Offer(user1, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));
        when(offerRepository.save(new Offer(new User(), new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))))).thenReturn(offer);

        // when
        //final Offer result = offerServiceUnderTest.update(0L, creator, offeredObject, "title", "location", "contact", "particularities", new ArrayList<>(List.of(0L)), 0L, false, new ArrayList<>(List.of("value")), VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // then
        //assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void updateOfferReturnsAbsentWhileFromVehicleRepositoryFindById() {
        // given
        final User creator = new User();

        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final User user = new User();
        final Offer expectedResult = new Offer(user, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));

        // Configure OfferRepository.findById(...).
        final User user1 = new User();
        final Optional<Offer> offer = Optional.of(new Offer(user1, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))));
        when(offerRepository.findById(0L)).thenReturn(offer);

        when(mockVehicleRepository.findById(0L)).thenReturn(Optional.empty());

        // Configure VehicleFeaturesRepository.findById(...).
        final Optional<VehicleFeatures> vehicleFeatures = Optional.of(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(vehicleFeatures);

        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final User user2 = new User();
        final Offer offer1 = new Offer(user2, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));
        when(offerRepository.save(new Offer(new User(), new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))))).thenReturn(offer1);

        // when
        //final Offer result = offerServiceUnderTest.update(0L, creator, offeredObject, "title", "location", "contact", "particularities", new ArrayList<>(List.of(0L)), 0L, false, new ArrayList<>(List.of("value")), VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // then
        //assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void updateOfferReturnsAbsentWhileFromVehicleFeaturesRepositoryFindById() {
        // given
        final User creator = new User();

        final Vehicle offeredObject = new Vehicle(new VehicleFeatures(null));
        final User user = new User();
        final Offer expectedResult = new Offer(user, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));

        // Configure OfferRepository.findById(...).
        final User user1 = new User();
        final Optional<Offer> offer = Optional.of(new Offer(user1, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))));
        when(offerRepository.findById(0L)).thenReturn(offer);

        // Configure VehicleRepository.findById(...).
        final Optional<Vehicle> vehicle = Optional.of(new Vehicle(new VehicleFeatures(null)));
        when(mockVehicleRepository.findById(0L)).thenReturn(vehicle);

        when(mockVehicleFeaturesRepository.findById(0L)).thenReturn(Optional.empty());
        when(mockVehicleFeaturesRepository.save(new VehicleFeatures(new Vehicle(null)))).thenReturn(new VehicleFeatures(new Vehicle(null)));
        when(mockVehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle(new VehicleFeatures(null)));

        // Configure OfferRepository.save(...).
        final User user2 = new User();
        final Offer offer1 = new Offer(user2, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value")));
        when(offerRepository.save(new Offer(new User(), new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))))).thenReturn(offer1);

        // when
        //final Offer result = offerServiceUnderTest.update(0L, creator, offeredObject, "title", "location", "contact", "particularities", new ArrayList<>(List.of(0L)), 0L, false, new ArrayList<>(List.of("value")), VehicleType.CAMPER, "make", "model", "year", 0.0, 0.0, 0.0, "engine", "transmission", 0, 0, false, false, false, false, false, false, false);

        // then
        //assertThat(result).isEqualTo(expectedResult);
        verify(mockVehicleFeaturesRepository).save(new VehicleFeatures(new Vehicle(null)));
        verify(mockVehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void deleteOffer() throws Exception {
        // given
        User creator = new User();
        UserDTO userDTO = mockModelMapper.userToUserDTO(creator);

        // when
        offerServiceUnderTest.delete(0L, userDTO);

        // then
        verify(offerRepository).deleteById(0L);
    }

    @Test
    public void deleteOfferShouldThrowGenericServiceException() {
        // given
        final User creator = new User();

        // when
        //assertThatThrownBy(() -> offerServiceUnderTest.delete(0L)).isInstanceOf(IllegalArgumentException.class);
        verify(offerRepository).deleteById(0L);
    }

    @Test
    public void readOffers() {
        // given
        final User user = new User();
        final List<Offer> expectedResult = List.of(new Offer(user, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))));

        // Configure OfferRepository.findAll(...).
        final User user1 = new User();
        final List<Offer> offers = List.of(new Offer(user1, new Vehicle(new VehicleFeatures(null)), new ArrayList<>(List.of(0L)), "title", "location", "contact", "particularities", 0L, false, new ArrayList<>(List.of("value"))));
        when(offerRepository.findAll()).thenReturn(offers);

        // when
        final List<Offer> result = offerServiceUnderTest.offers();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void readOffersFromOfferRepositoryReturnsNoItems() {
        // given
        when(offerRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        final List<Offer> result = offerServiceUnderTest.offers();

        // then
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
