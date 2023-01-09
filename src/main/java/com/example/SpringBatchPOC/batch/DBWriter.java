package com.example.SpringBatchPOC.batch;

import com.example.SpringBatchPOC.model.User;
import com.example.SpringBatchPOC.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<User> {

    private static final Logger LOG = LoggerFactory.getLogger(DBWriter.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends User> users) throws Exception {
        LOG.info("Escribiendo en db ({} users)...", users.size());
        logUsers(users);
        userRepository.saveAll(users);

        // Simulando llamada de red bloqueante con RestTemplate, en estos casos necesitas que dicho Step sea ejecutando asincroncamente.
        Thread.sleep(3000L);
    }

    private void logUsers(List<? extends User> users) {
        users.forEach(user -> LOG.info(user.toString()));
    }

}

