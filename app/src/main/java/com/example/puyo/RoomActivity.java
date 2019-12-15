package com.example.puyo;

public class RoomActivity {

   // board 4개를 만든다.
                    /*
                game 이 시작된 후에는 participant 가 0일 때여야 server socket 을 종료할 수 있는가?
                host 는 다른 방을 만들 수도 있고 심지어 application 을 종료해버릴 수도 있다.
                그러므로 역할은 넘겨져야한다. 혹은 모든 socket 들이 종료되어야한다.
                그리고 null 확인은 입장 대기가 있을 때 유용하다.

                try{
                    if(parent_thread != null){
                        parent_thread.close_server();
                    }
                    parent_thread = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                /*
                방장이 방을 나올 때 RoomActivity 를 삭제하고 parent_thread 를 멈추며
                (server_socket 을 초기화할 필요는 없지만 state_check, participant 는
                초기화해줘야할 것이다.) - 이때 SocketThread 들은 모두 제거되어야한다.
                client 들의 입장에서는 socket 에서 종료 통보를 받고 RoomActivity 와
                socket 을 삭제한 뒤 client_thread 를 멈추는 비슷한 작업이 된다.
                // async? lock?

                */

                //게임이 종료되면 나가기가 활성화되고, server_thread를 destroy.
}
