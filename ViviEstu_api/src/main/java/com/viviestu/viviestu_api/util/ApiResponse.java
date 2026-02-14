package com.viviestu.viviestu_api.util;

/// Clase simple para respuestas uniformes
public record ApiResponse<T>(int status, String mensaje, T data) {}
