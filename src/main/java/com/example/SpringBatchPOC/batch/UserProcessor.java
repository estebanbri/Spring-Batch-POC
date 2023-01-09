package com.example.SpringBatchPOC.batch;

import com.example.SpringBatchPOC.dto.UserDTO;
import com.example.SpringBatchPOC.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserProcessor implements ItemProcessor<UserDTO, User> {

    private static final Map<String, String> DEPT_NAMES =
            new HashMap();

    public UserProcessor() {
        DEPT_NAMES.put("001", "Tecnologia");
        DEPT_NAMES.put("002", "Operaciones");
        DEPT_NAMES.put("003", "Cuentas");
    }

    @Override
    public User process(UserDTO userDTO) throws Exception {
        // if(userDTO.getName().equals("Esteban")) return null; // Tambien se puede hacer logica de filtrado retornando null no se almacena en db
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setDept(DEPT_NAMES.get(user.getDept())); // Transformamos los codigos del csv al nombre de dept
        user.setCreatedDate(new Date());
        return user;
    }

}
