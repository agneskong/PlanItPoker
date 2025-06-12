package org.planitpoker;

import org.eclipse.paho.client.mqttv3.*;

/**
 * The MQTTSubscriber class manages subscribing to an MQTT broker topic.
 * It connects to the public Mosquitto broker, subscribes to the given topic,
 * and handles incoming messages using the provided MqttCallback.
 *
 * Core responsibilities include:
 * - Establishing a connection to the MQTT broker.
 * - Subscribing to a specified topic.
 * - Assigning a callback handler for incoming messages and events.
 * - Logging subscription events.
 * - Disconnecting cleanly from the broker.
 *
 * Author: Sathvik Chilakala
 * Date: June 12, 2025
 */


public class T12MQTTSubscriber {
    private final String broker = "tcp://test.mosquitto.org:1883";
    private final String clientId = MqttClient.generateClientId();
    private MqttClient client;

    public T12MQTTSubscriber(String topic, MqttCallback callback) throws MqttException {
        client = new MqttClient(broker, clientId);
        client.setCallback(callback);
        client.connect();
        client.subscribe(topic);
        T12Logger.getLogger().info("Subscribed to topic: " + topic);
    }

    public void close() throws MqttException {
        client.disconnect();
    }
}
