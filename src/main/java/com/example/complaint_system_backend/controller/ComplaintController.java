package com.example.complaint_system_backend.controller;


import com.example.complaint_system_backend.dto.UpdateComplaintRequest;
import com.example.complaint_system_backend.dto.ComplaintStatsDTO;
import com.example.complaint_system_backend.model.Complaint;
import com.example.complaint_system_backend.email.EmailService;
import com.example.complaint_system_backend.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api") // All URLs in this class will start with /api
@CrossOrigin(origins = "http://localhost:5173") // Allows requests from your React app
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private EmailService emailService;

    // This method handles POST requests to http://localhost:8080/api/complaints
    @PostMapping("/complaints")
    public Complaint submitComplaint(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    // --- ADD THE NEW ENDPOINTS BELOW ---

    // Endpoint to get all complaints (for admin)
    // Handles GET requests to http://localhost:8080/api/complaints
    @GetMapping("/complaints")
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    // Endpoint to get a single complaint by its ID (for tracking)
    // Handles GET requests to http://localhost:8080/api/complaints/1 (or any ID)
    @GetMapping("/complaints/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintById(id)
                .map(ResponseEntity::ok) // If found, return 200 OK with the complaint
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    @PatchMapping("/complaints/{id}")
    public ResponseEntity<Complaint> updateComplaint(@PathVariable Long id, @RequestBody UpdateComplaintRequest request) {
        try {
            return complaintService.updateComplaintStatus(id, request.getStatus(), request.getRemarks())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // Return a 400 error for invalid status change
        }
    }

    @GetMapping("/complaints/track/{ticketId}")
    public ResponseEntity<Complaint> getComplaintByTicketId(@PathVariable String ticketId) {
        return complaintService.getComplaintByTicketId(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/complaints/{id}/notify")
    public ResponseEntity<Void> notifyUser(@PathVariable Long id) {
        Optional<Complaint> complaintOptional = complaintService.getComplaintById(id);

        if (complaintOptional.isPresent()) {
            emailService.sendNotificationEmail(complaintOptional.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/stats")
    public ComplaintStatsDTO getStats() {
        return complaintService.getComplaintStats();
    }
}