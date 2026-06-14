package com.AcademicScope.controller.dashboard;

import com.AcademicScope.service.dashboard.DashboardService;
import com.AcademicScope.dto.DashboardRendimientoDTO;
import com.AcademicScope.dto.TutorDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/rendimiento")
    public ResponseEntity<DashboardRendimientoDTO> getRendimiento() {
        return ResponseEntity.ok(dashboardService.obtenerRendimiento());
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<TutorDashboardDTO> getDashboardTutor(@PathVariable Long tutorId) {
        return ResponseEntity.ok(dashboardService.obtenerDashboardTutor(tutorId));
    }
}
