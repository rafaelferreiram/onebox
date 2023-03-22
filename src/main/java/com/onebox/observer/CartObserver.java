package com.onebox.observer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.onebox.repository.CartRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CartObserver {

	private final CartRepository repository;
	
	public CartObserver(CartRepository cartRepository) {
		super();
		this.repository = cartRepository;
	
	}
	
	@Scheduled(cron = "0 */1 * * * *")//run every minute
	public void deleteInactiveCarts() {
		log.info("CartObserver.deleteInactiveCarts - start - Deleting Inactive Carts");
		repository.findAllIncativeForMoreThan10minutes().stream().forEach(repository::delete);
		log.info("CartObserver.deleteInactiveCarts - end - Deleted Inactive Carts");
	}
	
}
