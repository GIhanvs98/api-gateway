package lk.gihanvs.knowledge.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Basic application startup verification test for API Gateway.
 * Configures standalone test properties without requiring external Eureka server connectivity.
 */
@SpringBootTest(properties = {
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false",
        "spring.cloud.gateway.discovery.locator.enabled=false"
})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        // Verifies successful initialization of the API Gateway application context
    }
}
