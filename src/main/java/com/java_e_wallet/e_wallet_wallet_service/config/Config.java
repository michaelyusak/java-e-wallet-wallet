package com.java_e_wallet.e_wallet_wallet_service.config;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {
    @JsonProperty("kafka_address")
    private String KafkaAddress;

    @JsonProperty("kafka_transaction_topic_name")
    private String KafkaTransactionTopicName;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static Config configInstance;

    private Config() {
    }

    public static void Init() {
        String configFilePath = System.getenv("JAVA_E_WALLET_WALLET_CONF");

        try {
            configInstance = mapper.readValue(new File(configFilePath), Config.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to load configuration.");
        }
    }

    public static Config getConfigInstance() {
        if (configInstance == null) {
            throw new IllegalStateException("Config must be initialized first by calling Init().");
        }
        return configInstance;
    }


    public static String getKafkaAddress() {
        if (configInstance == null) {
            throw new IllegalStateException("Config must be initialized first by calling Init().");
        }
        return configInstance.KafkaAddress;
    }

    public static String getKafkaTransactionTopicName() {
        if (configInstance == null) {
            throw new IllegalStateException("Config must be initialized first by calling Init().");
        }
        return configInstance.KafkaTransactionTopicName;
    }
}
