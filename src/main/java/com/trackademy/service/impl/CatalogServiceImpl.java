package com.trackademy.service.impl;

import com.trackademy.domain.entity.*;
import com.trackademy.domain.repo.*;
import com.trackademy.dto.*;
import com.trackademy.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private static final Logger log = LoggerFactory.getLogger(CatalogServiceImpl.class);

    private final CarreraRepository carreraRepository;
    private final CursoRepository cursoRepository;
    private final UnidadRepository unidadRepository;
    private final TemaRepository temaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final BibliografiaRepository bibliografiaRepository;
    private final NotaPoliticaRepository notaPoliticaRepository;
    private final SilaboRepository silaboRepository;
    private final CursoCompetenciaRepository cursoCompetenciaRepository;
    private final ResultadoAprendizajeRepository resultadoAprendizajeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CarreraDto> listarCarreras(Long universidadId) {
        log.debug("Listando carreras para universidad {}", universidadId);
        return carreraRepository.findByUniversidadIdOrderByNombreAsc(universidadId).stream()
                .map(c -> new CarreraDto(c.getId(), c.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDto> listarCursosPorCarrera(Long carreraId) {
        log.debug("Listando cursos por carrera {}", carreraId);
        return cursoRepository.findByCarreraId(carreraId).stream()
                .map(c -> new CursoDto(c.getId(), StringUtils.defaultString(c.getCodigo()), c.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CursoDetailDto obtenerCursoDetalle(Long cursoId) {
        log.debug("Obteniendo detalle de curso {}", cursoId);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        var unidades = unidadRepository.findByCursoIdOrderByNroAsc(cursoId).stream()
                .map(u -> new UnidadDto(u.getId(), u.getNro(), u.getTitulo(),
                        temaRepository.findByUnidadIdOrderByIdAsc(u.getId()).stream()
                                .map(t -> new TemaDto(t.getId(), t.getTitulo()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        var evals = evaluacionRepository.findByCursoIdOrderBySemanaAsc(cursoId).stream()
                .map(e -> new EvaluacionDto(e.getId(), e.getCodigo(), e.getDescripcion(), e.getSemana(), e.getPorcentaje()))
                .collect(Collectors.toList());

        var biblio = bibliografiaRepository.findByCursoId(cursoId).stream()
                .map(b -> b.getReferencia())
                .collect(Collectors.toList());

        var politicas = notaPoliticaRepository.findByCursoId(cursoId)
                .stream()
                .map(np -> new com.trackademy.dto.NotaPoliticaDto(np.getSeccion(), np.getTexto()))
                .collect(Collectors.toList());

        var silabo = silaboRepository.findFirstByCursoIdAndVigenteTrueOrderByIdDesc(cursoId)
                .map(Silabo::getSumilla)
                .orElse(null);

        var competencias = cursoCompetenciaRepository.findByCursoId(cursoId).stream()
                .map(cc -> cc.getCompetencia().getNombre())
                .sorted()
                .collect(Collectors.toList());

        var resultados = resultadoAprendizajeRepository.findByCursoId(cursoId).stream()
                .map(ra -> new ResultadoAprendizajeDto(
                        ra.getId(), ra.getTexto(), ra.getTipo(), ra.getUnidad() != null ? ra.getUnidad().getId() : null
                ))
                .collect(Collectors.toList());

        return new CursoDetailDto(curso.getId(), curso.getCodigo(), curso.getNombre(), curso.getHorasSemanales(),
                silabo, resultados, unidades, evals, biblio, competencias, politicas);
    }
}
