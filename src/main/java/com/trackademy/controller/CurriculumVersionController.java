package com.trackademy.controller;

import com.trackademy.dto.CurriculumVersionDTO;
import com.trackademy.service.CurriculumVersionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum-versions")
public class CurriculumVersionController {
    
    private final CurriculumVersionService curriculumVersionService;
    
    public CurriculumVersionController(CurriculumVersionService curriculumVersionService) {
        this.curriculumVersionService = curriculumVersionService;
    }
    
    @PostMapping
    public ResponseEntity<CurriculumVersionDTO> createCurriculumVersion(@RequestBody CurriculumVersionDTO curriculumVersionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(curriculumVersionService.createCurriculumVersion(curriculumVersionDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CurriculumVersionDTO> getCurriculumVersion(@PathVariable Long id) {
        return curriculumVersionService.getCurriculumVersionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<CurriculumVersionDTO>> getCurriculumVersionsByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(curriculumVersionService.getCurriculumVersionsByProgram(programId));
    }
    
    @GetMapping("/program/{programId}/version/{versionNumber}")
    public ResponseEntity<CurriculumVersionDTO> getCurriculumVersionByProgramAndVersion(
            @PathVariable Long programId, 
            @PathVariable Integer versionNumber) {
        return curriculumVersionService.getCurriculumVersionByProgramAndVersion(programId, versionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CurriculumVersionDTO> updateCurriculumVersion(@PathVariable Long id, @RequestBody CurriculumVersionDTO curriculumVersionDTO) {
        return ResponseEntity.ok(curriculumVersionService.updateCurriculumVersion(id, curriculumVersionDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculumVersion(@PathVariable Long id) {
        curriculumVersionService.deleteCurriculumVersion(id);
        return ResponseEntity.noContent().build();
    }
}
