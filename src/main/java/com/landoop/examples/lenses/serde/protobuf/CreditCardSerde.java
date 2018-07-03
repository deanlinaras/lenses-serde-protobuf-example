package com.landoop.examples.lenses.serde.protobuf;

import com.landoop.examples.lenses.serde.protobuf.generated.CardData;
import com.landoop.lenses.lsql.serde.Deserializer;
import com.landoop.lenses.lsql.serde.Serde;
import com.landoop.lenses.lsql.serde.Serializer;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.util.Properties;

public class CreditCardSerde implements Serde {

    @Override
    public Serializer serializer(String s, Properties properties) {

        return new Serializer() {
            @Override
            public byte[] serialize(GenericRecord record) throws IOException {
                CardData.CreditCard card = CardData.CreditCard.newBuilder()
                        .setName((String) record.get("name"))
                        .setCardNumber((String) record.get("cardNumber"))
                        .setCardType(CardData.CreditCard.CardType.valueOf((String) record.get("cardType")))
                        .setCountry((String) record.get("country"))
                        .setBlocked((boolean) record.get("blocked"))
                        .setCurrency((String) record.get("currency"))
                        .build();
                return card.toByteArray();
            }

            @Override
            public void close() throws IOException {
            }
        };
    }

    @Override
    public Deserializer deserializer(String s, Properties properties) {
        return new Deserializer() {
            @Override
            public GenericRecord deserialize(byte[] bytes) throws IOException {

                CardData.CreditCard card = CardData.CreditCard.parseFrom(bytes);

                Schema schema = SchemaBuilder.builder()
                        .record("credit_card")
                        .fields()
                        .requiredString("name")
                        .requiredString("cardNumber")
                        .requiredString("cardType")
                        .requiredString("country")
                        .requiredString("currency")
                        .requiredBoolean("blocked")
                        .endRecord();

                GenericRecord record = new GenericData.Record(schema);
                record.put("name", card.getName());
                record.put("cardNumber", card.getCardNumber());
                record.put("cardType", card.getCardType().name());
                record.put("country", card.getCountry());
                record.put("currency", card.getCurrency());
                record.put("blocked", card.getBlocked());
                return record;
            }

            @Override
            public void close() throws IOException {
            }
        };
    }
}
