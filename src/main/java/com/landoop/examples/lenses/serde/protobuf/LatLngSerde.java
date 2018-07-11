package com.landoop.examples.lenses.serde.protobuf;

import com.landoop.lenses.lsql.serde.Deserializer;
import com.landoop.lenses.lsql.serde.Serde;
import com.landoop.lenses.lsql.serde.Serializer;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.util.Properties;

public class LatLngSerde implements Serde {
  private Schema schema = SchemaBuilder.builder()
      .record("lat_lng")
      .fields()
      .requiredDouble("lat")
      .requiredDouble("lng")
      .endRecord();

  @Override
  public Serializer serializer(Properties properties) {
    return new Serializer() {

      @Override
      public byte[] serialize(GenericRecord record) throws IOException {
        double lat = (double) record.get("lat");
        double lng = (double) record.get("lng");
        String data = lat + ":" + lng;
        return data.getBytes("UTF-8");
      }

      @Override
      public void close() throws IOException {
      }
    };
  }

  @Override
  public Deserializer deserializer(Properties properties) {
    return new Deserializer() {
      @Override
      public GenericRecord deserialize(byte[] bytes) throws IOException {
        String data = new String(bytes);
        String[] tokens = data.split(":");
        double lat = Double.parseDouble(tokens[0]);
        double lng = Double.parseDouble(tokens[1]);

        GenericRecord record = new GenericData.Record(schema);
        record.put("lat", lat);
        record.put("lng", lng);
        return record;
      }

      @Override
      public void close() throws IOException {
      }
    };
  }

  @Override
  public Schema getSchema() {
    return schema;
  }
}
