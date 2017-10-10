package com.astl.energy.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.astl.energy.boundary.EnergyEvent;

@Singleton
@Startup
public class EventSubscriber {

	@Resource(name = "DefaultManagedExecutorService")
	ManagedExecutorService executor;

	@Inject
	Event<EnergyEvent> energyEvent;

	@PostConstruct
	public void listen() {
		System.out.print("started EventSubscriber");

		executor.execute(new SimpleTask());

		System.out.print("started SimpleTask");
	}

	public class SimpleTask implements Runnable {

		@Override
		public void run() {
			KafkaConsumer<String, String> consumer = null;

			try {
				System.out.println("SimpleTask Thread started.");

				String id = "energy-subscriber";

				Properties props = new Properties();
				props.put("bootstrap.servers", "localhost:9092");
				props.put("group.id", id);
				props.put("key.deserializer", StringDeserializer.class.getName());
				props.put("value.deserializer", StringDeserializer.class.getName());
				consumer = new KafkaConsumer<>(props);

				List<String> topics = Arrays.asList("energyhourly");
				consumer.subscribe(topics);

				while (true) {
					ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
					for (ConsumerRecord<String, String> record : records) {
						Map<String, Object> data = new HashMap<>();
						data.put("timestamp", record.timestamp());
						data.put("partition", record.partition());
						data.put("offset", record.offset());
						data.put("key", record.key());
						data.put("value", record.value());

						System.out.println(id + ": " + data);

						EnergyEvent event = new EnergyEvent(record.value());

						energyEvent.fire(event);
						
						System.out.println("fired event " + data);

					}
				}
			} catch (Throwable t) {
				if (t instanceof Exception) {
				((Exception) t).printStackTrace();
				} else {
					System.err.println(t);
				}
			} finally {
				if (consumer != null) {
					consumer.close();
				}
			}
		}
	}
}
