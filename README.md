# webSocket 사용해보기

- stomp를 이용하여 pub이 발행한 message를 sub을 통해서 데이터 받아서 진행

## websocketConfig 설정

- @EnableWebSocketMessageBroker
    - websocket을 사용하기 위한 설정
    - 웹소켓 연결시 필요한 MessageBroker 제공

## configureMessageBroker

- 메시지 브로커 설정을 위한 메서드
    - config.enableSimpleBroker(“/sub”)
        - 스프링에서 제공하는 내장 브로커 사용 설정
            - 외부 브로커 사용시 enableStompBrokerReplay(”/sub”) 사용
    - 평균적으로 /topic, /queue 사용 → queue는 1대1 ,topic은 1:N으로 사용됨.
- config.setApplicationDestinationPrefixes(”/pub”)
    - 메시지를 보낼때,  관련 경로를 설정해주는 함수
    - 클라이언트가 메시지를 보낼 때, 경로 앞에 /pub을 붙여서 메시지를 전송해야함.

## registerStompEndPoints

- **registry.addEndPoint(”/ws”).setAllowedOrigins(”*”)**
    - 웹소켓의 엔드포인트를 지정 → 추후 클라이언트에서 해당 경로로 서버와 handshake를 진행한다.
        - endpoint의 경우 변경 가능
    - **setAllowdOrigins**
        - 특정 url에 대한 cors 문제를 해결 하기 위해서 사용하는 문법
            - 현재는 *로 전체적으로 허용하게 되지만 특정 url만 가능하게 url을 적어주거나 spring Security를통해서 해당 url을 관리하는게 가능해짐.

---

@MessageMapping(”/channel”)

- 스프링 프레임워크의 일부로서 webSocket을 사용한 메시징 어플리케이션을 쉽게 구현 할수 있게 해주는 어노테이션
- RequestMapping과 유사하게 사용가능.

Prefix로 pub을 줬기 때문에 클라이언트가 메시지를 보내는 경우 → /pub/channel로 보내줘야한다.

- prefix로 해당 pub으로 broker가 확인하여 진행하기 때문에

SimpMessageSendingOperations

- MessageSendingOperations<Destination> 를 spring에 맞춘 것으로 stomp를 지원하기 위해서 사용한다고 문서에 적혀있음.
    - [https://docs.spring.io/spring-framework/docs/4.0.0.M1_to_4.2.0.M2/Spring Framework 4.2.0.M2/org/springframework/messaging/simp/SimpMessageSendingOperations.html](https://docs.spring.io/spring-framework/docs/4.0.0.M1_to_4.2.0.M2/Spring%20Framework%204.2.0.M2/org/springframework/messaging/simp/SimpMessageSendingOperations.html)
