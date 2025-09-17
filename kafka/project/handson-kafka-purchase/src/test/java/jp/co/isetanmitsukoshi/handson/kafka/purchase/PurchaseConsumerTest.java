package jp.co.isetanmitsukoshi.handson.kafka.purchase;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@DirtiesContext
@Testcontainers
class PurchaseConsumerTest {
    /*
     * https://java.testcontainers.org/modules/kafka/
     * https://docs.confluent.io/platform/current/installation/versions-interoperability.html#cp-and-apache-kafka-compatibility
     */
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    // @DynamicPropertySourceアノテーションを使用すると、各種コンフィグレーションプロパティに動的な値を設定できます。
    // ここではKafkaPropertiesのbootstrapServersフィールドに値を設定しています。
    // コンフィグレーションプロパティに値を設定するには、application.properties (yml)、環境変数、システムプロパティ、
    // 他、複数の方法が用意されており、@DynamicPropertySourceによる設定もそのひとつです。
    // https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafkaContainer.getBootstrapServers());
    }

    @Autowired
    KafkaTemplate<String, Purchase> kafkaTemplate;

    // @MockBeanアノテーションを使用するとDIコンテナ内に登録されているBeanオブジェクトを、
    // モックインスタンスに置き換えることができます。
    @MockBean
    CalculateApplicationService calculateApplicationService;

    @Test
    @DisplayName(
            """
            Given configured Kafka listener,
            When produce message,
            Then consume produced message by listener
            """
    )
    void given_configured_Kafka_listener__when_produce_message__then_consume_produced_message_by_listener() throws Exception {
        /*----------------------------------------------------------------------
         * Given
         *----------------------------------------------------------------------*/

        final AtomicReference<Purchase> atomicRef = new AtomicReference<>();
        /*
         * カウントダウンラッチは、マルチスレッド環境下において、
         * 他方のスレッドの任意の処理の完了を待ち受けるために使用するものです。
         *
         * ラッチとは、いわゆる"かんぬき"のことです。
         * 門や扉の錠前として使用され、横方向にスライドしていき、
         * 一定量のスライドを完了すると、解錠される仕組みの鍵です。
         *
         * カウントダウンラッチはコンストラクタの引数で、"かんぬき"の解錠に必要なスライド量を定義し、
         * 任意の条件を満たした場合にのみ、これをスライド(カウントダウン)していくことで、
         * すべての条件を満たすまで、施錠しておく(処理を通さない)ような待受処理を実現できます。
         *
         * ここでは解錠に必要なスライド量を1に設定しています。
         */
        final CountDownLatch latch = new CountDownLatch(1);

        /*
         * モックインスタンスに置き換えられているCalculateApplicationServiceの、
         * calculateメソッドに、テスト時のみ動作内容を定義します。
         *
         * 以下の流れで適用していきます。
         * doAnswer(動作内容).when(適用インスタンス).適用メソッド(引数パターン)
         */
        Mockito.doAnswer(invocationOnMock -> {
            /*
             * calculateApplicationServiceは、PurchaseConsumer内のlistenメソッドから呼び出されます。
             * listenメソッドはリスナースレッド上で動作するため、
             * テストスレッドとは異なるスレッド上で動作することになります。
             *
             * スレッド同士は通常、直接干渉し合うことができないため、
             * 値の受け渡しなどをしたい場合には工夫が必要になります。
             * 外部データストアを使用したり、static変数を使用したりすることで、
             * スレッド間の値の受け渡しを実現できますが、
             * AtomicReferenceオブジェクトを使用することでも、これを実現可能です。
             *
             * ここではリスナースレッドから、テストスレッドに値を受け渡すため、
             * AtomicReferenceにモックインスタンスのメソッドが受け取った値を書き出しています。
             */
            atomicRef.set(invocationOnMock.getArgument(0, Purchase.class));
            /*
             * calculateApplicationServiceのcalculateメソッドが呼び出されたら、"かんぬき"をスライドします。
             * ここでは解錠に必要なスライド量が1に設定されていたので、countDownが一度呼び出されたら解錠されます。
             */
            latch.countDown();
            return null;
        }).when(calculateApplicationService).calculate(Mockito.any());

        /*----------------------------------------------------------------------
         * When
         *----------------------------------------------------------------------*/

        Purchase purchase = new Purchase(
                "food_takeout",
                "kfc red hot chicken",
                320,
                5
        );

        /*
         * Kafkaトピックへ、直接メッセージを投入します。
         *
         * プロダクションコードが正しく実装されていれば、トピックにメッセージが投入されたことをトリガーに、
         * リスナースレッドがこのメッセージを取得します。
         */
        kafkaTemplate.sendDefault(purchase.name(), purchase);

        /*
         * カウントダウンラッチにより、CalculateApplicationService#calculateが呼び出されるまで、処理を待機させます。
         *
         * 以下の流れが全て完了すると、"かんぬき"が解錠され、後続処理に進めるようになります。
         *
         * 1. メッセージがKafkaトピックに投入される
         * 2. リスナースレッドがKafkaトピックからメッセージを取得する
         * 3. リスナースレッドがCalculateApplicationService#calculateに取得したメッセージを受け渡す
         * 4. CalculateApplicationService#calculateが呼び出されたことで、"かんぬき"が開場される
         */
        if (!latch.await(10_000L, TimeUnit.MILLISECONDS)) {
            /*
             * 10秒待っても解錠されなかった場合、どこかに設定ミス等が存在する可能性が高いため、
             * テストを失敗させて終了します。
             */
            Assertions.fail("Latch timeout");
        }

        /*----------------------------------------------------------------------
         * Then
         *----------------------------------------------------------------------*/

        /*
         * verifyメソッドでは、モッキングした処理が、意図したとおりに呼び出されたかどうかを検査できます。
         *
         * ここでは、モッキングしたCalculateApplicationService#calculateが呼び出されたかどうかを確認しています。
         */
        Mockito.verify(calculateApplicationService).calculate(purchase);

        /*
         * 異なるスレッド間で値の受け渡しが可能なAtomicReferenceオブジェクトから、
         * リスナースレッド上で書き出していた値を取得して、
         * Kafkaトピックに投入した値と同じものが連携されたかどうかを検証します。
         */
        Assertions.assertThat(atomicRef.get()).isEqualTo(purchase);
    }
}
