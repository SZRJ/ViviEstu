// Modelo para los datos de la tabla 'zonas' en PostgreSQL
export interface Zona {
    idZona: number;
    nombre: string;
    precioPromedio: number;
    recomendado: boolean;
    seguridad: string; // ALTA, MEDIA, BAJA
    transporteDisponible: string; // Ej: METRO_BUS, TAXI_APP
    
    // Propiedad opcional para mostrar los comentarios, aunque no la carguemos ahora
    comentarios?: any[];
    // Agrega estos opcionales para el frontend:
    esFavorito?: boolean; 
    promedioCalificacion?: number;
}