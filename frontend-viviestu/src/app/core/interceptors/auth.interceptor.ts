import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { UsuarioService } from '../services/usuario.service';  // Se creará en el Paso 4

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Inyectamos el servicio que maneja el estado del token
  const usuarioService = inject(UsuarioService);
  const token = usuarioService.token();

  // Si existe un token, lo adjuntamos a los headers de la solicitud
  if (token) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(authReq);
  }

  // Si no hay token, la solicitud continúa sin el header de autenticación
  return next(req);
};