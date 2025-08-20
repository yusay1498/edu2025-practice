package com.example.demo_jpa;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

@DataJpaTest
@Import(TestContainerConfig.class)
class JpaCardRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @DynamicPropertySource
    static void setDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.show-sql", () -> true);
    }

    @Autowired
    JpaCardRepository jpaCardRepository;

    @Test
    void testFindAll() {
        Element testElement = new Element();
        testElement.setId(200);
        testElement.setName("test element");

        testEntityManager.persist(testElement);

        Card testCard = new Card();
        testCard.setId(1000);
        testCard.setName("test card");
        testCard.setLevel(1);
        testCard.setElement(testElement);
        testCard.setTop(1);
        testCard.setRight(1);
        testCard.setBottom(1);
        testCard.setLeft(1);

        testEntityManager.persist(testCard);

        List<Card> actual = jpaCardRepository.findAll();

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.size()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getId()).isEqualTo(1000);
        Assertions.assertThat(actual.get(0).getName()).isEqualTo("test card");
        Assertions.assertThat(actual.get(0).getLevel()).isEqualTo(1);

        Assertions.assertThat(actual.get(0).getElement().getId()).isEqualTo(200);
        Assertions.assertThat(actual.get(0).getElement().getName()).isEqualTo("test element");

        Assertions.assertThat(actual.get(0).getTop()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getRight()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getBottom()).isEqualTo(1);
        Assertions.assertThat(actual.get(0).getLeft()).isEqualTo(1);
    }
}