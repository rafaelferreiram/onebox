package com.onebox.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.onebox.constants.ErrorCodes;
import com.onebox.dto.CartRequestDTO;
import com.onebox.exceptions.DeleteException;
import com.onebox.exceptions.NotFoundException;
import com.onebox.mapper.Mapper;
import com.onebox.model.Cart;
import com.onebox.model.Product;
import com.onebox.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

	@Mock
	private CartRepository cartRepository;

	@InjectMocks
	private CartServiceImpl service;

	@BeforeEach
	public void initService() {
		service = new CartServiceImpl(cartRepository, new Mapper());
	}

	// Create Cart
	@Test
	public void givenValidRequestBodyWhenCreateCartThenReturnCreatedCart() {
		// Arrange
		Set<Product> products = new HashSet<Product>();
		products.add(Product.builder().id(1L).amount(1L).description("Product Test").build());
		Cart expected = Cart.builder().id(1L).products(products).build();

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());
		// Act
		when(cartRepository.save(any(Cart.class))).thenReturn(expected);

		Cart result = service.createCart(cartRequest);

		// Assert
		assertEquals(expected, result);
	}

	@Test
	public void givenValidRequestBodyWithouProductsWhenCreateCartThenReturnCreatedCart() {
		// Arrange
		Cart expected = Cart.builder().id(1L).products(new HashSet<Product>()).build();

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		// Act
		when(cartRepository.save(any(Cart.class))).thenReturn(expected);

		Cart result = service.createCart(cartRequest);

		// Assert
		assertEquals(expected, result);
	}

	// Add Products to Cart
	@Test
	public void givenValidRequestBodyWhenAddProductsToCartThenReturnUpdatedCart() {
		// Arrange
		Cart oldCart = Cart.builder().id(1L).products(new HashSet<Product>()).build();
		Set<Product> products = new HashSet<Product>();
		products.add(Product.builder().id(1L).amount(1L).description("Product Test").build());
		products.add(Product.builder().id(2L).amount(1L).description("Product Test 2").build());
		Cart expected = Cart.builder().id(1L).products(products).build();

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());
		cartRequest.add(CartRequestDTO.builder().productId(2L).amount(1L).description("Product Test 2").build());
		// Act
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(oldCart));
		when(cartRepository.save(any(Cart.class))).thenReturn(expected);

		Cart result = service.addProductsToCart(cartRequest, 1L);

		// Assert
		assertEquals(expected, result);
	}

	@Test
	public void givenInvalidRequestCartIdWhenAddProductsToCartThenReturnNotFoundException() {
		// Arrange
		String expectedMsg = String.format("Cart not found for ID [%s]", 1L);

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());
		cartRequest.add(CartRequestDTO.builder().productId(2L).amount(1L).description("Product Test 2").build());
		// Act
		when(cartRepository.findById(anyLong())).thenThrow(new NotFoundException(ErrorCodes.NOT_FOUND, expectedMsg));

		Exception exception = assertThrows(NotFoundException.class, () -> {
			service.addProductsToCart(cartRequest, 1L);
		});

		String actualMessage = exception.getMessage();

		// Then
		assertTrue(actualMessage.contains(expectedMsg));

	}

	// Get cart by Id
	@Test
	public void givenValidCartIdWhenGetCartThenReturnCart() {
		// Arrange
		Set<Product> products = new HashSet<Product>();
		products.add(Product.builder().id(1L).amount(1L).description("Product Test").build());
		Cart expected = Cart.builder().id(1L).products(products).build();

		// Act
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(expected));

		Cart result = service.getById(1L);

		// Assert
		assertEquals(expected, result);
	}

	@Test
	public void givenInvalidRequestCartIdWhenGetCartThenReturnNotFoundException() {
		// Arrange
		String expectedMsg = String.format("Cart not found for ID [%s]", 1L);

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());

		// Act
		when(cartRepository.findById(anyLong())).thenThrow(new NotFoundException(ErrorCodes.NOT_FOUND, expectedMsg));

		Exception exception = assertThrows(NotFoundException.class, () -> {
			service.getById(1L);
		});

		String actualMessage = exception.getMessage();

		// Then
		assertTrue(actualMessage.contains(expectedMsg));

	}

	// Delete cart
	@Test
	public void givenValidCartIdWhenDeleteCartThenReturnDeleted() {
		// Arrange
		Set<Product> products = new HashSet<Product>();
		products.add(Product.builder().id(1L).amount(1L).description("Product Test").build());
		Cart expected = Cart.builder().id(1L).products(products).build();

		// Act
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(expected));

		service.deleteCart(1L);

		// Assert
		verify(cartRepository).delete(expected);
	}

	@Test
	public void givenInvalidRequestCartIdWhenDeleteCartThenReturnDeleteException() {
		// Arrange
		String expectedMsg = String.format("Error while deleting cart [%s]", 1l);

		List<CartRequestDTO> cartRequest = new ArrayList<>();
		cartRequest.add(CartRequestDTO.builder().productId(1L).amount(1L).description("Product Test").build());

		// Act
		when(cartRepository.findById(anyLong())).thenThrow(new NotFoundException(ErrorCodes.NOT_FOUND, expectedMsg));

		Exception exception = assertThrows(DeleteException.class, () -> {
			service.deleteCart(1L);
		});

		String actualMessage = exception.getMessage();

		// Then
		assertTrue(actualMessage.contains(expectedMsg));

	}

}
