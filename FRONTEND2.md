# Trackademy Frontend (Next.js + NextAuth)

Guía para integrar el frontend con el backend protegido por Microsoft Entra ID (JWT) y consumir los endpoints definidos. Incluye setup de NextAuth, manejo del access token y ejemplos de llamadas al backend.

## Stack Recomendado

- Next.js 14+ (App Router) con TypeScript
- NextAuth (provider Microsoft Entra ID)
- React Query o fetch nativo (a elección)
- Tailwind u otro framework de estilos (opcional)

## Variables de Entorno

En el frontend (.env.local):

- `NEXTAUTH_URL` = http://localhost:3000
- `NEXTAUTH_SECRET` = <cadena aleatoria segura>
- `AUTH_MICROSOFT_ENTRA_ID_ID` = <Client ID de la app (o del API si usas recurso dedicado)>
- `AUTH_MICROSOFT_ENTRA_ID_SECRET` = <Client Secret>
- `AUTH_MICROSOFT_ENTRA_ID_TENANT_ID` = <Tenant ID> (opcional; si no se define, usa `common`)
- `BACKEND_URL` = http://localhost:8080

Notas:
- Si en el backend se valida `aud` (propiedad `trackademy.security.microsoft.audience`), asegúrate de que corresponda con el `Client ID` del token que emite NextAuth (ver sección “Audiencia”).
- En despliegue, agrega los orígenes del frontend en `trackademy.cors.allowed-origins` del backend.

## NextAuth: Microsoft Entra ID

Archivo sugerido `src/auth.config.ts` (o `auth.ts` según tu estructura):

```ts
import type { NextAuthConfig } from "next-auth";
import MicrosoftEntraID from "next-auth/providers/microsoft-entra-id";

const tenantId = process.env.AUTH_MICROSOFT_ENTRA_ID_TENANT_ID;

export const authConfig: NextAuthConfig = {
  providers: [
    MicrosoftEntraID({
      clientId: process.env.AUTH_MICROSOFT_ENTRA_ID_ID!,
      clientSecret: process.env.AUTH_MICROSOFT_ENTRA_ID_SECRET!,
      issuer: tenantId
        ? `https://login.microsoftonline.com/${tenantId}/v2.0`
        : "https://login.microsoftonline.com/common/v2.0",
      authorization: {
        params: {
          // Solicita scopes OIDC + acceso a tu API si corresponde
          // Si tu backend valida aud contra el Client ID del API, usa el .default del recurso API
          scope: `openid profile email offline_access api://${process.env.AUTH_MICROSOFT_ENTRA_ID_ID}/.default`,
        },
      },
    }),
  ],
  session: { strategy: "jwt" },
  callbacks: {
    async jwt({ token, account }) {
      // Persistir access_token de Microsoft en el token JWT de NextAuth
      if (account) {
        token.access_token = account.access_token;
        token.id_token = account.id_token;
        token.expires_at = account.expires_at;
      }
      return token;
    },
    async session({ session, token }) {
      // Exponer el access_token en session para usar desde el cliente/servidor
      (session as any).access_token = token.access_token;
      (session as any).expires_at = token.expires_at;
      return session;
    },
  },
};
```

Handler (App Router), por ejemplo en `src/app/api/auth/[...nextauth]/route.ts`:

```ts
import NextAuth from "next-auth";
import { authConfig } from "@/auth.config";

const handler = NextAuth(authConfig);
export { handler as GET, handler as POST };
```

## Audiencia (aud) del Token

- El backend puede validar `aud` contra `trackademy.security.microsoft.audience`.
- Opciones:
  - Modo simple: deja vacía `trackademy.security.microsoft.audience` (se omite validación de audiencia en dev).
  - Modo recomendado: fija `trackademy.security.microsoft.audience` al Client ID del recurso que emite el token (puede ser la misma app del frontend o un App Registration específico para el API). Ajusta el `scope` de autorización para solicitar token para ese recurso (ej.: `api://<CLIENT_ID_API>/.default`).

## Cliente HTTP hacia el Backend

Helper de servidor `src/lib/api.ts` que reenvía el access token en el header `Authorization`:

```ts
import { getServerSession } from "next-auth";
import { authConfig } from "@/auth.config";

const BASE_URL = process.env.BACKEND_URL!;

export async function apiFetch(path: string, init: RequestInit = {}) {
  const session = await getServerSession(authConfig);
  const headers: HeadersInit = {
    ...(init.headers || {}),
    Authorization: session && (session as any).access_token
      ? `Bearer ${(session as any).access_token}`
      : "",
    "Content-Type": "application/json",
  };
  const res = await fetch(`${BASE_URL}${path}`, { ...init, headers, cache: "no-store" });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`Backend ${res.status}: ${text}`);
  }
  return res.json();
}
```

Uso en Server Components / Server Actions:

```ts
import { apiFetch } from "@/lib/api";

export async function getMe() {
  return apiFetch("/api/me");
}
```

## Rutas Clave a Consumir

- Catálogo
  - `GET /api/catalog/carreras?universidadId=...`
  - `GET /api/catalog/cursos?carreraId=...`
  - `GET /api/catalog/curso/{id}` → incluye: silabo, resultados de aprendizaje, unidades/temas, evaluaciones, bibliografía, competencias, política de notas
- Onboarding
  - `POST /api/onboarding` body: `{ campusId, periodoId, carreraId, cursoIds: number[] }`
- Usuario (requiere token)
  - `GET /api/me` → claims y contexto
  - `GET /api/me/cursos` → resumen por curso con evaluaciones clonadas
  - `GET /api/me/evaluaciones` → próximas 3 semanas
  - `POST /api/me/evaluaciones/{id}/nota` body: `{ nota: string }`
  - `POST /api/me/preferencias/recordatorios` body: `{ anticipacionDias: number }`
  - `POST /api/me/horario` body: `[{ usuarioCursoId, diaSemana, horaInicio, duracionMin }]`
  - `POST /api/me/habitos` body: `{ nombre, periodicidad? }`
  - `POST /api/me/habitos/{id}/log` body: `{ fecha? }`
  - `GET /api/me/recomendaciones`

## Protección de Páginas

- App Router: protege layouts/segmentos leyendo sesión en el servidor.

```ts
// src/app/(protected)/layout.tsx
import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";
import { authConfig } from "@/auth.config";

export default async function ProtectedLayout({ children }: { children: React.ReactNode }) {
  const session = await getServerSession(authConfig);
  if (!session) redirect("/login");
  return <>{children}</>;
}
```

- Alternativa: middleware para forzar login en rutas específicas.

```ts
// src/middleware.ts
import { withAuth } from "next-auth/middleware";

export default withAuth({});
export const config = { matcher: ["/dashboard/:path*", "/me/:path*"] };
```

## Ejemplo de UI (esqueleto)

- Listar carreras/cursos, seleccionar y hacer `POST /api/onboarding`.
- Página `Me` con bloques:
  - Cursos activos (`GET /api/me/cursos`)
  - Próximas evaluaciones (`GET /api/me/evaluaciones`)
  - Registrar nota por evaluación (`POST /api/me/evaluaciones/{id}/nota`)
  - Preferencias de recordatorios y horario
  - Hábitos y recomendaciones

## CORS / Dominios

- Backend permite orígenes configurados en `trackademy.cors.allowed-origins`.
- En desarrollo: `http://localhost:3000` ya está habilitado por defecto.
- En producción: agrega Vercel/Render u otros dominios al property del backend.

## Desarrollo Local

1) Levanta backend: `./mvnw spring-boot:run` (puerto 8080)
2) Levanta frontend: `pnpm dev`/`npm run dev`/`yarn dev` (puerto 3000)
3) Inicia sesión por NextAuth (Microsoft). Verifica que en el `session` exista `access_token`.
4) Navega a una página protegida y consume `/api/me` para validar el token contra el backend.

## Errores Comunes

- 401 del backend: token ausente o inválido (issuer/audience). Revisa `AUTH_MICROSOFT_ENTRA_ID_TENANT_ID` y el scope del provider.
- CORS bloqueado: agrega el origen del frontend en `trackademy.cors.allowed-origins` del backend.
- `aud` inválido: alinea `trackademy.security.microsoft.audience` con el recurso que emite el token. En dev, puedes dejarlo vacío (no se valida `aud`).

---

Con esto, el frontend queda listo para autenticar con Microsoft Entra ID, obtener un access token y consumir el backend protegido. Si quieres, puedo agregar un esqueleto Next.js (rutas, provider y componentes) directamente en otro repo o en una carpeta `front/` para arrancar más rápido.
