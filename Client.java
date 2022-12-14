import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {
    Socket socket;

    BufferedReader br;
    PrintWriter out;


    // Declare Components

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    // CONSTRUCTOR CLIENT
    public Client() {
        try {
            System.out.println("Sending the request server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWriting();
        } catch (Exception e) {

        }
    }
        //handleEvent here

        private void handleEvents(){
            messageInput.addKeyListener((KeyListener) new KeyListener(){

                @Override
                public void keyTyped(KeyEvent e) {
                   
                    
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                   
                    // System.out.println("Key released" + e.getKeyCode());
                    if(e.getKeyCode()==10){
                        // System.out.println("You have pressed enter button");
                        String contentToSend = messageInput.getText();
                        messageArea.append("Me :" + contentToSend+"\n");
                        out.println(contentToSend);
                        out.flush();
                        messageInput.setText("");
                        messageInput.requestFocus();
                    }
                    
                }

            });
        }
    // createGUI here
    private void createGUI(){
        // GUI code
        this.setTitle("Client Messager[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coding for components

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("logo.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.LEFT);

        // frame layout set

        this.setLayout(new BorderLayout());

        // adding the components to frame

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);





        this.setVisible(true);
    }


    // reading method
    public void startReading() {
        // thread -read data
        Runnable r1 = () -> {
            System.out.println("Reader started..");

            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server: " + msg);
                    messageArea.append("Server: " + msg+ "\n");

                }
            } catch (Exception e) {

                // e.printStackTrace();
                System.out.println("connection closed");
            }

        };
        new Thread(r1).start();

    }

    public void startWriting() {
        // thread - data use and send client

        Runnable r2 = () -> {
            System.out.println("Writing start..");
            try {
                while (true && !socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection Close");
            }

        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is Client");
        new Client();
    }

}
