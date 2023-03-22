package com.onebox.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.onebox.constants.ErrorCodes;
import com.onebox.dto.CartRequestDTO;
import com.onebox.exceptions.DeleteException;
import com.onebox.exceptions.NotFoundException;
import com.onebox.mapper.CartMapper;
import com.onebox.model.Cart;
import com.onebox.repository.CartRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;

	private final CartMapper mapper;

	public CartServiceImpl(CartRepository cartRepository, CartMapper mapper) {
		super();
		this.cartRepository = cartRepository;
		this.mapper = mapper;
	}

	@Override
	public Cart createCart(List<CartRequestDTO> cartRequest) {
		log.debug("CartServiceImpl.createCart - Create cart {} ", cartRequest);
		Cart model = mapper.toCartModel(cartRequest);
		log.debug("CartServiceImpl.createCart - Cart created {} ", model);
		return cartRepository.save(model);
	}

	@Override
	public Cart addProductsToCart(List<CartRequestDTO> cartRequest, Long CartId) {
		Cart cart = getById(CartId);
		cart.setProducts(mapper.toProducts(cartRequest));
		cartRepository.save(cart);
		return getById(CartId); 
	}

	@Override
	public Cart getById(Long cartId) {
		return cartRepository.findById(cartId)
				.orElseThrow(() -> new NotFoundException(ErrorCodes.NOT_FOUND ,String.format("Cart not found for ID [%s]", cartId)));
	}

	@Override
	public void deleteCart(Long cartId) {
		try {
			Cart cart = getById(cartId);
			cartRepository.delete(cart);
		} catch (Exception e) {
			throw new DeleteException(String.format("Error while deleting cart [%s]", cartId));
		}
	}
}
