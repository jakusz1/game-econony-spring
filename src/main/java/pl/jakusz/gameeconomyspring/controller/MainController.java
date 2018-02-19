package pl.jakusz.gameeconomyspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import pl.jakusz.gameeconomyspring.model.User;
import pl.jakusz.gameeconomyspring.repo.TransferRepository;
import pl.jakusz.gameeconomyspring.repo.UserRepository;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransferRepository transferRepository;


    @MessageMapping("/app.onConnect")
    public void onConnect(@Payload String username, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", username);
        List<User> foundUserList = userRepository.findByName(username);
        if(foundUserList.isEmpty()){
            userRepository.save(new User(username));
            simpMessageSendingOperations.convertAndSendToUser(username, "/queue/update", User.START_BALANCE);
            // TODO send new username to everyone
        }
        else{
            simpMessageSendingOperations.convertAndSendToUser(username, "/queue/update", foundUserList.get(0).getBalance());
        }
    }




}
