package com.tao.test.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tao.test.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	 
	 List<Product> findByProductNameContainingAndPriceBetweenAndProductDateBetween(
	            String productName,
	            double minPrice, double maxPrice,
	            LocalDate minProductDate, LocalDate maxProductDate
	    );
	 
	 List<Product> findByApprovalQueueIsNotNullOrderByProductDateAsc();
	 
}
