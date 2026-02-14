// Modelo para los datos de la tabla 'preferencias'
export interface Preference {
    idPreferencia?: number; // Opcional, solo existe si ya fue guardada
    presupuesto: number;
    seguridad: string;
    tiempoMax: number;
    transporte: string;
    universidad: string;
    idUsuario: number; // Requerido para el endpoint
}