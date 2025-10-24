package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZonaService {
    @Autowired
    private ZonaRepository zonaRep;

    public Zona listId(int idZona) {
        return zonaRep.findById(idZona).orElse(null);
    }
    public void edit(Zona z) {
        zonaRep.save(z);
    }
}
