package com.tao.test.controller;

import java.time.DateTimeException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tao.test.entity.Product;
import com.tao.test.servi.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/api/products")
	public ResponseEntity<List<Product>> getActiveProduct() {
		try {
		List<Product> activeProducts = productService.getActiveProducts();
		return new ResponseEntity<>(activeProducts, HttpStatus.OK);
		} catch (IllegalAccessError e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/api/products/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String productName,
			@RequestParam(required = false) String minPrice, @RequestParam(required = false) String maxPrice,
			@RequestParam(required = false) String minPostedDate,
			@RequestParam(required = false) String maxPostedDate) {
		try {
			List<Product> list = productService.searchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
		return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}catch (DateTimeException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		}

	@PostMapping("/api/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		try {
			Product createdProduct = productService.createProduct(product);
			return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/api/products/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
		try {
			Product updated = productService.updateProduct(productId, updatedProduct);
			return new ResponseEntity<>(updated, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/api/products/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
		try {
			productService.deleteProduct(productId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/api/products/approval-queue")
    public ResponseEntity<List<Product>> getProductsInApprovalQueue() {
		try {
        List<Product> products = productService.getProductsInApprovalQueue();
        return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PutMapping("/api/products/{approvalId}/approve")
    public ResponseEntity<Void> approveProduct(@PathVariable String approvalId) {
        try {
            productService.approveProduct(Integer.valueOf(approvalId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
	
	@PutMapping("/api/products/approval-queue/{approvalId}/reject")
    public ResponseEntity<Product> rejectProduct(@PathVariable String approvalId) {
        try {
            productService.rejectProduct(Integer.valueOf(approvalId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
