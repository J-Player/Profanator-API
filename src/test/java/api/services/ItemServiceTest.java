package api.services;

<<<<<<< HEAD
import api.models.entities.Item;
import api.repositories.impl.ItemRepository;
=======
import api.domains.Item;
import api.domains.dtos.ItemDTO;
import api.repositories.ItemRepository;
>>>>>>> main
import api.services.cache.CacheService;
import api.services.impl.ItemService;
import api.services.impl.ProficiencyService;
import api.utils.ItemCreator;
import api.utils.ProficiencyCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> main
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Service Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProficiencyService proficiencyService;

    @Mock
    private CacheService cacheService;

<<<<<<< HEAD
    private final Item item = ItemCreator.item().withId(1);

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemRepository.findById(anyInt()))
=======
    private final Item item = ItemCreator.item();
    private final ItemDTO itemDTO = ItemCreator.itemDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemRepository.findById(anyLong()))
>>>>>>> main
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findAllBy(any(Pageable.class)))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.count())
                .thenReturn(Mono.just(1L));
        BDDMockito.when(itemRepository.findAllByProficiencyIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.save(any(Item.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.delete(any(Item.class)))
                .thenReturn(Mono.empty());
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("findById | Returns a mono of item when successful")
    void findById() {
<<<<<<< HEAD
        StepVerifier.create(itemService.findById(1))
=======
        StepVerifier.create(itemService.findById(1L))
>>>>>>> main
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when item does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
<<<<<<< HEAD
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findById(1))
=======
        BDDMockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findById(1L))
>>>>>>> main
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByName | Returns a mono of item when successful")
    void findByName() {
        StepVerifier.create(itemService.findByName(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName | Returns a mono error when item does not exists")
    void findByName_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findByName(""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAllByProficiency | Returns a flux of item when successful")
    void findAllByProficiency() {
        Pageable pageable = Pageable.ofSize(100);
        List<Item> itemList = List.of(item);
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(ProficiencyCreator.proficiency()));
        StepVerifier.create(itemService.findAllByProficiency("", pageable))
                .expectSubscription()
                .expectNext(new PageImpl<>(itemList, pageable, itemList.size()))
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll | Returns a flux of item when successful")
    void findAll() {
        Pageable pageable = Pageable.ofSize(100);
        List<Item> itemList = List.of(item);
        StepVerifier.create(itemService.findAll(pageable))
                .expectSubscription()
                .expectNext(new PageImpl<>(itemList, pageable, itemList.size()))
                .verifyComplete();
    }

    @Test
    @DisplayName("save | creates an item when successful")
    void save() {
        StepVerifier.create(itemService.save(itemDTO))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | save updated item and returns empty mono when successful")
    void update() {
        StepVerifier.create(itemService.update(itemDTO, 1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when item does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
<<<<<<< HEAD
        BDDMockito.when(itemRepository.findById(anyInt()))
=======
        BDDMockito.when(itemRepository.findById(anyLong()))
>>>>>>> main
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.update(itemDTO, 1L))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | removes the item when successful")
    void delete() {
<<<<<<< HEAD
        StepVerifier.create(itemService.delete(1))
=======
        StepVerifier.create(itemService.delete(1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns mono error when item does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
<<<<<<< HEAD
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.delete(1))
=======
        BDDMockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.delete(1L))
>>>>>>> main
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}