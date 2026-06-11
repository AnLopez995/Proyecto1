package com.proyecto1.customerservice;

import com.proyecto1.customerservice.infrastructure.messaging.ClienteEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceApplicationTests {

	@MockBean
	private ClienteEventPublisher clienteEventPublisher;

	@Test
	void contextLoads() {
	}
}