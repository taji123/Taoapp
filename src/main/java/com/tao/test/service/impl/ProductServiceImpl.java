package com.tao.test.service.impl;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tao.test.entity.ApprovalQueue;
import com.tao.test.entity.Product;
import com.tao.test.exception.CustomException;
import com.tao.test.repository.ApprovalQueueRepository;
import com.tao.test.repository.ProductRepository;
import com.tao.test.servi.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ApprovalQueueRepository approvalQueueRepo;

	@Override
	public List<Product> getActiveProducts() {
		try {
		List<Product> productList = productRepository.findAll();
		List<Product> activeProducts = null;
		if(productList != null) {
	    activeProducts = productList.stream().filter(Product::getIsActive)
				.sorted((p1, p2) -> p2.getProductDate().compareTo(p1.getProductDate())).collect(Collectors.toList());
		}
		return activeProducts;
		} catch (IllegalAccessError e) {
			throw new IllegalAccessError("There is no different dates to sort");
		}

	}

	@Override
	public List<Product> searchProducts(String productName, String minPrice, String maxPrice, String minProductDate,
			String maxProductDate) {
		try {
			return productRepository.findByProductNameContainingAndPriceBetweenAndProductDateBetween(
					productName != null ? productName : "", minPrice != null ? Double.valueOf(minPrice) : 0,
					maxPrice != null ? Double.valueOf(maxPrice) : Double.MAX_VALUE,
					minProductDate != null ? LocalDate.parse(minProductDate) : LocalDate.MIN,
					maxProductDate != null ? LocalDate.parse(maxProductDate) : LocalDate.MAX);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Please Enter Proper Price Value");
		} catch (DateTimeParseException e) {
			throw new DateTimeException("Please Enter Date in yyyy-MM-dd Format");     
		}
	}

	@Override
	public Product createProduct(Product product) {
		double maxPriceForApproval = 5000.0;
		double maxPrice = 10000.0;
		product.setProductDate(LocalDate.now());

		if (product.getPrice() > maxPrice) {
			throw new IllegalArgumentException("Product price cannot exceed $10,000.");
		}

		if (product.getPrice() > maxPriceForApproval) {
			ApprovalQueue approvalQueue = new ApprovalQueue();
			approvalQueue.setStatus("Pending");
			approvalQueue.setProduct(product);
			approvalQueue.setRequestDate(LocalDate.now());
			product.setProductStatus("Pending");
			product.setApprovalQueue(approvalQueue);

			return productRepository.save(product);
		} else {
			product.setProductStatus("Approved");
			return productRepository.save(product);
		}
	}

	@Override
	public Product updateProduct(Long productId, Product updatedProduct) {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		double maxPriceIncreasePercentage = 0.5;
		existingProduct.setProductDate(LocalDate.now());

		if (updatedProduct.getPrice() > existingProduct.getPrice() * (1 + maxPriceIncreasePercentage)) {
			ApprovalQueue approvalQueue = new ApprovalQueue();
			approvalQueue.setStatus("Pending");
			approvalQueue.setProduct(existingProduct);
			approvalQueue.setRequestDate(LocalDate.now());
			existingProduct.setProductStatus("Pending");
			existingProduct.setApprovalQueue(approvalQueue);
		} else {
			existingProduct.setProductName(updatedProduct.getProductName());
			existingProduct.setPrice(updatedProduct.getPrice());
			existingProduct.setProductStatus(updatedProduct.getProductStatus());
			existingProduct.setApprovalQueue(null);
		}

		return productRepository.save(existingProduct);
	}

	@Override
	public void deleteProduct(Long productId) {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
	
		ApprovalQueue approvalQueue = new ApprovalQueue();
		approvalQueue.setStatus("Deleted ");       
		approvalQueue.setProduct(null);
		approvalQueue.setRequestDate(LocalDate.now());
		approvalQueue.setDeletedProductId(String.valueOf(existingProduct.getProductId()));
        existingProduct.setApprovalQueue(null);

		productRepository.delete(existingProduct); 
		
		approvalQueueRepo.save(approvalQueue);
	}
	
	@Override
	public List<Product> getProductsInApprovalQueue() {
        return productRepository.findByApprovalQueueIsNotNullOrderByProductDateAsc();
    }
	
	@Override
	public void approveProduct(Integer approvalId) {
        Optional<ApprovalQueue> approvalQueueOptional = approvalQueueRepo.findByQueueIdAndStatus(approvalId, "Pending");

        if (approvalQueueOptional.isPresent()) {
            ApprovalQueue approvalQueue = approvalQueueOptional.get();
            Product product = approvalQueue.getProduct();

            product.setProductStatus("approved");

            product.setApprovalQueue(null);

             productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product not found in the approval queue with ID: " + approvalId);
        }
    }
	
	@Override
	public Product rejectProduct(Integer approvalId) {
        Optional<ApprovalQueue> approvalQueueOptional = approvalQueueRepo.findByQueueIdAndStatus(approvalId, "Pending");

        if (approvalQueueOptional.isPresent()) {
            ApprovalQueue approvalQueue = approvalQueueOptional.get();
            Product product = approvalQueue.getProduct();

            product.setProductStatus("Rejected");

            product.setApprovalQueue(null);

             Product product2 = productRepository.save(product);
             return product2;
        } else {
            throw new IllegalArgumentException("Product not found in the approval queue with ID: " + approvalId);
        }
    }
	
}