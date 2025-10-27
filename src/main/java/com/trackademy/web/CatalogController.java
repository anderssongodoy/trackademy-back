package com.trackademy.web;

import com.trackademy.dto.CarreraDto;
import com.trackademy.dto.CursoDetailDto;
import com.trackademy.dto.CursoDto;
import com.trackademy.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    // Nota: por simplicidad, pedimos universidadId por query; 
    // también podríamos derivarlo de config si es fija.
    @GetMapping("/carreras")
    public ResponseEntity<List<CarreraDto>> listCarreras(@RequestParam("universidadId") Long universidadId) {
        return ResponseEntity.ok(catalogService.listarCarreras(universidadId));
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDto>> listCursos(@RequestParam("carreraId") Long carreraId) {
        return ResponseEntity.ok(catalogService.listarCursosPorCarrera(carreraId));
    }

    @GetMapping("/curso/{id}")
    public ResponseEntity<CursoDetailDto> getCurso(@PathVariable("id") Long id) {
        return ResponseEntity.ok(catalogService.obtenerCursoDetalle(id));
    }
}
