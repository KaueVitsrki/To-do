package com.vaga.todo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaga.todo.dto.UserDto;
import com.vaga.todo.model.TodoModel;
import com.vaga.todo.model.UserModel;
import com.vaga.todo.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void existsByEmailSuccess(){
        UUID id = UUID.fromString("3864f1c7-0a60-4a6e-a4b4-df8a3748ed61");
        List<TodoModel> todoModel = new ArrayList<>();
        UserModel userInput = new UserModel(id, "example@gmail.com", "1234", todoModel);
        UserDto userInput1 = new UserDto(userInput);
        UserModel userModel = this.createUser(userInput1);

        when(userRepository.existsByEmail(userModel.getEmail())).thenReturn(true);

        boolean existsByEmailSuccess = userRepository.existsByEmail(userModel.getEmail());

        assertEquals(true, existsByEmailSuccess);
    }

    @Test
    void existsByEmailError(){
        UUID id = UUID.fromString("3864f1c7-0a60-4a6e-a4b4-df8a3748ed61");
        List<TodoModel> todoModel = new ArrayList<>();
        UserModel userInput = new UserModel(id, "example@gmail.com", "1234", todoModel);
        UserDto userInput1 = new UserDto(userInput);
        UserModel userModel = this.createUser(userInput1);

        when(userRepository.existsByEmail(userModel.getEmail())).thenReturn(false);

        boolean existsByEmailSuccess = userRepository.existsByEmail(userModel.getEmail());

        assertEquals(false, existsByEmailSuccess);
    }

    @Test
    void findByEmailSuccess(){
        UUID id = UUID.fromString("3864f1c7-0a60-4a6e-a4b4-df8a3748ed61");
        List<TodoModel> todoModel = new ArrayList<>();
        UserModel userInput = new UserModel(id, "example@gmail.com", "1234", todoModel);
        UserDto userInput1 = new UserDto(userInput);
        UserModel userModel = this.createUser(userInput1);
 
        when(userRepository.findByEmail(userInput1.getEmail())).thenReturn(Optional.of(userModel));

        Optional<UserModel> findByEmailSearch = userRepository.findByEmail(userModel.getEmail());

        assertTrue(findByEmailSearch.isPresent());
        assertEquals(userModel.getId(), findByEmailSearch.get().getId());
        assertEquals(userModel.getEmail(), findByEmailSearch.get().getEmail());
        assertEquals(userModel.getPassword(), findByEmailSearch.get().getPassword());
        assertEquals(userModel.getTodoModel(), findByEmailSearch.get().getTodoModel());
    }

    @Test
    void findByEmailError(){
        UUID id = UUID.fromString("3864f1c7-0a60-4a6e-a4b4-df8a3748ed61");
        List<TodoModel> todoModel = new ArrayList<>();
        UserModel userInput = new UserModel(id, "example@gmail.com", "1234", todoModel);
        UserDto userInput1 = new UserDto(userInput);
        UserModel userModel = this.createUser(userInput1);
 
        when(userRepository.findByEmail(userModel.getEmail())).thenReturn(Optional.empty());
    
        Optional<UserModel> findByEmailSearch = userRepository.findByEmail(userModel.getEmail());
    
        assertTrue(findByEmailSearch.isEmpty());
    }

    private UserModel createUser(UserDto userDto) {
        UserModel userModel = new UserModel();
        userModel.setEmail(userDto.getEmail());
        userModel.setPassword(userDto.getPassword());
        
        List<TodoModel> todoModelList = new ArrayList<>();
        for (TodoModel todo : userDto.getTodoModel()) {
            TodoModel todoModel = new TodoModel();
            todoModel.setId(todo.getId());
            todoModel.setName(todo.getName());
            todoModel.setDescription(todo.getDescription());

            todoModelList.add(todoModel);
        }
        userModel.setTodoModel(todoModelList);

        return userModel;
    }
}
