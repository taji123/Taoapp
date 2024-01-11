package com.tao.test.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ApprovalQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "queue_id")
	private Integer queueId;
	@Column(name = "status")
	private String status;
	private String deletedProductId;
    private LocalDate requestDate;



	@OneToOne
	@JoinColumn(name = "product_Id")
	@JsonBackReference
	private Product product;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public String getDeletedProductId() {
		return deletedProductId;
	}

	public void setDeletedProductId(String deletedProductId) {
		this.deletedProductId = deletedProductId;
	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}
	
}
