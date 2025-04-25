package at.bbrz.kaiser.service;

import java.util.UUID;

public class UUIDWrapper {
    public String createUUID(){
        return UUID.randomUUID().toString();
    }
}
