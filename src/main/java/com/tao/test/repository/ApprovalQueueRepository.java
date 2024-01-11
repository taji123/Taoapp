package com.tao.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tao.test.entity.ApprovalQueue;

public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Integer> {

	Optional<ApprovalQueue> findByQueueIdAndStatus(Integer approvalQueueId, String approvalStatus);

}
