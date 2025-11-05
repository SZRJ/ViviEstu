// Archivo: dto/request/CompararRequest.java
// (Para US09: Comparar zonas)
package com.viviestu.viviestu_api.dto.request;

import java.util.List;

public record CompararRequest(
        List<Integer> zonaIds
) {}