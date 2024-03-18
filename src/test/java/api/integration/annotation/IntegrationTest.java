package api.integration.annotation;

<<<<<<< HEAD
import api.configs.data.DatabaseConfigTest;
import api.configs.security.SecurityConfigTest;
=======
import api.configs.security.SecurityConfig;
>>>>>>> main
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@ActiveProfiles("test")
@AutoConfigureWebTestClient
<<<<<<< HEAD
@TestMethodOrder(MethodOrderer.DisplayName.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {DatabaseConfigTest.class, SecurityConfigTest.class})
=======
@Import({SecurityConfig.class})
@TestMethodOrder(MethodOrderer.DisplayName.class)
>>>>>>> main
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public @interface IntegrationTest {
}
