// src/main/java/com/example/complaint_system_backend/dto/ComplaintStatsDTO.java
package com.example.complaint_system_backend.dto;

public class ComplaintStatsDTO {
    private long submitted;
    private long inReview;
    private long resolved;

    // Constructor
    public ComplaintStatsDTO(long submitted, long inReview, long resolved) {
        this.submitted = submitted;
        this.inReview = inReview;
        this.resolved = resolved;
    }

    // Getters and Setters
    public long getSubmitted() { return submitted; }
    public void setSubmitted(long submitted) { this.submitted = submitted; }
    public long getInReview() { return inReview; }
    public void setInReview(long inReview) { this.inReview = inReview; }
    public long getResolved() { return resolved; }
    public void setResolved(long resolved) { this.resolved = resolved; }
}