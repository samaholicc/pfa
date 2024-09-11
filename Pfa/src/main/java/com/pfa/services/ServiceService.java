package com.pfa.services;

import com.pfa.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<com.pfa.models.Service> getAllServices() {
        return serviceRepository.findAll();
    }
}
