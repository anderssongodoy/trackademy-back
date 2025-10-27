# Data Model: Entities, Fields, Types, Relations (31)

Nota: Tipo SQL seguido de tipo Java entre paréntesis.

## universidad
- id: bigint (Long), PK, identity, not null
- nombre: varchar(200) (String), unique, not null

## campus
- id: bigint (Long), PK, identity, not null
- universidad_id: bigint (Universidad), FK -> universidad.id, not null (ManyToOne)
- nombre: varchar(120) (String), not null
- timezone: text (String), null, default 'America/Lima'

## periodo
- id: bigint (Long), PK, identity, not null
- universidad_id: bigint (Universidad), FK -> universidad.id, not null (ManyToOne)
- etiqueta: text (String), not null
- fecha_inicio: date (LocalDate), null
- fecha_fin: date (LocalDate), null
- Unique: (universidad_id, etiqueta)

## carrera
- id: bigint (Long), PK, identity, not null
- universidad_id: bigint (Universidad), FK -> universidad.id, not null (ManyToOne)
- nombre: varchar(150) (String), not null
- Unique: (universidad_id, nombre)

## curso
- id: bigint (Long), PK, identity, not null
- universidad_id: bigint (Universidad), FK -> universidad.id, not null (ManyToOne)
- codigo: varchar(40) (String), not null
- nombre: varchar(200) (String), not null
- horas_semanales: int (Integer), null
- anio: int (Integer), null
- periodo_texto: text (String), null
- modalidad: text (String), null
- creditos: int (Integer), null
- course_key: text (String), null
- Unique: (universidad_id, codigo)

## curso_carrera
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- carrera_id: bigint (Carrera), FK -> carrera.id, not null (ManyToOne)
- Unique: (curso_id, carrera_id)

## silabo
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- version: text (String), null
- vigente: boolean (Boolean), not null, default true
- fuente_pdf: text (String), null
- hash_pdf: text (String), null
- sumilla: text (String), null
- fundamentacion: text (String), null
- metodologia: text (String), null
- logro_general: text (String), null

## unidad
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- nro: int (Integer), not null
- titulo: varchar(200) (String), not null
- semana_inicio: int (Integer), null
- semana_fin: int (Integer), null
- logro_especifico: text (String), null
- Unique: (curso_id, nro)

## tema
- id: bigint (Long), PK, identity, not null
- unidad_id: bigint (Unidad), FK -> unidad.id, not null (ManyToOne)
- titulo: varchar(200) (String), not null
- orden: int (Integer), null
- Unique: (unidad_id, orden)

## evaluacion
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- codigo: varchar(40) (String), not null
- descripcion: text (String), null
- semana: int (Integer), null
- porcentaje: numeric(6,2) (BigDecimal), null
- tipo: text (String), null
- observacion: text (String), null
- modalidad: text (String), null
- individual_grupal: text (String), null
- producto: text (String), null
- flexible: boolean (Boolean), not null, default false
- unidad_nro: int (Integer), null
- atributos_json: jsonb/text (String), null
- Unique: (curso_id, codigo)

## bibliografia
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- referencia: text (String), not null

## competencia
- id: bigint (Long), PK, identity, not null
- universidad_id: bigint (Universidad), FK -> universidad.id, not null (ManyToOne)
- nombre: varchar(200) (String), not null
- tipo: text (String), not null
- Unique: (universidad_id, tipo, nombre)

## curso_competencia
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- competencia_id: bigint (Competencia), FK -> competencia.id, not null (ManyToOne)
- Unique: (curso_id, competencia_id)

## nota_politica
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- seccion: text (String), null
- texto: text (String), null

## usuario
- id: bigint (Long), PK, identity, not null
- subject: varchar(200) (String), unique, not null
- email: varchar(200) (String), null
- nombre: text (String), null
- created_at: timestamptz (OffsetDateTime), not null, default now()

## usuario_perfil
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, unique, not null (OneToOne)
- campus_id: bigint (Campus), FK -> campus.id, not null (ManyToOne)
- periodo_id: bigint (Periodo), FK -> periodo.id, not null (ManyToOne)
- carrera_id: bigint (Carrera), FK -> carrera.id, not null (ManyToOne)

## usuario_curso
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- activo: boolean (Boolean), not null, default true
- Unique: (usuario_id, curso_id)

## usuario_evaluacion
- id: bigint (Long), PK, identity, not null
- usuario_curso_id: bigint (UsuarioCurso), FK -> usuario_curso.id, not null (ManyToOne)
- evaluacion_id: bigint (Evaluacion), FK -> evaluacion.id, not null (ManyToOne)
- semana: int (Integer), null
- porcentaje: numeric(6,2) (BigDecimal), null
- fecha_estimada: date (LocalDate), null
- nota: numeric(5,2) (BigDecimal), null
- exonerado: boolean (Boolean), null
- es_rezagado: boolean (Boolean), null
- reemplaza_a_id: bigint (UsuarioEvaluacion), FK -> usuario_evaluacion.id, null (ManyToOne)
- fecha_real: date (LocalDate), null
- comentarios: text (String), null

## resultado_aprendizaje
- id: bigint (Long), PK, identity, not null
- curso_id: bigint (Curso), FK -> curso.id, not null (ManyToOne)
- unidad_id: bigint (Unidad), FK -> unidad.id, null (ManyToOne)
- texto: text (String), not null
- tipo: text (String), null

## regla_recordatorio
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- anticipacion_dias: int (Integer), not null
- activo: boolean (Boolean), not null, default true
- tipo: text (String), null
- canal: text (String), null
- Unique: (usuario_id, tipo)

## recordatorio_evento
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- tipo: varchar(50) (String), null
- titulo: varchar(200) (String), not null
- fecha_programada: timestamp (LocalDateTime), not null
- enviado: boolean (Boolean), not null, default false
- usuario_evaluacion_id: bigint (UsuarioEvaluacion), FK -> usuario_evaluacion.id, null (ManyToOne)
- agenda_evento_id: bigint (AgendaEvento), FK -> agenda_evento.id, null (ManyToOne)
- fecha_envio: timestamp (LocalDateTime), null
- canal: text (String), null
- estado: text (String), null
- payload_json: jsonb/text (String), null

## agenda_evento
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- titulo: varchar(200) (String), not null
- descripcion: text (String), null
- inicio: timestamp (LocalDateTime), not null
- fin: timestamp (LocalDateTime), null
- curso_id: bigint (Curso), FK -> curso.id, null (ManyToOne)
- usuario_evaluacion_id: bigint (UsuarioEvaluacion), FK -> usuario_evaluacion.id, null (ManyToOne)
- fuente: text (String), null
- ref_id: bigint (Long), null
- estado: text (String), null

## usuario_curso_horario
- id: bigint (Long), PK, identity, not null
- usuario_curso_id: bigint (UsuarioCurso), FK -> usuario_curso.id, not null (ManyToOne)
- dia_semana: int (Integer), not null (1=Lunes..7=Domingo)
- hora_inicio: time (LocalTime), not null
- duracion_min: int (Integer), not null
- bloque_nro: int (Integer), null
- tipo_sesion: text (String), null
- ubicacion: text (String), null
- url_virtual: text (String), null

## usuario_curso_resumen
- id: bigint (Long), PK, identity, not null
- usuario_curso_id: bigint (UsuarioCurso), FK -> usuario_curso.id, unique, not null (OneToOne)
- promedio_parcial: numeric(5,2) (BigDecimal), null
- nota_necesaria: numeric(5,2) (BigDecimal), null
- progreso_porcentaje: numeric(6,2) (BigDecimal), null
- nota_final: numeric(6,2) (BigDecimal), null
- creditos: int (Integer), null
- riesgo_aplazo_score: numeric(6,2) (BigDecimal), null
- siguiente_hito_semana: int (Integer), null
- siguiente_hito_fecha: date (LocalDate), null

## usuario_resumen
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, unique, not null (OneToOne)
- promedio_ponderado: numeric(6,2) (BigDecimal), null
- cursos_activos: int (Integer), null
- creditos_totales: int (Integer), null
- habitos_cumplidos_7d: int (Integer), null
- riesgo_global_score: numeric(6,2) (BigDecimal), null
- proximo_recordatorio_fecha: timestamptz (OffsetDateTime), null

## recomendacion
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- texto: text (String), not null
- fecha_sugerida: date (LocalDate), null
- activo: boolean (Boolean), not null, default true
- tipo: text (String), null
- prioridad: smallint (Short), null
- mensaje: text (String), null
- data_json: jsonb/text (String), null
- creada_en: timestamptz (OffsetDateTime), null
- leida_en: timestamptz (OffsetDateTime), null

## study_task
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- titulo: varchar(200) (String), not null
- descripcion: text (String), null
- fecha_sugerida: date (LocalDate), null
- completado: boolean (Boolean), not null, default false
- semana: int (Integer), null
- estado: text (String), null

## risk_event
- id: bigint (Long), PK, identity, not null
- usuario_curso_id: bigint (UsuarioCurso), FK -> usuario_curso.id, null (ManyToOne)
- usuario_id: bigint (Usuario), FK -> usuario.id, null (ManyToOne)
- curso_id: bigint (Curso), FK -> curso.id, null (ManyToOne)
- tipo: text (String), not null
- fecha: date (LocalDate), not null
- detalle: text (String), null
- activo: boolean (Boolean), not null, default true
- severidad: smallint (Short), null
- semana: int (Integer), null
- generado_en: timestamptz (OffsetDateTime), null
- data_json: jsonb/text (String), null

## usuario_habito
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- titulo: varchar(150) (String), not null
- descripcion: text (String), null
- frecuencia: text (String), null
- recordatorio_hora: time (LocalTime), null
- activo: boolean (Boolean), not null, default true

## usuario_habito_log
- id: bigint (Long), PK, identity, not null
- usuario_habito_id: bigint (UsuarioHabito), FK -> usuario_habito.id, not null (ManyToOne)
- fecha: date (LocalDate), not null
- cumplido: boolean (Boolean), not null, default false
- nota: text (String), null
- Unique: (usuario_habito_id, fecha)

## usuario_meta
- id: bigint (Long), PK, identity, not null
- usuario_id: bigint (Usuario), FK -> usuario.id, not null (ManyToOne)
- titulo: varchar(200) (String), not null
- descripcion: text (String), null
- fecha_objetivo: date (LocalDate), null
- progreso: int (Integer), null
- tipo: text (String), null
- curso_id: bigint (Curso), FK -> curso.id, null (ManyToOne)
- estado: text (String), null
