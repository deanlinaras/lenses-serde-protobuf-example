package com.landoop.examples.lenses.serde.protobuf;

import com.landoop.examples.lenses.serde.protobuf.generated.CardData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CreditCardPublisher implements Runnable {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new CreditCardPublisher("cc_protobuf"));
    }

    private final String topic;
    private final Supplier<CardData.CreditCard> generator = new CreditCardGenerator();
    private final Producer<String, byte[]> producer;

    public CreditCardPublisher(String topic) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        this.producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                producer.send(new ProducerRecord<>(topic, generator.get().toByteArray()));
                producer.flush();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
