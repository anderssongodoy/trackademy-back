package com.trackademy.controller;

import com.trackademy.dto.TermDTO;
import com.trackademy.service.TermService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
public class TermController {
    
    private final TermService termService;
    
    public TermController(TermService termService) {
        this.termService = termService;
    }
    
    @PostMapping
    public ResponseEntity<TermDTO> createTerm(@RequestBody TermDTO termDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(termService.createTerm(termDTO));
    }

    @GetMapping
    public ResponseEntity<List<TermDTO>> getAllTerms() {
        return ResponseEntity.ok(termService.getAllTerms());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> getTerm(@PathVariable Long id) {
        return termService.getTermById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-code/{code}")
    public ResponseEntity<TermDTO> getTermByCode(@PathVariable String code) {
        return termService.getTermByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<TermDTO>> getTermsByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(termService.getTermsByInstitution(institutionId));
    }
    
    @GetMapping("/year/{year}")
    public ResponseEntity<List<TermDTO>> getTermsByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(termService.getTermsByYear(year));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TermDTO> updateTerm(@PathVariable Long id, @RequestBody TermDTO termDTO) {
        return ResponseEntity.ok(termService.updateTerm(id, termDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        termService.deleteTerm(id);
        return ResponseEntity.noContent().build();
    }
}
