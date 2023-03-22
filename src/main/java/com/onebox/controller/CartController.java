package com.onebox.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onebox.dto.CartRequestDTO;
import com.onebox.model.Cart;
import com.onebox.service.CartService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1")
public class CartController {
	
	private final CartService cartService;
	
	public CartController(CartService cartService) {
		super();
		this.cartService = cartService;
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createCart(@RequestBody final List<CartRequestDTO> cartDTO) {

		log.debug("CartController.createCart - Create cart {} ", cartDTO);
		Cart cartSaved = cartService.createCart(cartDTO);
		log.debug("CartController.createCart - Wishlist saved successfully {}",cartSaved);

		return ResponseEntity.ok(cartSaved);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/cart/{cartId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity addProductsToCart(@RequestBody final List<CartRequestDTO> cartDTO, @PathVariable("cartId") Long cartId) {

		log.debug("CartController.addProductsToCart - Add products to cart {} ", cartDTO);
		Cart cart = cartService.addProductsToCart(cartDTO, cartId);
		log.debug("CartController.addProductsToCart - Prodcuts added successfully to cart {}",cart);

		return ResponseEntity.ok(cart);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value = "/cart/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getCart(@PathVariable("cartId") Long cartId) {

		log.debug("CartController.getCart - Get cart by id {} ", cartId);
		Cart cart = cartService.getById(cartId);
		log.debug("CartController.getCart - Cart retrieved successfully {}", cart);

		return ResponseEntity.ok(cart);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping(value = "/cart/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity deleteCart(@PathVariable("cartId") Long cartId) {

		log.debug("CartController.deleteCart - Delete cart {} ", cartId);
		cartService.deleteCart(cartId);
		log.debug("CartController.deleteCart - Cart deleted successfully {}");

		return ResponseEntity.ok("Cart Deleted");
	}


}
