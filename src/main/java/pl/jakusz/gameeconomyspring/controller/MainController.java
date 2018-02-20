package pl.jakusz.gameeconomyspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import pl.jakusz.gameeconomyspring.model.Transfer;
import pl.jakusz.gameeconomyspring.model.User;
import pl.jakusz.gameeconomyspring.model.UserChange;
import pl.jakusz.gameeconomyspring.repo.UserRepository;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    UserRepository userRepository;


    @MessageMapping("/app.onConnect")
    @SendTo("/topic/public")
    public UserChange onConnect(@Payload String username, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", username);
        List<User> foundUserList = userRepository.findByName(username);
        if(foundUserList.isEmpty()){
            userRepository.save(new User(username));
            simpMessageSendingOperations.convertAndSendToUser(username, "/queue/update", User.START_BALANCE);
        }
        else{
            simpMessageSendingOperations.convertAndSendToUser(username, "/queue/update", foundUserList.get(0).getBalance());
        }
        return new UserChange(username,UserChange.ChangeType.JOIN);
    }

    @MessageMapping("/app.onNewTransfer")
    public void onNewTransfer(@Payload Transfer transfer, SimpMessageHeaderAccessor headerAccessor){
        String mainUsername = (String) headerAccessor.getSessionAttributes().get("username");
        String receiverUsername = transfer.getReceiverUsername();

        User mainUser = userRepository.findByName(mainUsername).get(0);
        int mainUserNewBalance = mainUser.changeBalance(receiverUsername!=null?-transfer.getValue():transfer.getValue());
        simpMessageSendingOperations.convertAndSendToUser(mainUsername, "/queue/update", mainUserNewBalance);

        if(receiverUsername!=null){
            User receiverUser = userRepository.findByName(receiverUsername).get(0);
            int receiverUserNewBalance = receiverUser.changeBalance(transfer.getValue());
            simpMessageSendingOperations.convertAndSendToUser(receiverUsername, "/queue/update", receiverUserNewBalance);
        }
    }




}
