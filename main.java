import java.io.*;
import java.net.*;

public class main {

    public static void main(String[] args) throws Exception {

        String server = "irc.luatic.net";
        String nick = "치노";
        String login = "chino";
        String channel = "";
        String command = "";
        String join_msg = "";

        //private static final String regex = "(\\S+) (\\S+) (\\S+) (\\S+)";
        
        Socket socket = new Socket(server, 7778);
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream( )));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream( )));
        
        writer.write("USER " + login + " 8 * :I'm chino bot!\r\n");
        writer.flush( );
        writer.write("NICK " + nick + "\r\n");
        writer.flush();
        
        String line = null;

        System.out.println("Starting Client");

        while ((line = reader.readLine( )) != null) {

            if (line.indexOf("433") >= 0) {
                System.out.println("[!] 닉네임이 이미 사용중입니다. 봇을 종료합니다.");
                return;
            }

            else if (line.indexOf("PING") >= 0) {
                String abc = line.split("PING :")[1];
                writer.write("PONG " + abc + "\r\n");
                writer.flush();
                System.out.println("[*] 서버와 연결되었습니다. PONG 데이터를 전송합니다.\r\n");
                break;
            }
        }
        
        //Pattern pattern = Pattern.compile(regex);
        
        while ((line = reader.readLine( )) != null) {
            //Matcher match = pattern.matcher(line);

            if (line.indexOf("PING :") >= 0 && line.indexOf("@") <= 0) {
                System.out.println("[서버 요청] 명령: PING");

		        String abc = line.split("PING :")[1];
                writer.write("PONG :" + abc.trim() + "\r\n");
                writer.flush();

                System.out.println("[서버 요청] 응답: " + abc + "\r\n");
                
            } else if (line.indexOf(":maki.luatic.net 266") >= 0) {
                System.out.println("[*] 봇을 로그인하는 중입니다..");
                writer.write("PRIVMSG ^^ 로그인 username password\r\n");
                writer.write("mode 치노 +x\r\n");
                writer.flush();
                writer.write("JOIN " + channel + "\r\n");
                writer.flush( );
                System.out.println("[*] 봇이 로그인되었습니다.\r\n");
            } else if (line.indexOf("# :") >= 0) {

                command = line.split("# :")[1];

                if (command.equals("#명령어")) {
                    System.out.println("[유저 요청] 명령: 명령어");
                    writer.write("PRIVMSG # :#명령어 | #저장 #불러 #청소 | #말해\r\n");
                    writer.flush();
                    System.out.println("[유저 요청] 응답: #명령어 | #저장 #불러 #청소 | #말해\r\n");
                }

                if (command.indexOf("#저장") >= 0) {
                	if (line.indexOf("username.users.Luatic.net") >=0 ) {
	                    System.out.println("[관리자 요청] 명령: 저장");
	                    join_msg = command.split("#저장 ")[1];
	                    writer.write("PRIVMSG # :저장되었습니다.\r\n");
	                    writer.flush();
	                    System.out.println("[관리자 요청] 응답: 저장되었습니다.\r\n");
                	}
                }

                if (command.equals("#불러")) {
                    System.out.println("[유저 요청] 명령: 명령어");

                    if(! join_msg.equals("") ) {
	                    writer.write("PRIVMSG # :" + join_msg + "\r\n");
	                    writer.flush();
	                    System.out.println("[유저 요청] 응답: " + join_msg + "\r\n");
                	} else {
                		writer.write("PRIVMSG # :저장된 메세지가 없습니다.\r\n");
	                    writer.flush();
	                    System.out.println("[유저 요청] 응답: 저장된 메세지가 없습니다.\r\n");
                	}
                }

                if (command.equals("#청소")) {
                	if (line.indexOf("username.users.Luatic.net") >=0 ) {
	                    System.out.println("[관리자 요청] 명령: 청소");
	                    join_msg = "";
	                    writer.write("PRIVMSG # :초기화 되었습니다.\r\n");
	                    writer.flush();
	                    System.out.println("[관리자 요청] 응답: 초기화 되었습니다.\r\n");
                	}
                }

            } else if (line.indexOf("JOIN :#") >= 0) {
                if ( !join_msg.equals("") ) {
                    writer.write("PRIVMSG # :" + join_msg + "\r\n");
                    writer.flush();
                }
            }
            else {
                //System.out.println(line);
            }
        }

    
    }

}
