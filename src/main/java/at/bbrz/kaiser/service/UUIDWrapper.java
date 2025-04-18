package at.bbrz.kaiser.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDWrapper {
    public String createUUID(){
        return UUID.randomUUID().toString();
    }
}
