package pl.jakusz.gameeconomyspring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    UserRepository userRepository;


    @MessageMapping("/onConnect")
    @SendTo("/topic/public")
    public UserChange onConnect(@Payload UserChange userChange, SimpMessageHeaderAccessor headerAccessor){
        String username = userChange.getUsername();
        headerAccessor.getSessionAttributes().put("username", username);
        logger.info(username);
        List<User> foundUserList = userRepository.findByName(username);
        if(foundUserList.isEmpty()){
            logger.info("if");
            userRepository.save(new User(username));
            simpMessageSendingOperations.convertAndSend("/player/"+ username, User.START_BALANCE);
        }
        else{
            logger.info("else");
            simpMessageSendingOperations.convertAndSend("/player/"+ username, foundUserList.get(0).getBalance());
        }
        //userChange.setChangeType(UserChange.ChangeType.JOIN);
        return userChange;
    }

    @MessageMapping("/transfer")
    public void onNewTransfer(@Payload Transfer transfer, SimpMessageHeaderAccessor headerAccessor){
        String mainUsername = (String) headerAccessor.getSessionAttributes().get("username");
        String receiverUsername = transfer.getReceiverUsername();

        User mainUser = userRepository.findByName(mainUsername).get(0);
        int mainUserNewBalance = mainUser.changeBalance(receiverUsername!=null?-transfer.getValue():transfer.getValue());
        logger.info(String.valueOf(mainUserNewBalance));
        simpMessageSendingOperations.convertAndSend("/player/"+ mainUsername, mainUserNewBalance);
        userRepository.save(mainUser);
        if(receiverUsername!=null){
            User receiverUser = userRepository.findByName(receiverUsername).get(0);
            int receiverUserNewBalance = receiverUser.changeBalance(transfer.getValue());
            userRepository.save(receiverUser);

            simpMessageSendingOperations.convertAndSend("/player/"+ receiverUsername, receiverUserNewBalance);
        }

    }




}
