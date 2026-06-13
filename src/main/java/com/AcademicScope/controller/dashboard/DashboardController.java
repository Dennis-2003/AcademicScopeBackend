package com.AcademicScope.controller.dashboard;

import com.AcademicScope.service.dashboard.DashboardService;
import com.AcademicScope.dto.DashboardRendimientoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
