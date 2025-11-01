// src/main/java/com/example/complaint_system_backend/dto/UpdateComplaintRequest.java
package com.example.complaint_system_backend.dto;

public class UpdateComplaintRequest {
    private String status;
    private String remarks;

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}