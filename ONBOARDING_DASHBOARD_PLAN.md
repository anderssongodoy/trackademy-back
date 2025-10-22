# Plan de Dashboards para apoyar al estudiante

Este documento especifica la planificación de un conjunto de dashboards y widgets destinados a facilitar la vida universitaria de los estudiantes: alertas, motivación, seguimiento de progreso, calendario académico, ranking entre pares, recomendaciones de cursos, y más. Incluye el contrato API esperado (endpoints, shapes/payloads, autenticación), eventos en tiempo real y criterios de aceptación para cada widget.

## Resumen ejecutivo
Objetivo: ofrecer al alumno una interfaz personalizada que centralice su información académica y sugiera acciones para mejorar su progreso y bienestar.
Principales dashboards:
- Resumen (Home): vista compacta con estado actual, alertas importantes y próximos pasos.
- Progreso y metas: seguimiento de créditos, aprobados, avance por programa, metas personales.
- Alertas & recordatorios: deadlines (inscripción, entregas), alertas de riesgo (bajas de rendimiento), recordatorios motivacionales.
- Calendario & horarios: integración de horario de clases y fechas importantes.
- Recomendaciones & acción: cursos sugeridos, recursos de apoyo, actividades de refuerzo.
- Social / Ranking: comparativo anónimo con pares, badges y micro-recompensas.
- Bienestar & motivación: mensajes motivacionales, streaks, y nudges personalizados.

## Origen de datos: qué ya tenemos desde el Onboarding

Al diseñar los dashboards debemos partir de la premisa: inicialmente, el único conjunto de datos que el frontend GUARDA/CONOCE sin pedir permiso adicional al backend es lo que se recopila durante el flujo de onboarding. Esos datos son la fuente mínima para poblar el primer estado del dashboard.

Datos que esperamos recibir desde el proceso de onboarding (mínimo garantizado):
- termCode / termId (término académico preferido como `termCode`)
- campusId
- programId
- lista inicial de cursos del estudiante (array de objetos con `courseCode` y/o `courseId`)

Con solo esos datos los widgets que sí pueden funcionar inmediatamente son:
- Resumen (nombre, programa, término) — mostrar lo básico del perfil
- Cursos seleccionados — mostrar chips con los cursos añadidos durante onboarding
- Recomendaciones básicas basadas en programa/term (si el backend provee un endpoint `recommendations` que acepte programId o termCode)

Widgets que necesitarán datos adicionales del backend (se pedirán después si el usuario lo autoriza o cuando el backend tenga la fuente):
- Progreso y metas: requiere historial de cursos y notas (`/api/student/progress`).
- Alertas & recordatorios: requiere calendario de fechas administrativas y reglas de negocio (`/api/student/alerts`).
- Calendario & horarios: requiere horario de clases/enrolamientos (`/api/student/calendar`).
- Social / Ranking: requiere acceso a métricas agregadas de pares (`/api/student/social/ranking`).
- Bienestar & motivación: puede requerir señales adicionales (login activity, streaks) que el backend debe calcular.

Nota: si estos endpoints no existen inicialmente, la UI debe mostrar un estado de "pendiente" con CTA para el usuario: "Solicitar datos académicos" o "Conectar servicios".

## Requerimientos opcionales que el Dashboard podría pedir al backend (después del onboarding)

Los siguientes campos NO son obligatorios en el onboarding y pueden solicitarse más tarde desde el dashboard cuando sea necesario. Cada petición debería estar protegida con `Authorization: Bearer <id_token>`.

- Historial académico detallado: lista de cursos tomados, estado, notas, créditos por término.
- Datos administrativos: fechas de matrícula, plazos, pagos.
- Horario oficial del estudiante (para calendario y detección de choques).
- Métricas agregadas para ranking (solo agregadas/pseudonimizadas).
- Logs de actividad (para calcular streaks y engagement).

Para cada dato opcional, en la UI mostrar un pequeño modal/consentimiento explicando por qué se pide y cómo se usará. Ejemplo: "Solicitamos tu historial académico para calcular tu progreso hacia la titulación. Se usará solo para mostrar un resumen privado."

## Endpoints opcionales sugeridos (cuando se habiliten)
- POST /api/student/consent - body: { scopes: ["progress","calendar","social"] } -> 200
- GET /api/student/progress -> historial académico
- GET /api/student/calendar -> horario y eventos

## Casos de fallback en frontend
- Si `/api/student/progress` devuelve 404 o 403: mostrar CTA "Conectar historial académico" y ofrecer instrucciones.
- Si no hay datos de `campusId` o `programId` desde onboarding: mostrar componentes de estado vacío y CTA para completar perfil.

## Autenticación
- Usar `Authorization: Bearer <id_token>` (id_token proveniente de Microsoft Entra / NextAuth v5) para todas las llamadas protegidas.
- Todos los endpoints devuelven 401 si el token expira o es inválido.

## Principales Endpoints (Back-end)
Notas generales:
- Base path: `/api/student` (ejemplo: `GET /api/student/me`)
- Todos los endpoints devuelven `application/json`.
- Campos de fecha en ISO 8601 (UTC) por defecto.

1) GET /api/student/me
- Propósito: información básica del estudiante y estado académico resumido.
- Req headers: Authorization
- Response example:
  {
    "id": 12345,
    "name": "María Pérez",
    "email": "maria@uni.edu",
    "campusId": "SCL",
    "programId": "CS-2024",
    "currentTerm": { "id": 20251, "code": "2025-F", "name": "2025 - Fall" },
    "creditsRequired": 180,
    "creditsApproved": 60,
    "gpa": 3.6
  }

2) GET /api/student/progress
- Propósito: estado detallado de progreso por materia y bloqueo.
- Response example:
  {
    "programId": "CS-2024",
    "terms": [ { "code": "2024-S", "credits": 18, "gpa": 3.4 }, ... ],
    "courses": [ { "courseId": 11, "code": "INF-101", "name": "Intro", "status": "APPROVED", "grade": 6.5, "credits": 4 }, ... ],
    "remainingCredits": 120
  }

3) GET /api/student/alerts
- Propósito: lista de alertas (plazo de matrícula, tareas, riesgo académico).
- Response example:
  [
    { "id": "a1", "type": "ENROLLMENT_DEADLINE", "level": "high", "title": "Último día matrícula", "message": "Matrícula cierra el 2025-09-01", "dueDate": "2025-09-01T23:59:00Z", "read": false },
    { "id": "a2", "type": "LOW_GPA_RISK", "level": "critical", "title": "Riesgo académico", "message": "Tu GPA en los últimos 2 términos es 2.1", "recommendedAction": "Solicitar tutorías", "read": false }
  ]

4) GET /api/student/calendar?start=ISO&end=ISO
- Propósito: eventos (clases, entregas, fechas admin).
- Response example:
  [ { "id": "e1", "title": "Clase INF-101", "start": "2025-08-25T10:00:00Z", "end": "2025-08-25T11:30:00Z", "type": "class", "location": "Aula 12" }, ... ]

5) GET /api/student/recommendations?limit=10
- Propósito: sugerencias personalizadas de cursos/recursos.
- Response example:
  [ { "type": "course", "courseId": 201, "code": "ALG-201", "reason": "Complementa tu plan" }, { "type": "resource", "id": "r1", "title": "Tutorías" } ]

6) GET /api/student/social/ranking?scope=campus|program&metric=gpa&limit=20
- Propósito: ranking anónimo (solo datos agregados y pseudonimizados).
- Response example:
  { "scope": "program", "metric": "gpa", "items": [ { "anonId": "u1", "gpa": 4.0, "percentile": 99 }, ... ] }

7) POST /api/student/actions
- Propósito: registrar acciones del usuario (p. ej. confirmar recomendación, marcar alerta como leída).
- Payload example:
  { "action": "MARK_ALERT_READ", "payload": { "alertId": "a1" } }
- Response: 200 + updated resource or 204

8) WS /api/student/stream (o SSE)
- Propósito: notificaciones en tiempo real (alertas críticas, cambios de horario, mensajes del staff).
- Autenticación: enviar id_token como query o por header en handshake.
- Mensajes: JSON con { "type": "ALERT_CREATED", "data": { ... } }

## Contract de datos (TS types)
- Añade un bloque con tipos TypeScript esperados (ejemplos):

```ts
export interface StudentMe {
  id: number; name: string; email: string; campusId?: string; programId?: string;
  currentTerm?: { id: number; code: string; name?: string };
  creditsRequired?: number; creditsApproved?: number; gpa?: number;
}

export interface AlertItem { id: string; type: string; level: 'low'|'medium'|'high'|'critical'; title: string; message?: string; dueDate?: string; read?: boolean; recommendedAction?: string }
```

## Eventos y triggers
- Backend debe generar alertas en los siguientes casos (ejemplos):
  - Matrícula próxima a cerrar (7 días, 48h y 2h)
  - Baja en rendimiento (2 términos consecutivos por debajo del umbral)
  - Choque de horario detectado
  - Inscripción en curso bloqueado

## Criterios de aceptación (por widget)
- Resumen: carga < 500ms (si cache local) y muestra los 3 items más importantes.
- Alertas: deben poder marcarse como leídas (POST /api/student/actions) y desaparecer en la UI.
- Calendario: eventos cargados por rango y cache por 5 minutos.
- Recomendaciones: deben incluir motivo y acción sugerida.

## Sugerencias UX rápidas
- Mostrar chips para cursos seleccionados con posibilidad de editar código.
- Barra de progreso hacia graduación visible en la parte superior del Dashboard.
- Notificaciones push / in-app para alertas críticas.

## Pasos siguientes para backend
1. Revisar y confirmar shapes/fields propuestos.
2. Implementar endpoints `/api/student/*` y un stream (SSE o WS).
3. Probar con un token real (envío desde NextAuth) y devolver 401 en caso de token inválido.
4. Enviar ejemplos de respuesta (JSON) para validar la UI.

## Ejemplos de payloads de prueba
- GET /api/student/me -> (ver más arriba)
- GET /api/student/alerts -> array de AlertItem
- POST /api/student/actions { action: 'MARK_ALERT_READ', payload: { alertId: 'a1' } }

---

Si quieres, puedo adaptar este MD a un formato más formal (OpenAPI / Swagger) o añadir ejemplo de responses más completos y mocks para pruebas front-end. ¿Lo quieres así?