package com.example.SpringBatchPOC.batch;

import com.example.SpringBatchPOC.model.User;
import com.example.SpringBatchPOC.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends User> users) throws Exception {
        userRepository.saveAll(users);
    }

}

