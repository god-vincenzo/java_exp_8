package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/employees")
    public String getEmployees(@RequestParam(required = false) Integer empId, Model model) {
        try (Connection conn = dataSource.getConnection()) {
            if (empId != null) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Employee WHERE EmpID = ?");
                ps.setInt(1, empId);
                ResultSet rs = ps.executeQuery();
                
                List<Map<String, Object>> employees = new ArrayList<>();
                if (rs.next()) {
                    Map<String, Object> emp = new HashMap<>();
                    emp.put("empId", rs.getInt("EmpID"));
                    emp.put("name", rs.getString("Name"));
                    emp.put("salary", rs.getDouble("Salary"));
                    employees.add(emp);
                }
                model.addAttribute("employees", employees);
                rs.close();
                ps.close();
            } else {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Employee");
                ResultSet rs = ps.executeQuery();
                
                List<Map<String, Object>> employees = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> emp = new HashMap<>();
                    emp.put("empId", rs.getInt("EmpID"));
                    emp.put("name", rs.getString("Name"));
                    emp.put("salary", rs.getDouble("Salary"));
                    employees.add(emp);
                }
                model.addAttribute("employees", employees);
                rs.close();
                ps.close();
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "employees";
    }
}
