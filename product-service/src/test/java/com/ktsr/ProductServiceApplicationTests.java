package com.ktsr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsr.DTO.ProductRequest;
import com.ktsr.DTO.ProductResponse;
import com.ktsr.model.Product;
import com.ktsr.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@Container
	static MongoDBContainer mongoDBContainer=new MongoDBContainer("mongo:4.4.2");

//	DockerImageName myImage = DockerImageName.parse("product:4.4.2")
//			.asCompatibleSubstituteFor("mongo");
//
//	MongoDBContainer mongoDBContainer = new MongoDBContainer(myImage);

	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {

		productRepository.deleteAll();


		ProductRequest productRequest= getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("IPhone 13 Pro")
				.description("Iphone 13")
				.price(90000)
				.build();
	}


	@Test
	void shouldGetAllProducts() throws Exception {

		productRepository.deleteAll();

		ProductRequest product1=ProductRequest.builder()
				.name("IPhone 13 Pro")
				.description("Iphone 13")
				.price(90000)
				.build();

		ProductRequest product2= ProductRequest.builder()
				.name("Samsung Galaxy S22")
				.description("Flagship Samsung phone")
				.price(85000)
				.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(product1)))
				.andExpect(status().isCreated());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(product2)))
				.andExpect(status().isCreated());


		String response=mockMvc.perform(MockMvcRequestBuilders.get("/api/product/getAll"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		ProductResponse[] productResponses= objectMapper.readValue(response,ProductResponse[].class);

		Assertions.assertEquals(2,productResponses.length);
		Assertions.assertEquals("IPhone 13 Pro", productResponses[0].getName());
		Assertions.assertEquals("Samsung Galaxy S22",productResponses[1].getName());
	}


}
