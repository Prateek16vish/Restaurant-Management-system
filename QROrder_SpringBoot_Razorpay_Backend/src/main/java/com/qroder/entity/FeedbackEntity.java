package com.qroder.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="customer_feedback")
public class FeedbackEntity{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id; String token; Integer rating;
 @Column(length=1000) String feedback; LocalDateTime createdAt;
 public Long getId(){return id;} public String getToken(){return token;} public void setToken(String v){token=v;}
 public Integer getRating(){return rating;} public void setRating(Integer v){rating=v;} public String getFeedback(){return feedback;} public void setFeedback(String v){feedback=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}