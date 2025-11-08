package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Controller
public class AttendanceController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/attendance")
    public String attendanceForm() {
        return "attendance";
    }
    
    @PostMapping("/attendance")
    public String submitAttendance(@RequestParam String studentId,
                                  @RequestParam String studentName,
                                  @RequestParam String date,
                                  @RequestParam String status,
                                  Model model) {
        try (Connection conn = dataSource.getConnection()) {
            String query = "INSERT INTO Attendance (StudentID, StudentName, AttendanceDate, Status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, studentId);
            ps.setString(2, studentName);
            ps.setString(3, date);
            ps.setString(4, status);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                model.addAttribute("success", true);
                model.addAttribute("studentId", studentId);
                model.addAttribute("studentName", studentName);
                model.addAttribute("date", date);
                model.addAttribute("status", status);
            }
            ps.close();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "attendanceResult";
    }
}
