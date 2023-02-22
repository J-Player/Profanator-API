package api.integration.annotation;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@ActiveProfiles
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public @interface IntegrationTest {

    @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles")
    String[] activeProfiles() default {"test"};

    @AliasFor(annotation = TestMethodOrder.class, attribute = "value")
    Class<? extends MethodOrderer> methodOrder() default MethodOrderer.OrderAnnotation.class;

}
