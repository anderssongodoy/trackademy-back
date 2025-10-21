package com.trackademy.controller;

import com.trackademy.dto.CampusDTO;
import com.trackademy.service.CampusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campus")
public class CampusController {
    
    private final CampusService campusService;
    
    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }
    
    @PostMapping
    public ResponseEntity<CampusDTO> createCampus(@RequestBody CampusDTO campusDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campusService.createCampus(campusDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CampusDTO> getCampus(@PathVariable Long id) {
        return campusService.getCampusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-name/{name}")
    public ResponseEntity<CampusDTO> getCampusByName(@PathVariable String name) {
        return campusService.getCampusByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<CampusDTO>> getAllCampus() {
        return ResponseEntity.ok(campusService.getAllCampus());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CampusDTO> updateCampus(@PathVariable Long id, @RequestBody CampusDTO campusDTO) {
        return ResponseEntity.ok(campusService.updateCampus(id, campusDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampus(@PathVariable Long id) {
        campusService.deleteCampus(id);
        return ResponseEntity.noContent().build();
    }
}
