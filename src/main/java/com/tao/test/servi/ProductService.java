package com.tao.test.servi;

import java.util.List;

import com.tao.test.entity.Product;

public interface ProductService {

	List<Product> getActiveProducts();

	List<Product> searchProducts(String productName, String minPrice, String maxPrice, String minProductDate,
			String maxProductDate);

	Product createProduct(Product product);

	Product updateProduct(Long productId, Product updatedProduct);
	
    void deleteProduct(Long productId);
    
    List<Product> getProductsInApprovalQueue();
    
    void approveProduct(Integer approvalId);
    
    Product rejectProduct(Integer approvalId);





}
