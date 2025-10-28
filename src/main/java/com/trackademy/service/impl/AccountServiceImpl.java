package com.trackademy.service.impl;

import com.trackademy.domain.entity.Usuario;
import com.trackademy.domain.entity.UsuarioPerfil;
import com.trackademy.domain.repo.UsuarioCursoRepository;
import com.trackademy.domain.repo.UsuarioPerfilRepository;
import com.trackademy.domain.repo.UsuarioRepository;
import com.trackademy.dto.LoginStatusDto;
import com.trackademy.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioPerfilRepository usuarioPerfilRepository;
    private final UsuarioCursoRepository usuarioCursoRepository;

    @Override
    @Transactional
    public LoginStatusDto getLoginStatus(String subject, String email, String nombre, String avatar) {
        Usuario usuario = usuarioRepository.findBySubject(subject)
                .orElseGet(() -> usuarioRepository.save(Usuario.builder()
                        .subject(subject)
                        .email(StringUtils.defaultString(email))
                        .nombre(StringUtils.defaultString(nombre))
                        .avatar(StringUtils.abbreviate(StringUtils.defaultString(avatar), 1_000_000))
                        .build()));

        boolean changed = false;
        if (email != null && !StringUtils.equals(usuario.getEmail(), email)) { usuario.setEmail(email); changed = true; }
        if (nombre != null && !StringUtils.equals(usuario.getNombre(), nombre)) { usuario.setNombre(nombre); changed = true; }
        if (avatar != null && !StringUtils.equals(usuario.getAvatar(), avatar)) { usuario.setAvatar(StringUtils.abbreviate(avatar, 1_000_000)); changed = true; }
        if (changed) usuarioRepository.save(usuario);

        var missing = new ArrayList<String>();
        Long campusId = null, periodoId = null, carreraId = null;

        var perfilOpt = usuarioPerfilRepository.findByUsuarioId(usuario.getId());
        if (perfilOpt.isEmpty()) {
            missing.add("perfil");
        } else {
            UsuarioPerfil p = perfilOpt.get();
            if (p.getCampus() == null) missing.add("campus"); else campusId = p.getCampus().getId();
            if (p.getPeriodo() == null) missing.add("periodo"); else periodoId = p.getPeriodo().getId();
            if (p.getCarrera() == null) missing.add("carrera"); else carreraId = p.getCarrera().getId();
        }

        int cursosCount = usuarioCursoRepository.findByUsuarioId(usuario.getId()).size();
        if (cursosCount == 0) missing.add("cursos");

        boolean needs = !missing.isEmpty();
        return new LoginStatusDto(needs, missing, usuario.getId(), usuario.getSubject(), usuario.getEmail(),
                campusId, periodoId, carreraId, cursosCount);
    }
}
