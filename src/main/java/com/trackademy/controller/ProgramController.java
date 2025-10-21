package com.trackademy.controller;

import com.trackademy.dto.ProgramDTO;
import com.trackademy.service.ProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
    
    private final ProgramService programService;
    
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }
    
    @PostMapping
    public ResponseEntity<ProgramDTO> createProgram(@RequestBody ProgramDTO programDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programService.createProgram(programDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProgramDTO> getProgram(@PathVariable Long id) {
        return programService.getProgramById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<ProgramDTO> getProgramBySlug(@PathVariable String slug) {
        return programService.getProgramBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<ProgramDTO>> getProgramsByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(programService.getProgramsByInstitution(institutionId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProgramDTO> updateProgram(@PathVariable Long id, @RequestBody ProgramDTO programDTO) {
        return ResponseEntity.ok(programService.updateProgram(id, programDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
