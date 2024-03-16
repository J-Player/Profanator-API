package api.controllers;

import api.controllers.impl.ItemController;
import api.mappers.ItemMapper;
import api.models.dtos.ItemDTO;
import api.models.entities.Item;
import api.services.impl.ItemService;
import api.util.ItemCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Controller Test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private final ItemDTO itemDTO = ItemCreator.itemDTO();
    private final Item item = ItemMapper.INSTANCE.toItem(itemDTO);

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemService.findById(anyInt()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findByName(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findAll(any(Pageable.class)))
                .thenReturn(Mono.just(new PageImpl<>(List.of(item))));
        BDDMockito.when(itemService.findAllByProficiency(anyString(), any()))
                .thenReturn(Mono.just(new PageImpl<>(List.of(item))));
        BDDMockito.when(itemService.save(any(Item.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.update(any(Item.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(itemService.delete(anyInt()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        StepVerifier.create(itemController.findById(1))
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
        StepVerifier.create(itemController.update(1, itemDTO))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(itemController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}