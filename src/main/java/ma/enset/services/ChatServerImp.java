package ma.enset.services;

import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Chat;
import ma.enset.stubs.ChatServiceGrpc;

import java.util.HashMap;

public class ChatServerImp extends ChatServiceGrpc.ChatServiceImplBase {
   HashMap<String,StreamObserver<Chat.Message>> clients=new HashMap<>();
   @Override
   public StreamObserver<Chat.Message> fullStream(StreamObserver<Chat.Message> responseObserver) {
       return new StreamObserver<Chat.Message>() {
           @Override
           public void onNext(Chat.Message message) {
               String messageFrom = message.getMessageFrom();
               String messageTo = message.getMessageTo();
               if(!clients.containsKey(messageFrom)){
                   clients.put(messageFrom,responseObserver);
               }

               if (clients.containsKey(messageTo) && !messageTo.equals("")){
                   StreamObserver<Chat.Message> messageStreamObserver= clients.get(messageTo);
                   messageStreamObserver.onNext(message);
               }
           }

           @Override
           public void onError(Throwable throwable) {

           }

           @Override
           public void onCompleted() {
               responseObserver.onCompleted();
           }
       };
   }
}