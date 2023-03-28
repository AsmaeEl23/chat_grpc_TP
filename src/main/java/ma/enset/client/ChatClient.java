package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Chat;
import ma.enset.stubs.ChatServiceGrpc;

import java.io.IOException;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5555)
                .usePlaintext()
                .build();
        ChatServiceGrpc.ChatServiceStub asyncStub = ChatServiceGrpc.newStub(managedChannel);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name : ");
        String clientName= sc.next();
        System.out.println(clientName+" connected ....");

        StreamObserver<Chat.Message> send = asyncStub.fullStream(new StreamObserver<Chat.Message>() {
            @Override
            public void onNext(Chat.Message message) {
                String messageFrom= message.getMessageFrom();
                String content =message.getContent();
                System.out.println(messageFrom+" : "+content);

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("End......!");
            }
        });
        while (true){
            System.out.println("===================");
            System.out.println("Send to : ");
            String to=sc.next();
            System.out.println("Message : ");
            String message=sc.next();
            Chat.Message request=Chat.Message.newBuilder()
                    .setMessageFrom(clientName)
                    .setMessageTo(to)
                    .setContent(message)
                    .build();
            send.onNext(request);
        }
    }
}
