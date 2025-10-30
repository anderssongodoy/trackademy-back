# Trackademy API Reference

Base: `${BACKEND_URL}/api`
Auth: `Authorization: Bearer <access_token Microsoft Entra ID>`
Content-Type: `application/json`
Zona horaria: America/Lima

Nota: Todos los endpoints (salvo health) requieren JWT v�lido.

## Tipos comunes (resumen)

- CarreraDto: `{ id: number, nombre: string }`
- CursoDto: `{ id: number, codigo: string, nombre: string }`
- TemaDto: `{ id: number, titulo: string }`
- UnidadDto: `{ id: number, numero: number, titulo: string, temas: TemaDto[] }`
- EvaluacionDto: `{ id: number, codigo: string|null, descripcion: string|null, semana: number|null, porcentaje: number }`
- ResultadoAprendizajeDto: `{ id: number, texto: string, tipo?: string|null, unidadId?: number|null }`
- NotaPoliticaDto: `{ seccion?: string|null, texto?: string|null }`
- CursoDetailDto: `{ id, codigo, nombre, horasSemanales?, silaboDescripcion?, resultadosAprendizaje[], unidades[], evaluaciones[], bibliografia[], competencias[], politicas[] }`
- UsuarioEvaluacionDto: `{ id, codigo, descripcion, semana, porcentaje, fechaEstimada?, nota? }`
- UsuarioCursoResumenDto: `{ cursoId, cursoNombre, evaluaciones: UsuarioEvaluacionDto[] }`
- OnboardingRequest: `{ campusId: number, periodoId: number, carreraId: number, cursoIds: number[] }`

---

## Salud

GET `/health`
- Objetivo: Verificar que el servicio est� vivo.
- Auth: no
- 200: `{ "status": "ok" }`

---

## Usuario

GET `/me`
- Objetivo: Inspeccionar claims del token recibido por el backend.
- Auth: s�
- 200 ejemplo:
```
{
  "sub": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
  "issuer": "https://login.microsoftonline.com/<tenant>/v2.0",
  "aud": ["<clientId>"],
  "claims": { "name": "John Doe", "preferred_username": "john@org.com" }
}
```

GET `/me/status`
- Objetivo: Decidir flujo post-login. Crea/actualiza Usuario si no existe y reporta si falta onboarding.
- Auth: s�
- Headers opcionales: `X-User-Image` o `X-User-Avatar` para persistir avatar (base64 o URL)
- 200 ejemplo:
```
{
  "needsOnboarding": true,
  "missing": ["perfil", "campus", "periodo", "carrera", "cursos"],
  "usuarioId": 42,
  "subject": "RuuAw...",
  "email": "u24209904@utp.edu.pe",
  "campusId": null,
  "periodoId": null,
  "carreraId": null,
  "cursosCount": 0
}
```

GET `/me/cursos`
- Objetivo: Listar cursos activos del usuario con evaluaciones clonadas.
- Auth: s�
- 200 ejemplo:
```
[
  {
    "cursoId": 123,
    "cursoNombre": "C�lculo I",
    "evaluaciones": [
      {"id": 1, "codigo": "PC1", "descripcion": null, "semana": 4, "porcentaje": 15.0, "fechaEstimada": "2025-04-01", "nota": null}
    ]
  }
]
```

GET `/me/evaluaciones`
- Objetivo: Pr�ximas evaluaciones (ventana 3 semanas).
- Auth: s�
- 200 ejemplo:
```
[
  {"id": 10, "codigo": "EP", "descripcion": "Examen Parcial", "semana": 8, "porcentaje": 30.0, "fechaEstimada": "2025-05-06", "nota": null}
]
```

POST `/me/evaluaciones/{id}/nota`
- Objetivo: Registrar/actualizar nota y recalcular resumen del curso.
- Auth: s�
- Body: `{ "nota": "15" }`
- 204 sin contenido

POST `/me/preferencias/recordatorios`
- Objetivo: Crear/actualizar preferencias globales de recordatorios.
- Auth: s�
- Body: `{ "anticipacionDias": 3 }`
- 204

POST `/me/horario`
- Objetivo: Definir/actualizar bloques de estudio por curso (45 min cada bloque).
- Auth: s�
- Body:
```
[
  {"usuarioCursoId": 55, "diaSemana": 2, "horaInicio": "08:00", "duracionMin": 90},
  {"usuarioCursoId": 55, "diaSemana": 4, "horaInicio": "10:30", "duracionMin": 45}
]
```
- 204

POST `/me/habitos`
- Objetivo: Crear h�bito del usuario.
- Auth: s�
- Body: `{ "titulo": "Leer 30min", "frecuencia": "diaria" }`
- 200: `123` (id creado)

POST `/me/habitos/{id}/log`
- Objetivo: Marcar cumplimiento del h�bito en una fecha espec�fica.
- Auth: s�
- Body: `{ "fecha": "2025-04-10" }` (opcional; default hoy)
- 204

GET `/me/recomendaciones`
- Objetivo: Listar recomendaciones activas.
- Auth: s�
- 200: `string[]`

POST `/me/avatar`
- Objetivo: Actualizar avatar del usuario (base64 o URL).
- Auth: s�
- Body: `{ "image": "data:image/jpeg;base64,..." }`
- 204

---

## Cat�logo

GET `/catalog/campus?universidadId={id}`
- Objetivo: Listar campus por universidad.
- Auth: s�
- 200: `[ {"id": 3, "nombre": "Lima Centro"}, ... ]`

GET `/catalog/periodos?universidadId={id}`
- Objetivo: Listar periodos por universidad.
- Auth: s�
- 200: `[ {"id": 1, "etiqueta": "2025-1", "fechaInicio": "2025-03-04", "fechaFin": "2025-07-20"}, ... ]`

GET `/catalog/carreras?universidadId={id}`
- Objetivo: Listar carreras por universidad.
- Auth: s�
- 200: `[ {"id": 1, "nombre": "Ingenier�a de Sistemas"}, ... ]`

GET `/catalog/cursos?carreraId={id}`
- Objetivo: Listar cursos por carrera.
- Auth: s�
- 200: `[ {"id": 100, "codigo": "MAT101", "nombre": "Matem�tica I"}, ... ]`

GET `/catalog/curso/{id}`
- Objetivo: Detalle completo del curso (s�labo/unidades/evaluaciones/bibliograf�a/competencias/pol�ticas).
- Auth: s�
- 200 ejemplo (parcial):
```
{
  "id": 100,
  "codigo": "MAT101",
  "nombre": "Matem�tica I",
  "horasSemanales": 4,
  "silaboDescripcion": "Introducci�n al c�lculo...",
  "resultadosAprendizaje": [ {"id":1, "texto": "Desarrolla...", "tipo": "general", "unidadId": null} ],
  "unidades": [ {"id": 1, "numero": 1, "titulo": "L�mites", "temas": [ {"id": 11, "titulo": "Definici�n de l�mite"} ]} ],
  "evaluaciones": [ {"id": 9, "codigo": "PC1", "descripcion": null, "semana": 4, "porcentaje": 15.0} ],
  "bibliografia": ["Stewart, C�lculo"],
  "competencias": ["Pensamiento cr�tico"],
  "politicas": [ {"seccion": "C�lculo de nota", "texto": "Se promedian..."} ]
}
```

---

## Onboarding

POST `/onboarding`
- Objetivo: Completar onboarding del usuario: guarda perfil (campus/periodo/carrera), crea relaci�n con cursos y clona evaluaciones.
- Auth: s�
- Headers opcionales: `X-User-Image` o `X-User-Avatar` para guardar avatar si a�n no existe.
- Body:
```
{ "campusId": 1, "periodoId": 2, "carreraId": 3, "cursoIds": [100,101] }
```
- 200: `UsuarioCursoResumenDto[]` (misma forma que `/me/cursos`)

---

## Admin (opcional)

POST `/admin/ingesta/json`
- Objetivo: Ingestar JSON de s�labo/cursos (idempotente) � proteger con API key / roles en prod.
- Auth: s�
- Body: JSON del extractor
- 202: `{ "status": "accepted" }`

---

## Errores est�ndar

- 401 Unauthorized: token ausente / inv�lido (issuer/audience)
- 403 Forbidden: recurso de otro usuario
- 404 Not Found: id inexistente
- 409 Conflict: violaci�n de clave �nica (ingesta/admin)
- 400 Bad Request: validaci�n de body/params

Notas:
- CORS: definir or�genes en `trackademy.cors.allowed-origins` (agregar `https://trackademy.trinitylabs.app`).
- OPTIONS (preflight) es gestionado autom�ticamente.
