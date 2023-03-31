package br.bieniek.com.websocket.providers;

import java.security.PublicKey;

public interface KeyProvider {
    PublicKey getPublicKey(String keyId);
}
