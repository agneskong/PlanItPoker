package org.planitpoker;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * The MQTTPublisher class handles publishing messages to an MQTT broker.
 * It connects to the public Mosquitto broker and allows sending messages
 * to specific topics within the Planning Poker application.
 *
 * Core responsibilities include:
 * - Establishing a connection to the MQTT broker.
 * - Publishing messages with QoS level 1 to a given topic.
 * - Logging published messages using the custom Logger class.
 * - Disconnecting from the broker after publishing.
 *
 * Author: Sathvik Chilakala
 * Date: June 12, 2025
 */


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
        Logger.getLogger().info("Published to " + topic + ": " + message);
    }

    public void close() throws MqttException {
        client.disconnect();
    }
}
