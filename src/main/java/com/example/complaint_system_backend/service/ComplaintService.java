package com.example.complaint_system_backend.service;


import com.example.complaint_system_backend.model.Complaint;
import com.example.complaint_system_backend.dto.ComplaintStatsDTO;
import com.example.complaint_system_backend.repository.ComplaintRepository;
import com.example.complaint_system_backend.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.Optional;


@Service
public class ComplaintService {

    @Autowired // Spring automatically provides an instance of the repository here
    private ComplaintRepository complaintRepository;

    @Autowired
    private EmailService emailService;

    public Complaint createComplaint(Complaint complaint) {
        // Generate a random 8-digit number for the user-facing ticket ID
        long randomId = ThreadLocalRandom.current().nextLong(10000000, 100000000);
        complaint.setTicketId(String.valueOf(randomId));

        // Set default status and time (existing logic)
        complaint.setStatus("Submitted");
        complaint.setSubmittedAt(LocalDateTime.now());

        return complaintRepository.save(complaint);
    }

    // Method to get all complaints for the admin dashboard
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // Method to get a single complaint for the tracking page
    public Optional<Complaint> getComplaintById(Long id) {
        return complaintRepository.findById(id);
    }


    public Optional<Complaint> updateComplaintStatus(Long id, String newStatus, String remarks) {
        Optional<Complaint> complaintOptional = complaintRepository.findById(id);

        if (complaintOptional.isPresent()) {
            Complaint complaint = complaintOptional.get();
            String currentStatus = complaint.getStatus();

            boolean isValidChange = (currentStatus.equals("Submitted") && newStatus.equals("In Review")) ||
                                  (currentStatus.equals("In Review") && newStatus.equals("Resolved"));
            
            if (isValidChange) {
                complaint.setStatus(newStatus);
                complaint.setAdminRemarks(remarks);

                // Save the complaint
                Complaint savedComplaint = complaintRepository.save(complaint);

                // 3. CALL THE EMAIL SERVICE AFTER SAVING
                emailService.sendNotificationEmail(savedComplaint);

                return Optional.of(savedComplaint);
            } else {
                throw new IllegalStateException("Invalid status change from " + currentStatus + " to " + newStatus);
            }
        } else {
            return Optional.empty();
        }
    }


    public ComplaintStatsDTO getComplaintStats() {
        long submitted = complaintRepository.countByStatus("Submitted");
        long inReview = complaintRepository.countByStatus("In Review");
        long resolved = complaintRepository.countByStatus("Resolved");

        return new ComplaintStatsDTO(submitted, inReview, resolved);
    }
    

    public Optional<Complaint> getComplaintByTicketId(String ticketId) {
        return complaintRepository.findByTicketId(ticketId);
    }

    
}


