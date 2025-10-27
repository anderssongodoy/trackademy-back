# Differences: solo pendientes tras la última actualización de Entities.md

A continuación, únicamente los puntos que aún difieren y deben ajustarse para alinearse al 100% con el esquema objetivo.

1) unidad.numero vs. unidad.nro (!)
- Entities: campo `numero` (int) con Unique(curso_id, numero).
- Esquema/Extractores: `nro`.
- Acción: renombrar `numero` → `nro` (o confirmar convención y avisar para adaptar extractores si prefieres `numero`).

2) bibliografia (normalización opcional)
- Entities: un solo campo `referencia` (text).
- Esquema: `tipo`, `autores`, `titulo`, `editorial`, `anio`, `url`.
- Acción: mantener `referencia` es válido; si buscas mayor granularidad en UI, considera normalizar a los campos del esquema.

3) competencia: nullables
- Entities: `universidad_id` y `tipo` pueden ser null.
- Esquema: se asume `universidad_id` not null y `tipo` requerido ('general'|'especifica') para filtros/unicidad.
- Acción: marcar `universidad_id` y `tipo` como not null, y garantizar `unique(universidad_id, tipo, nombre)`.

4) usuario_perfil: nullables
- Entities: `campus_id` (visible), `periodo_id` y `carrera_id` posiblemente null.
- Esquema: onboarding completo → no null.
- Acción: si el flujo exige completar, marcar not null; si permites omitir, valida en servicio para no dejar perfiles parciales en producción.

5) evaluacion.flexible null vs. default false
- Entities: `flexible` puede ser null.
- Esquema: `flexible boolean not null default false`.
- Acción: establecer default false y not null.

6) usuario.created_at null
- Entities: `created_at` null.
- Esquema: default now().
- Acción: establecer `created_at` con default now() not null.

7) agenda_evento: FKs adicionales
- Entities: mantiene `curso_id` y `usuario_evaluacion_id` además de `fuente`/`ref_id`.
- Esquema: se recomienda usar `fuente`+`ref_id` para flexibilidad.
- Acción: opcional; si mantienes las FKs adicionales, que `fuente/ref_id` sea la referencia primaria en la lógica.
