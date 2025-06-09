package org.planitpoker;

import org.eclipse.paho.client.mqttv3.*;
import org.planitpoker.Logger;

public class MQTTSubscriber {
    private final String broker = "tcp://test.mosquitto.org:1883";
    private final String clientId = MqttClient.generateClientId();
    private MqttClient client;

    public MQTTSubscriber(String topic, MqttCallback callback) throws MqttException {
        client = new MqttClient(broker, clientId);
        client.setCallback(callback);
        client.connect();
        client.subscribe(topic);
        Logger.getLogger().info("Subscribed to topic: " + topic);
    }

    public void close() throws MqttException {
        client.disconnect();
    }
}
