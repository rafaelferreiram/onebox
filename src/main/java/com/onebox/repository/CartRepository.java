package com.onebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.onebox.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	@Query(value = "SELECT * FROM cart c WHERE c.updated_at < CURRENT_TIMESTAMP - NUMTODSINTERVAL(10, 'MINUTE')" , nativeQuery = true)
	List<Cart> findAllIncativeForMoreThan10minutes();

}
