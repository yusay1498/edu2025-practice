package com.example.sales.api.application.service;

import com.example.sales.api.domain.entity.Item;
import com.example.sales.api.domain.repository.ItemRepository;
import com.example.sales.api.application.service.ItemApplicationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

class ItemApplicationServiceTest {
    @Test
    void testListItem() {
        List<Item> itemList = List.of(
                new Item(1500, "item1", 2000, 1700),
                new Item(1501, "item2", 2000, 100),
                new Item(1502, "item3", 2500, 2000)
        );

        Page<Item> itemPage = new PageImpl<>(itemList);

        ItemRepository mockedItemRepository = Mockito.mock(ItemRepository.class);

        Mockito.doReturn(itemPage).when(mockedItemRepository).findAll(Mockito.any(Pageable.class));

        ItemApplicationService itemApplicationService = new ItemApplicationService(mockedItemRepository);

        Page<Item> actual = itemApplicationService.listItem(PageRequest.of(0, 20));

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getTotalElements()).isEqualTo(itemList.size());

        Item firstItem = actual.getContent().get(0);
        Assertions.assertThat(firstItem.id()).isEqualTo(1500);
        Assertions.assertThat(firstItem.name()).isEqualTo("item1");
        Assertions.assertThat(firstItem.price()).isEqualTo(2000);
        Assertions.assertThat(firstItem.sellPrice()).isEqualTo(1700);

        Item secondItem = actual.getContent().get(1);
        Assertions.assertThat(secondItem.id()).isEqualTo(1501);
        Assertions.assertThat(secondItem.name()).isEqualTo("item2");
        Assertions.assertThat(secondItem.price()).isEqualTo(2000);
        Assertions.assertThat(secondItem.sellPrice()).isEqualTo(100);

        Item thirdItem = actual.getContent().get(2);
        Assertions.assertThat(thirdItem.id()).isEqualTo(1502);
        Assertions.assertThat(thirdItem.name()).isEqualTo("item3");
        Assertions.assertThat(thirdItem.price()).isEqualTo(2500);
        Assertions.assertThat(thirdItem.sellPrice()).isEqualTo(2000);

        Mockito.verify(mockedItemRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }
}