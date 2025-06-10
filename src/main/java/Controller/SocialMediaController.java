package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    //constructor
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();

    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/login", this::loginHandler);
        app.post("/register", this::postAccountHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.post("/messages", this::newMessageHandler);
        app.get("/messages/{message_id}", this::MessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::MessageByUserHandler);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    
    private void loginHandler(Context cxt) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(cxt.body(), Account.class);
        Account loginAccount = accountService.login(account);
        if(loginAccount != null){
            cxt.json(loginAccount);
        }else{
            cxt.status(401);
        }
    }


    private void postAccountHandler(Context cxt) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(cxt.body(), Account.class);
        Account registerAccount = accountService.registerAccount(account);
        if(registerAccount != null){
            cxt.json(mapper.writeValueAsString(registerAccount));
        }else{
            cxt.status(400);
        }

    }
    private void newMessageHandler(Context cxt) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(cxt.body(), Message.class);
        Message newMessage = messageService.newMessage(message);
        if(newMessage != null){
            cxt.json(mapper.writeValueAsString(newMessage));
        }else{
            cxt.status(400);
        }
    }

    private void getAllMessageHandler(Context cxt){
        List<Message> message = messageService.getAllMessages();
        cxt.json(message);

    }

    private void MessageByIdHandler(Context cxt) throws JsonProcessingException{
        int id = Integer.parseInt(cxt.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if(message != null){
            cxt.json(message);
        }

    }

    private void deleteMessageHandler(Context cxt){
        int id = Integer.parseInt(cxt.pathParam("message_id"));
        Message delMessage = messageService.deleteMessage(id);
        if(delMessage != null){
            cxt.json(delMessage);
        }

    }

    private void patchMessageHandler(Context cxt) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(cxt.body(), Message.class);
        int id = Integer.parseInt(cxt.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(id, message);
        if(updatedMessage != null){
            cxt.json(updatedMessage);
        }else{
            cxt.status(400);
        }

    }

    private void MessageByUserHandler(Context cxt){
        int id = Integer.parseInt(cxt.pathParam("account_id"));
        List<Message> message = messageService.getMessagesByUser(id);
        cxt.json(message);
    }
}