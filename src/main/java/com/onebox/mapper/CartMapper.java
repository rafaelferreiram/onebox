package com.onebox.mapper;

import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import com.onebox.dto.CartRequestDTO;
import com.onebox.model.Cart;
import com.onebox.model.Product;

@Mapper(componentModel="spring")
public interface CartMapper {

	public Cart toCartModel(List<CartRequestDTO> cartRequest);
	
	public Set<Product> toProducts(List<CartRequestDTO> cartRequest);
}
