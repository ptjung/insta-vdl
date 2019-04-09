/**
 * This class displays a user interface for the Instagram video downloader, which include all of the window's visuals 
 * and manages all interface events through the swing and AWT libraries.
 * 
 * @author Patrick Jung
 * @version April 9, 2019
 */

package InstagramVideoDownloader;

import InstagramVideoDownloader.VideoDownloader;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
 
class InterfaceWindow extends JFrame {
    
    // Instance constants and variables
    private final String[] ENCODE_FORMATS = { "*.mp4", "*.ogg", "*.wmv", "*.webm", "*.flv", "*.avi" };
    private final String USER_MESSAGE_SUCCESS = "Video has been saved to directory.";
    private final String USER_MESSAGE_FAILURE = "Unable to save video to directory.";
    private final int COLUMN_COUNT = 12;
    
    private VideoDownloader videoDownloader;
    private JComboBox<String> formatBox;
    private JTextField getLinkField;
    private JButton downloadButton;
    private JLabel notification;
    
    /**
     * This method is the class' parameterized constructor. It handles the window's main components including widgets,
     * size, and visibility. The video downloader parameter is used to link the window's button event with the download.
     * 
     * @param videoDownloader The video downloader object instantiated along with the interface window
     */ 
    InterfaceWindow( VideoDownloader videoDownloader ) {
        super( "Instagram Video Downloader" );
        setLayout( new FlowLayout() );
        
        // Window: add a label to prompt the user for a link for the upcoming text field
        add( new JLabel( " Enter a video link: " ) );
        
        // Window: add a text field for the user's input link
        getLinkField = new JTextField( "" );
        getLinkField.setColumns( COLUMN_COUNT );
        add( getLinkField );
        
        // Window: add a file encoder format box to choose which file extension is used when saving the video
        formatBox = new JComboBox<String>( ENCODE_FORMATS );
        formatBox.setMaximumRowCount( ENCODE_FORMATS.length );
        add( formatBox );
        
        // Window: add a button used to download the video
        downloadButton = new JButton( "Download" );
        downloadButton.addActionListener( new ButtonEventListener() );
        add( downloadButton );
        
        // Window: add an invisible label at the end to indicate further notifications on the download success
        notification = new JLabel( "   " );
        add( notification );
        
        // Window: initialize the window's main components and make it visible
        this.setSize( 450, 100 );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setVisible( true );
        this.videoDownloader = videoDownloader;
    }  
    
    /**
     * This inner class is used to listen to all button press events associated with the "Download" button.
     */
    private class ButtonEventListener implements ActionListener {
        
        /**
         * This method keeps track of the action that is being performed. When associated with the "Download" button,
         * it will use the VideoDownloader object to download the video with the given extension, as well as set the
         * new notification. It has no return values.
         * 
         * @param event The action event
         */ 
        @Override
        public void actionPerformed( ActionEvent event ) {
            
            // Upon "Download" button press: download video and display download success
            if ( event.getSource() == downloadButton ) {
                videoDownloader.formalDownload( getLinkField.getText(), ENCODE_FORMATS[ formatBox.getSelectedIndex() ].substring( 2 ) );
                notification.setText( videoDownloader.getDownloadSuccess() ? USER_MESSAGE_SUCCESS : USER_MESSAGE_FAILURE );
            }
        }
    }
}
