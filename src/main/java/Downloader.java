/**
 * This class is used to download Instagram videos through extracting page source data of the given link, finding the
 * video source link, and using a stream to output the file to a local directory. This is mostly achieved through the
 * IO and Net libaries.
 * 
 * @author Patrick Jung
 * @version April 10, 2019
 */

import java.util.Random;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {
    
    // Instance variable
    private boolean downloadSuccess;
    
    /**
     * This method is used for the interface window to link with the video downloader. It downloads a video at the
     * given link and saves it with a randomized name and given file extension. It has no return values.
     * 
     * @param videoLink The video link, not to be confused with the video source link
     * @param fileExtension The file extension that is used when downloading the video
     */
    public void formalDownload( String videoLink, String fileExtension ) {
        
        // Calls upon a less formal method to download a video with a given link and extension
        downloadFromSource( getSourceURL( videoLink ), createFileName(), fileExtension );
    }
    
    /**
     * This accessor method is used to return the video downloader's download success.
     * 
     * @return boolean - If the most recent download was successful, true. Otherwise, false.
     */
    public boolean getDownloadSuccess() {
        
        // Returns the download success
        return downloadSuccess;
    }
    
    /**
     * This mutator method is used to set the video downloader's current download success with a given boolean value.
     * 
     * @param downloadSuccess The new download success boolean value
     */
    public void setDownloadSuccess( boolean downloadSuccess ) {
        
        // Sets the download success with a new value
        this.downloadSuccess = downloadSuccess;
    }
    
    /**
     * This helper method is used to download a video from a source, with a specific file name and extension. It has
     * no return values.
     * 
     * @param videoSourceLink The video source link, not to be confused with the video link
     * @param fileName The video's file name to be saved as
     * @param fileExtension The video's file extension to be saved as
     */
    private void downloadFromSource( String videoSourceLink, String fileName, String fileExtension ) {
        BufferedInputStream bufferedInputStream;
        FileOutputStream fileOutputStream;
        URL videoSourceURL;
        byte dataBuffer[];
        int byteCount;
        
        // This clause will catch any input-output errors when opening a stream from the video source link
        try {
            
            // Instantiate a video source URL, a buffered input stream, and a file output stream
            videoSourceURL = new URL( videoSourceLink );
            bufferedInputStream = new BufferedInputStream( videoSourceURL.openStream() );
            fileOutputStream = new FileOutputStream( String.format( "%s.%s", fileName, fileExtension ) );
            
            // Use a data buffer to effectively write a file through an output stream
            dataBuffer = new byte[1024];
            while ( ( byteCount = bufferedInputStream.read( dataBuffer, 0, 1024 ) ) != -1 ) {
                fileOutputStream.write( dataBuffer, 0, byteCount );
            }
            
            // If no exception has occurred throughout the entire process, the download is successful
            setDownloadSuccess( true );
        }
        catch ( IOException exception ) {
            
            // An IO exception has occurred throughout the process, meaning the download was not successful
            System.err.println( String.format( "Exception (%s) has occurred.", exception ) );
            setDownloadSuccess( false );
        }
    }
    
    /**
     * This helper method is used to retrieve a video source link from a normal video link, through narrowing down the
     * page source of the given video link until the video source link is found. It returns the video source link as
     * a result.
     * 
     * @param videoLink The video link that a Instagram user can retrieve without visiting the page's source
     *
     * @return String - The video source link, which cannot be visited directly by an Instagram user
     */
    private String getSourceURL( String videoLink ) {
        URL userInputLink;
        InputStreamReader streamReader;
        BufferedReader bufferedReader;
        String lineInPageSource;
        String linkInPageSource;
        
        // This clause will validate the given video link and open a stream to the link, to analyze the page source
        linkInPageSource = "";
        try {
            
            // Instantiate an input link, a stream reader, and a buffered reader
            userInputLink = new URL( videoLink );
            streamReader = new InputStreamReader( userInputLink.openStream() );
            bufferedReader = new BufferedReader( streamReader );

            // Read the page source line-by-line until an attribute is reached (which means the video is on the same line)
            while ( ( lineInPageSource = bufferedReader.readLine() ) != null ) {
                if ( lineInPageSource.contains( getFullAttribute( "meta property", "og:video" ) ) ) {
                    
                    // The video source link is between the line's 3rd and 4th quotation marks; break once found
                    int videoIndexInit = getSubstringIndex( 3, "\"", lineInPageSource ) + 1;
                    int videoIndexFinal = getSubstringIndex( 4, "\"", lineInPageSource );
                    
                    linkInPageSource = lineInPageSource.substring( videoIndexInit, videoIndexFinal );
                    break;
                }
            }
            
            // If no exception has occurred throughout the entire process, the download is successful
            bufferedReader.close();
            setDownloadSuccess( true );
        }
        catch ( MalformedURLException exception ) {
            
            // An malformed URL exception has occurred throughout the process, meaning the download was not successful
            System.err.println( String.format( "Exception (%s) has occurred.", exception ) );
            setDownloadSuccess( false );
        }
        catch ( IOException exception ) {
            
            // An IO exception has occurred throughout the process, meaning the download was not successful
            System.err.println( String.format( "Exception (%s) has occurred.", exception ) );
            setDownloadSuccess( false );
        }
        
        // Returns the link to the page source if possible, and an empty string otherwise
        return linkInPageSource;
    }
    
    /**
     * This helper method generates a random, fully alphabetical string of a certain character count. This string is
     * returned as a possible file name for the downloaded video. It takes no parameters.
     * 
     * @return String - The downloaded video's select file name
     */
    private String createFileName() {
        final String CHARACTER_POOL = "abcdefghijklmopqrstuvwxyz";
        final String FILE_NAME_STARTER = "IVD_";
        final int CHARACTER_COUNT = 12;
        
        Random generator;
        String fileName;
        int indexOfPool;
        
        // The file name is created with a file name starter, followed by CHARACTER_COUNT alphabetical characters
        fileName = FILE_NAME_STARTER;
        generator = new Random();
        
        for ( int characters = 0; characters < CHARACTER_COUNT; characters++ ) {
            
            // Generates a random index within the character pool, which is added onto the final file name string
            indexOfPool = ( int )( generator.nextFloat() * CHARACTER_POOL.length() );
            fileName += CHARACTER_POOL.charAt( indexOfPool );
        }
        
        // Returns the select file name for the downloaded video
        return fileName;
    }
    
    /**
     * This helper method generates a fully-formed HTML attribute, given the attribute's name and value.
     * 
     * @param attribute The attribute name
     * @param value The attribute's value
     *
     * @return String - The full HTML attribute
     */
    private String getFullAttribute( String attribute, String value ) {
        return String.format( "%s=\"%s\"", attribute, value );
    }
    
    /**
     * This helper method finds occurrence-th substring in a string and returns that substring's found index.
     * 
     * @param occurrence The occurrence number of the substring
     * @param substring The substring within the string
     * @param string The string that contains the substrings
     *
     * @return int - The index of the occurence-th substring within the string
     */
    private int getSubstringIndex( int occurrence, String substring, String string ) {
        int currentIndex;
        
        // Index starts at the beginning, and a substring is tested in every next character of the string repetitively
        currentIndex = -1;
        for ( int counter = 0; counter < occurrence; counter++ ) {
            currentIndex = string.indexOf( substring, currentIndex + 1 );
        }
        
        // Returns the index of the occurence-th substring within the string
        return currentIndex;
    }
}
