package chatting.application;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Client extends JFrame {

    private JPanel chattingPanel;
    Box vertical = Box.createVerticalBox();    // creating a container(box) that arranges in vert.
    BufferedReader in;
    PrintWriter sent;
    String out;
    Socket s=null;



    Client(){
        appUI();
        startClient();
    }

    private void appUI(){
        setSize(450,700);
        setLocation(250,150);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        header();
        chatPanel();
        createInputArea();
        add(addScroll());

        setUndecorated(true);
        setVisible(true);
    }

    private void startClient(){
        
            try{
                s = new Socket("127.0.0.1",4044);

                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                sent = new PrintWriter(s.getOutputStream(),true);
            
                startReading();
              //  startWriting();

            } catch(IOException e){
                e.printStackTrace();
            }
        
    }

    private void startReading(){
        
        Runnable r1 = ()->{
            try{ 
            String message;
                while((message = in.readLine()) != null){
                    addRecievedMessage(message);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }

    private void header(){
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0,117,94));
        headerPanel.setBounds(0,0,450,80);
        headerPanel.setLayout(null);
        add(headerPanel);

        //profile
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/profile1.png")); //File path as we are  already inside src so start from there
        Image i2 = i1.getImage().getScaledInstance(55,55,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel profile = new JLabel(i3);
        profile.setBounds(60, 15, 55, 55);
        headerPanel.add(profile);
        
        //Back button
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png")); //File path as we are  already inside src so start from there
        Image i5 = i4.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JButton back = new JButton(i6);
        back.setBackground(new Color(0,117,94));
        back.setBounds(20, 30, 30, 30);
        headerPanel.add(back);

        back.addMouseListener(new MouseAdapter(){        //Adding mouse Event
            public void mouseClicked(MouseEvent a){
                System.exit(0);         // Close the application   
            }
        });

        //Menu options
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png")); //File path as we are  already inside src so start from there
        Image i8 = i7.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JButton menu = new JButton(i9);
        menu.setBackground(new Color(0,117,94));
        menu.setBounds(400, 30, 10, 25);
        headerPanel.add(menu);
         

        menu.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent a){
                
                System.out.println("Developed by - ");
                System.out.println("     Jatin Rana   ");
            }

        });

        JLabel name = new JLabel("Makhan");
        name.setBounds(130, 20, 80, 25);
        name.setForeground(Color.white);
        name.setFont(new Font("SAN_SERIF",Font.BOLD,18));
        headerPanel.add(name);

    }

    private void chatPanel(){
        chattingPanel = new JPanel();
        chattingPanel.setBounds(5,85,440,570);
        add(chattingPanel);

    }

    private void createInputArea(){
        JTextField chat = new JTextField();
        chat.setBounds(5,660, 310, 35);
        chat.setFont(new Font("SAN_SARIF", Font.PLAIN, 16));
        add(chat);

        JButton send = new JButton("Send");
        send.setBackground(new Color(0,117,94));
        send.setForeground(Color.WHITE);
        send.setBounds(320, 655, 120, 40);
        add(send);

        send.addActionListener(new ActionListener() {  // Process and send the message
            public void actionPerformed(ActionEvent e) {  
                out = chat.getText();
                if(!out.isEmpty()){                 //to handle empty messages
                    addMessage(out);
                    sent.println(out);
                    chat.setText("");            // Clear the input field
                }  
            }
        });
    }

    public void addMessage(String out){

                JLabel message = new JLabel(out);
                message.setFont(new Font("Tahoma",Font.PLAIN,16));
                message.setBackground(new Color(37, 211, 102));
                message.setOpaque(true);
                message.setBorder(new EmptyBorder(10,10,10,50));

                Calendar t = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                JLabel time = new JLabel();
                time.setText(sdf.format(t.getTime()));             //setting time changing in real time so used settext instead of direct in jlabel
                

                JPanel bubble = new JPanel();
                bubble.setLayout(new BoxLayout(bubble,BoxLayout.Y_AXIS));     
                bubble.add(message);
                bubble.add(time);
                JPanel messageBlock = new JPanel(new BorderLayout());
                messageBlock.add(bubble,BorderLayout.EAST);

                vertical.add(messageBlock);
                vertical.add(Box.createVerticalStrut(15));       // vertical spacing
                chattingPanel.setLayout(new BorderLayout());
                chattingPanel.add(vertical,BorderLayout.PAGE_START);
                revalidate();                                            // refresh UI

    }


    public void addRecievedMessage(String in){
        JLabel message = new JLabel(in);
        message.setFont(new Font("Tahoma",Font.PLAIN,16));
        message.setBackground(new Color(0, 122, 255));
        message.setOpaque(true);
        message.setBorder(new EmptyBorder(10,10,10,50));

        Calendar t = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        JLabel time = new JLabel();
        time.setText(sdf.format(t.getTime()));             //setting time changing in real time so used settext instead of direct in jlabel
        

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble,BoxLayout.Y_AXIS));     
        bubble.add(message);
        bubble.add(time);
        JPanel messageBlock = new JPanel(new BorderLayout());
        messageBlock.add(bubble,BorderLayout.WEST);

        vertical.add(messageBlock);
        vertical.add(Box.createVerticalStrut(15));       // vertical spacing
        chattingPanel.setLayout(new BorderLayout());
        chattingPanel.add(vertical,BorderLayout.PAGE_START);
        revalidate(); 

}

    private JScrollPane addScroll(){

        JScrollPane scrollpane = new JScrollPane(chattingPanel);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpane.setBounds(5, 85, 440, 570);
        return scrollpane;

    };
    
   
    public static void main(String args[]){
        SwingUtilities.invokeLater(() -> new Client());
    }
}
