package com.proyecto1.customerservice;

import com.proyecto1.customerservice.infrastructure.messaging.ClienteEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceApplicationTests {

	@MockitoBean
	private ClienteEventPublisher clienteEventPublisher;

	@Test
	void contextLoads() {
	}
}