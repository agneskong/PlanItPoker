package org.planitpoker;

import org.eclipse.paho.client.mqttv3.*;

public class MQTTPublisher {
    private final String broker = "tcp://test.mosquitto.org:1883";
    private final String clientId = MqttClient.generateClientId();
    private MqttClient client;

    public MQTTPublisher() throws MqttException {
        client = new MqttClient(broker, clientId);
        client.connect();
    }

    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(1);
        client.publish(topic, mqttMessage);
        System.out.println("Published to " + topic + ": " + message);
    }

    public void close() throws MqttException {
        client.disconnect();
    }
}
