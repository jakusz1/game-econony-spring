package pl.jakusz.gameeconomyspring.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pl.jakusz.gameeconomyspring.model.User;
import pl.jakusz.gameeconomyspring.model.UserChange;
import pl.jakusz.gameeconomyspring.repo.ActiveUserRepository;
import pl.jakusz.gameeconomyspring.repo.UserRepository;

import java.util.List;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ActiveUserRepository activeUsers = new ActiveUserRepository();


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String username = headers.getNativeHeader("username").get(0);
        logger.info("Received a new web socket connection from: "+ headers.getSessionId() +" , " +username);

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

        simpMessageSendingOperations.convertAndSend("/player/"+ username, activeUsers);
        simpMessageSendingOperations.convertAndSend("/topic/public", new UserChange(username,UserChange.ChangeType.JOIN));

        activeUsers.addActiveUser(headers.getSessionId(),username);
        logger.info("CONNECTED USERS: "+activeUsers.getSize());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");

//        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
//        String username = headers.getUser().getName();
        //StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        //String username = headers.getNativeHeader("username").get(0);

        //if(username != null) {
        String username = activeUsers.getUsername(event.getSessionId());
        logger.info("User Disconnected : " + username);
        activeUsers.removeActiveUser(event.getSessionId());
        logger.info("CONNECTED USERS: "+activeUsers.getSize());
        simpMessageSendingOperations.convertAndSend("/topic/public", new UserChange(username,UserChange.ChangeType.LEAVE));
        //}
    }

}
