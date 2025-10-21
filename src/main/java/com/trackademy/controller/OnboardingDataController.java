package com.trackademy.controller;

import com.trackademy.dto.CampusDTO;
import com.trackademy.dto.CycleDto;
import com.trackademy.dto.ProgramDTO;
import com.trackademy.service.CampusService;
import com.trackademy.service.TermService;
import com.trackademy.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OnboardingDataController {

    @Autowired
    private CampusService campusService;

    @Autowired
    private TermService termService;

    @Autowired
    private ProgramService programService;

    @GetMapping("/campuses")
    public ResponseEntity<List<CampusDTO>> getCampuses() {
        List<CampusDTO> campuses = campusService.getAllCampusesForOnboarding();
        return ResponseEntity.ok(campuses);
    }

    @GetMapping("/cycles")
    public ResponseEntity<List<CycleDto>> getCycles() {
        List<CycleDto> cycles = termService.getAvailableCycles();
        return ResponseEntity.ok(cycles);
    }

    @GetMapping("/programs")
    public ResponseEntity<List<ProgramDTO>> getPrograms() {
        List<ProgramDTO> programs = programService.getAllPrograms();
        return ResponseEntity.ok(programs);
    }
}
