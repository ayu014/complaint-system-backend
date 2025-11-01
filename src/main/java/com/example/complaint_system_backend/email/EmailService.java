// src/main/java/com/example/complaintsystembackend/email/EmailService.java
package com.example.complaint_system_backend.email;

import com.example.complaint_system_backend.model.Complaint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${resend.api.key}")
    private String resendApiKey;

    private final String RESEND_API_URL = "https://api.resend.com/emails";

    public void sendNotificationEmail(Complaint complaint) {
        // 1. Check if the user provided an email
        if (complaint.getUserEmail() == null || complaint.getUserEmail().isEmpty()) {
            System.out.println("No email found for complaint " + complaint.getTicketId() + ", skipping notification.");
            return;
        }

        // 2. Build the email subject and body
        String subject = "Update on your complaint: " + complaint.getTicketId();
        String bodyHtml = String.format(
            "<h1>Complaint Status Update</h1>" +
            "<p>Hello,</p>" +
            "<p>This is a notification regarding your complaint (<strong>%s</strong>).</p>" +
            "<p><strong>Title:</strong> %s</p>" +
            "<p><strong>New Status:</strong> %s</p>" +
            "<p><strong>Admin Remarks:</strong> %s</p>" +
            "<p>Thank you for your submission.</p>",
            complaint.getTicketId(),
            complaint.getTitle(),
            complaint.getStatus(),
            complaint.getAdminRemarks()
        );

        // 3. Set HTTP Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + resendApiKey);

        // 4. Create the JSON payload for Resend's API
        Map<String, Object> requestBody = Map.of(
            "from", "ACRS <onboarding@resend.dev>",
            "to", List.of(complaint.getUserEmail()), // Resend expects 'to' to be an array
            "subject", subject,
            "html", bodyHtml
        );

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 5. Send the email
            String response = restTemplate.postForObject(RESEND_API_URL, httpEntity, String.class);
            System.out.println("Email sent successfully! Response: " + response);
        } catch (RestClientException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}