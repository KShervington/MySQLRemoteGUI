package com.company;// Display the results of queries against the bikes table in the bikedb database.


import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class DisplayQueryResults extends JFrame
{
   // default query retrieves all data from bikes table
   static final String DEFAULT_QUERY = "SELECT * FROM bikes";
   static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);

   // Allows "Main" to get user-entered info
   public static String dbUrl = "";
   public static String dbUserName = "";
   public static String dbPassword = "";
   public static String dbDriver = "";
   
   private ResultSetTableModel tableModel;
   private JTextArea queryArea;

   JTable resultTable = null;
   JScrollPane scroller = null;

   // create ResultSetTableModel and GUI
   public DisplayQueryResults() 
   {   
      super( "SQL Client App" );

         setMinimumSize(new Dimension(700, 600));
         setLayout(null);
         setLocationRelativeTo(null); // Place GUI window in center of screen

         // Command entry section
         // set up JTextArea in which user types queries
         queryArea = new JTextArea( 3, 100 );
         queryArea.setWrapStyleWord( true );
         queryArea.setLineWrap( true );

         // Title for query window
         JLabel queryLabel = new JLabel("Enter An SQL Command");
         queryLabel.setBounds(320, 10, 500, 15);
         queryLabel.setForeground(Color.blue);

         // Scroll pane for text area
         JScrollPane scrollPane = new JScrollPane( queryArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
         // end of command entry section

         // Database info
         JLabel dbInfo = new JLabel("Enter Database Information");
         dbInfo.setBounds(10, 10, 500, 15);
         dbInfo.setForeground(Color.blue);

         JLabel driverLabel = new JLabel("JDBC Driver");
         driverLabel.setBounds(10, 30, 200, 15);
         String[] driverArray = {"com.mysql.cj.jdbc.Driver"};
         final JComboBox driverSelect = new JComboBox(driverArray);
         driverSelect.setBounds(100, 30, 200, 20);

         JLabel urlLabel = new JLabel("Database URL");
         urlLabel.setBounds(10, 55, 200, 15);
         String[] urlArray = {"jdbc:mysql://localhost:3306/project3"};
         final JComboBox urlSelect = new JComboBox(urlArray);
         urlSelect.setBounds(100, 55, 200, 20);

         JLabel userNameLabel = new JLabel("Username");
         userNameLabel.setBounds(10, 80, 200, 15);
         final JTextField userNameField = new JTextField();
         userNameField.setBounds(100, 80, 200, 20);

         JLabel pswdLabel = new JLabel("Password");
         pswdLabel.setBounds(10, 105, 200, 15);
         final JPasswordField pswd = new JPasswordField();
         pswd.setBounds(100, 105, 200, 20);

         final JLabel connectStatus = new JLabel("No Connection Now");
         connectStatus.setOpaque(true);
         connectStatus.setBackground(Color.BLACK);
         connectStatus.setForeground(Color.RED);
         connectStatus.setBounds(10, 130, 290, 25);
         // end of Database info

         // Buttons
         // set up JButton for submitting queries
         JButton executeButton = new JButton( "Execute SQL Command" );
         executeButton.setBackground(Color.GREEN);
         executeButton.setForeground(Color.BLACK);
         executeButton.setBorderPainted(false);
         executeButton.setOpaque(true);
         executeButton.setFont(BUTTON_FONT);
         executeButton.setBounds(450, 170, 175, 35);

         // Button for clearing query area
         JButton clearCommButton = new JButton("Clear SQL Command");
         clearCommButton.setBackground(Color.WHITE);
         clearCommButton.setForeground(Color.RED);
         clearCommButton.setBorderPainted(false);
         clearCommButton.setOpaque(true);
         clearCommButton.setFont(BUTTON_FONT);
         clearCommButton.setBounds(250, 170, 175, 35);

         // Button for connecting to database
         JButton connectButton = new JButton("Connect To Database");
         connectButton.setBackground(Color.BLUE);
         connectButton.setForeground(Color.YELLOW);
         connectButton.setBorderPainted(false);
         connectButton.setOpaque(true);
         connectButton.setFont(BUTTON_FONT);
         connectButton.setBounds(50, 170, 175, 35);

         // Button for clearing result window
         JButton clearResultButton = new JButton("Clear Result Window");
         clearResultButton.setBackground(Color.YELLOW);
         clearResultButton.setForeground(Color.BLACK);
         clearResultButton.setBorderPainted(false);
         clearResultButton.setOpaque(true);
         clearResultButton.setFont(BUTTON_FONT);
         clearResultButton.setBounds(50, 475, 175, 35);
         // end of buttons

         // Result window label
         JLabel resultLabel = new JLabel("SQL Execution Result Window");
         resultLabel.setBounds(50, 235, 500, 30);
         scrollPane.setBounds(320, 30, 300, 100);
         
         // place GUI components on content pane
         add(dbInfo);
         add(queryLabel);
         add(driverLabel);
         add(driverSelect);
         add(urlLabel);
         add(urlSelect);
         add(userNameLabel);
         add(userNameField);
         add(pswdLabel);
         add(pswd);
         add(connectStatus);
         add( scrollPane );
         add(executeButton);
         add(clearCommButton);
         add(connectButton);
         add(clearResultButton);
         add(resultLabel);

         // Clear query area text
         clearCommButton.addActionListener(
                 new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       queryArea.setText("");
                    }
                 }
         );

         // Connect to database
         connectButton.addActionListener(
                 new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                       dbDriver = driverSelect.getSelectedItem().toString();
                       dbUrl = urlSelect.getSelectedItem().toString();
                       dbUserName = userNameField.getText();
                       dbPassword = pswd.getText(); // Get password as plain text

                       Main.makeConnection();
                       connectStatus.setText("Connected to " + urlSelect.getSelectedItem().toString());
                    }
                 }
         );

         clearResultButton.addActionListener(
                 new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       scroller.setVisible(false); // make result window invisible
                    }
                 }
         );

         // create event listener for executeButton
         executeButton.addActionListener(
         
            new ActionListener() 
            {
               String checker = "SELECT"; // Helps determine type of command

               public void actionPerformed( ActionEvent event )
               {

                  try 
                  {
                     // Perform either query or update
                     if (queryArea.getText().toUpperCase().contains(checker))
                     {
                        drawTable();
                        scroller.setVisible(true); // make result window visible
                     }
                     else
                     {
                        tableModel.setUpdate( queryArea.getText() );
                     }
                  } // end try
                  catch ( SQLException sqlException ) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        sqlException.getMessage(), "Database error", 
                        JOptionPane.ERROR_MESSAGE );

                     try
                     {
                        tableModel.setQuery( DEFAULT_QUERY );
                        queryArea.setText( "" );
                     }
                     catch ( SQLException sqlException2 )
                     {
                        JOptionPane.showMessageDialog( null,
                                sqlException2.getMessage(), "Database error",
                                JOptionPane.ERROR_MESSAGE );

                        // ensure database connection is closed
                        tableModel.disconnectFromDatabase();

                        System.exit( 1 ); // terminate application
                     } // end inner catch
                  } // end outer catch
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener

         //setSize( 600, 300 ); // set window size
         setVisible( true ); // display window

      
      // dispose of window when user quits application (this overrides
      // the default of HIDE_ON_CLOSE)
      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
      
      // ensure database connection is closed when user quits application
      addWindowListener(new WindowAdapter() 
         {
            // disconnect from database and exit when window has closed
            public void windowClosed( WindowEvent event )
            {
               tableModel.disconnectFromDatabase();
               System.exit( 0 );
            } // end method windowClosed
         } // end WindowAdapter inner class
      ); // end call to addWindowListener
   } // end DisplayQueryResults constructor

   // Create table and add it to GUI window
   public void drawTable () {
      try {
         // Get table info based on query
         tableModel = new ResultSetTableModel( queryArea.getText() );
         tableModel.setQuery(queryArea.getText());

         // Create table using retrieved result set table
         resultTable = new JTable( tableModel );
         resultTable.setGridColor( Color.BLACK );

         // Scroll pane for table results
         scroller = new JScrollPane( resultTable );
         scroller.setBounds(50, 260, 585, 200);
         this.add( scroller );
      }
      catch ( ClassNotFoundException classNotFound )
      {
         JOptionPane.showMessageDialog( null,
                 "MySQL driver not found", "Driver not found",
                 JOptionPane.ERROR_MESSAGE );

         System.exit( 1 ); // terminate application
      } // end catch
      catch ( SQLException sqlException )
      {
         JOptionPane.showMessageDialog( null, sqlException.getMessage(),
                 "Database error", JOptionPane.ERROR_MESSAGE );

         // ensure database connection is closed
         tableModel.disconnectFromDatabase();

         System.exit( 1 );   // terminate application
      } // end catch
   }

} // end class DisplayQueryResults




