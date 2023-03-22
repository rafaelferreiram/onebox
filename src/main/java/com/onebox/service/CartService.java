package com.onebox.service;

import java.util.List;

import com.onebox.dto.CartRequestDTO;
import com.onebox.model.Cart;

public interface CartService {
	
	public Cart createCart(List<CartRequestDTO> cartRequest);
	
	public Cart getById(Long cartId);

	public void deleteCart(Long cartId);
	
	public Cart addProductsToCart(List<CartRequestDTO> cartRequest, Long CartId);
}
