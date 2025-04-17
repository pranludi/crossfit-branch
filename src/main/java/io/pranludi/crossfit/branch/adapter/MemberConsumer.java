package io.pranludi.crossfit.branch.adapter;

import com.google.protobuf.InvalidProtocolBufferException;
import io.pranludi.crossfit.protobuf.kafka.MemberKafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MemberConsumer {

    final Logger log = LoggerFactory.getLogger(MemberConsumer.class);
    final String TOPIC = "members";

    @KafkaListener(topics = TOPIC)
    public void receiveNewMember(ConsumerRecord<String, byte[]> consumerRecord) {
        try {
            byte[] payload = consumerRecord.value();
            MemberKafka memberKafka = MemberKafka.parseFrom(payload);
            log.info("Received new member: " + memberKafka);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

}