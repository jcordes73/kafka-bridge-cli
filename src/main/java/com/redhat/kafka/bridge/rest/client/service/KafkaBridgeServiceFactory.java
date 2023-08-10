package com.redhat.kafka.bridge.rest.client.service;

import java.net.URI;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@ApplicationScoped
public class KafkaBridgeServiceFactory {

    @Inject @ConfigProperty(name="kafka.bridge.url")
    Provider<String> baseUri;

    public KafkaBridgeService getKafkaBridgeService() throws Exception {
        return RestClientBuilder.newBuilder().baseUri(new URI(baseUri.get())).build(KafkaBridgeService.class);
    }
}