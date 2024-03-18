package api.controllers;

<<<<<<< HEAD
import api.controllers.impl.ItemController;
import api.mappers.ItemMapper;
import api.models.dtos.ItemDTO;
import api.models.entities.Item;
=======
import api.domains.Item;
import api.domains.dtos.ItemDTO;
>>>>>>> main
import api.services.impl.ItemService;
import api.utils.ItemCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
<<<<<<< HEAD
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

=======
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

>>>>>>> main
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Controller Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private final ItemDTO itemDTO = ItemCreator.itemDTO();
    private final Item item = ItemMapper.INSTANCE.toItem(itemDTO);

    @BeforeEach
    void setUp() {
<<<<<<< HEAD
        BDDMockito.when(itemService.findById(anyInt()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findByName(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findAll(any(Pageable.class)))
                .thenReturn(Mono.just(new PageImpl<>(List.of(item))));
        BDDMockito.when(itemService.findAllByProficiency(anyString(), any()))
                .thenReturn(Mono.just(new PageImpl<>(List.of(item))));
        BDDMockito.when(itemService.save(any(Item.class)))
=======
        BDDMockito.when(itemService.findById(anyLong()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findByName(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findAll())
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemService.findAllByProficiency(anyString()))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemService.save(any(ItemDTO.class)))
>>>>>>> main
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.update(any(ItemDTO.class), anyLong()))
                .thenReturn(Mono.empty());
<<<<<<< HEAD
        BDDMockito.when(itemService.delete(anyInt()))
=======
        BDDMockito.when(itemService.delete(anyLong()))
>>>>>>> main
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a item when successful")
    void findById() {
<<<<<<< HEAD
        StepVerifier.create(itemController.findById(1))
=======
        StepVerifier.create(itemController.findById(1L))
>>>>>>> main
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName | Returns a item when successful")
    void findByName() {
        StepVerifier.create(itemController.findByName(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all items by proficiency when successful")
    void listAll() {
        StepVerifier.create(itemController.listAll("", Pageable.unpaged()))
                .expectSubscription()
                .expectNext(new PageImpl<>(List.of(item)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Returns a item when successful")
    void save() {
        StepVerifier.create(itemController.save(itemDTO))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
<<<<<<< HEAD
        StepVerifier.create(itemController.update(1, itemDTO))
=======
        StepVerifier.create(itemController.update(itemDTO, 1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
<<<<<<< HEAD
        StepVerifier.create(itemController.delete(1))
=======
        StepVerifier.create(itemController.delete(1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

}