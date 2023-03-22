package com.onebox.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebox.constants.ErrorCodes;
import com.onebox.dto.CartRequestDTO;
import com.onebox.dto.ExpectedExceptionResponse;
import com.onebox.exceptions.NotFoundException;
import com.onebox.model.Cart;
import com.onebox.model.Product;
import com.onebox.service.CartService;

@WebMvcTest(value = CartController.class)
public class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService service;

	@Autowired
	ObjectMapper mapper;

	// Create cart
	@Test
	void givenExistantIdWhenCreateCartByIdThenReturnResponsetWithStatus200() throws Exception {
		// Arrange
		Set<Product> products = new HashSet<Product>();
		products.add(Product.builder().id(1L).amount(1L).description("Product Test").build());
		Cart expected = Cart.builder().id(1L).products(products).build();

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());

		// Act
		when(service.createCart(cartRequest)).thenReturn(expected);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/cart")
				.accept(MediaType.APPLICATION_JSON)
				.content(mapToJson(cartRequest))
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult resultRequest = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse requestResponse = resultRequest.getResponse();

		Cart result = mapper.readValue(requestResponse.getContentAsString(), Cart.class);

		// Assert
		assertEquals(HttpStatus.OK.value(), requestResponse.getStatus());
		assertEquals(expected, result);
	}
	
	@Test
	void givenInvalidIdWhenCreateCartByIdThenReturnInternalServerErrorException() throws Exception {
		// Act
		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());

		when(service.createCart(cartRequest)).thenThrow(
				new RuntimeException(String.format("Error while creating cart ")));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/cart")
				.accept(MediaType.APPLICATION_JSON).content(mapToJson(cartRequest))
				.contentType(MediaType.APPLICATION_JSON);

		ExpectedExceptionResponse result = mapper.readValue(mockMvc.perform(requestBuilder)
				.andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsByteArray(),
				ExpectedExceptionResponse.class);

		ExpectedExceptionResponse expected = ExpectedExceptionResponse.builder().code(ErrorCodes.INTERNAL_SERVER_ERROR.name())
				.message("Internal server error").details(List.of("Error while creating cart ")).build();

		// Assert
		assertEquals(expected, result);
		verify(service).createCart(cartRequest);
	}

	// Add products to cart
	@Test
	void givenExistantIdWhenAddProductsToCartByIdThenReturnResponsetWithStatus200() throws Exception {
		// Arrange
		Set<Product> products = new HashSet<Product>();
		products.add(Product.builder().id(1L).amount(1L).description("Product Test").build());
		Cart expected = Cart.builder().id(1L).products(products).build();

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());
		// Act
		when(service.addProductsToCart(cartRequest, 1L)).thenReturn(expected);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/cart/{cartId}/products", 1)
				.accept(MediaType.APPLICATION_JSON).content(mapToJson(cartRequest))
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult resultRequest = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse requestResponse = resultRequest.getResponse();

		Cart result = mapper.readValue(requestResponse.getContentAsString(), Cart.class);

		// Assert
		assertEquals(HttpStatus.OK.value(), requestResponse.getStatus());
		assertEquals(expected, result);
	}

	@Test
	void givenInvalidIdWhenAddProductsToCartByIdThenReturnNotFoundException() throws Exception {
		// Act
		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());

		when(service.addProductsToCart(cartRequest, 1L)).thenThrow(
				new NotFoundException(ErrorCodes.NOT_FOUND, String.format("Cart not found for ID [%s]", 1L)));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/cart/{cartId}/products", 1)
				.accept(MediaType.APPLICATION_JSON).content(mapToJson(cartRequest))
				.contentType(MediaType.APPLICATION_JSON);

		ExpectedExceptionResponse result = mapper.readValue(mockMvc.perform(requestBuilder)
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsByteArray(),
				ExpectedExceptionResponse.class);

		ExpectedExceptionResponse expected = ExpectedExceptionResponse.builder().code(ErrorCodes.NOT_FOUND.name())
				.message("Not found").details(List.of("Cart not found for ID [1]")).build();

		// Assert
		assertEquals(expected, result);
		verify(service).addProductsToCart(cartRequest, 1L);
	}

	// Get Cart
	@Test
	void givenExistantIdWhenGetCartByIdThenReturnResponsetWithStatus200() throws Exception {
		// Arrange
		Cart expected = Cart.builder().id(1L).products(new HashSet<>()).build();

		// Act
		when(service.getById(1L)).thenReturn(expected);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/cart/{cartId}", 1)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

		MvcResult resultRequest = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse requestResponse = resultRequest.getResponse();

		Cart result = mapper.readValue(requestResponse.getContentAsString(), Cart.class);

		// Assert
		assertEquals(HttpStatus.OK.value(), requestResponse.getStatus());
		assertEquals(expected, result);
	}

	@Test
	void givenInvalidIdWhenGetCartByIdThenReturnNotFoundException() throws Exception {
		// Act
		when(service.getById(1L)).thenThrow(
				new NotFoundException(ErrorCodes.NOT_FOUND, String.format("Cart not found for ID [%s]", 1L)));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/cart/{cartId}", 1)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

		ExpectedExceptionResponse result = mapper.readValue(mockMvc.perform(requestBuilder)
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsByteArray(),
				ExpectedExceptionResponse.class);

		ExpectedExceptionResponse expected = ExpectedExceptionResponse.builder().code(ErrorCodes.NOT_FOUND.name())
				.message("Not found").details(List.of("Cart not found for ID [1]")).build();

		// Assert
		assertEquals(expected, result);
		verify(service).getById(1L);
	}

	// Delete cart
	@Test
	void givenExistantIdWhenDeleteCartByIdThenReturnResponsetWithStatus200() throws Exception {
		// Arrange
		String msg = "Cart Deleted";

		// Act
		doNothing().when(service).deleteCart(1L);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/cart/{cartId}", 1)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

		MvcResult resultRequest = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse requestResponse = resultRequest.getResponse();

		String result = requestResponse.getContentAsString();

		// Assert
		assertEquals(HttpStatus.OK.value(), requestResponse.getStatus());
		assertEquals(msg, result);
	}

	@Test
	void givenInvalidIdWhenDeleteCartByIdThenReturnNotFoundException() throws Exception {
		// Act
		doThrow(new NotFoundException(ErrorCodes.NOT_FOUND, String.format("Cart not found for ID [%s]", 1L)))
				.when(service).deleteCart(1L);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/cart/{cartId}", 1)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

		ExpectedExceptionResponse result = mapper.readValue(mockMvc.perform(requestBuilder)
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsByteArray(),
				ExpectedExceptionResponse.class);

		ExpectedExceptionResponse expected = ExpectedExceptionResponse.builder().code(ErrorCodes.NOT_FOUND.name())
				.message("Not found").details(List.of("Cart not found for ID [1]")).build();

		// Assert
		assertEquals(expected, result);
		verify(service).deleteCart(1L);
	}

	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
}
