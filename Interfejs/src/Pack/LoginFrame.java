package Pack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.List;

public class LoginFrame extends JFrame implements ActionListener {
	Logger logger = Logger.getLogger(LoginFrame.class);
	List<Component> components;
    public LoginFrame() {	
    	super("login");
    	setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(250, 150);
		setLocation(200, 100);

		JPanel mainPanel = new JPanel();
		JPanel labelPanel = new JPanel();
		JPanel fieldPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		fieldPanel.setLayout(new GridLayout(0,2));
		
		JLabel name = new JLabel("Name:");
		JLabel password = new JLabel("Password:");
		JLabel label = new JLabel("Logowanie");
		
		JTextField nameField = new JTextField();
		nameField.setName("name");
		JPasswordField passField = new JPasswordField();
		passField.setName("pass");
		
		JButton loginButton = new JButton("Loguj");
		loginButton.addActionListener(this);
		JButton registerButton = new JButton("Rejestruj");
		registerButton.addActionListener(this);
				
		labelPanel.add(label);
		
		fieldPanel.add(name);
		fieldPanel.add(nameField);
		fieldPanel.add(password);
		fieldPanel.add(passField);
		
		buttonPanel.add(loginButton);
		buttonPanel.add(registerButton);
		
		mainPanel.add(labelPanel);
		mainPanel.add(fieldPanel);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
		components = Util.getAllComponents(this.getContentPane());
    }
    
    public void actionPerformed(ActionEvent e) {  	
    	if(e.getActionCommand() == "Loguj") {
    	 	ConnectionDataBase connection = new ConnectionDataBase();
    	 	if(connection.login(((JTextField) Util.getComponent("name", components)).getText(), ((JTextField) Util.getComponent("pass", components)).getText())) {  
				String mail = connection.getMail(((JTextField) Util.getComponent("name", components)).getText());
		    	this.dispose();

			    new AppFrame(mail);
    		} else {
				logger.warn("z³e logowanie");
    		}
    		
    	} else if (e.getActionCommand() == "Rejestruj") {
			new RegisterFrame();
		}	
    }
}