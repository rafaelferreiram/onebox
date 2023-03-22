package com.onebox.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.onebox.dto.CartRequestDTO;
import com.onebox.model.Cart;
import com.onebox.model.Product;

public class Mapper implements CartMapper {

	public Mapper() {
	}

	@Override
	public Cart toCartModel(List<CartRequestDTO> cartRequest) {
		Set<Product> products = new HashSet<>();
		cartRequest.stream().forEach(cartItem -> {
			products.add(Product.builder().id(cartItem.getProductId()).description(cartItem.getDescription())
					.amount(cartItem.getAmount()).build());
		});

		return Cart.builder().products(products).build();
	}

	@Override
	public Set<Product> toProducts(List<CartRequestDTO> cartRequest) {
		Set<Product> products = new HashSet<>();
		cartRequest.stream().forEach(cartItem -> {
			products.add(Product.builder().id(cartItem.getProductId()).description(cartItem.getDescription())
					.amount(cartItem.getAmount()).build());
		});

		return products;
	}

}
