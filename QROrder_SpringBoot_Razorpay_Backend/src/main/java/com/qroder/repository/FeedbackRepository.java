package com.qroder.repository;
import com.qroder.entity.FeedbackEntity; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface FeedbackRepository extends JpaRepository<FeedbackEntity,Long>{List<FeedbackEntity> findAllByOrderByCreatedAtDesc();}