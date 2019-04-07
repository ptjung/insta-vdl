/**
 * This class serves as an executable for the Instagram video downloader package.
 * 
 * @author Patrick Jung
 * @version April 7, 2019
 */

package InstagramVideoDownloader;

import InstagramVideoDownloader.InterfaceWindow;
import InstagramVideoDownloader.VideoDownloader;

public class Executable {
    /*
     * This is the program's main-line logic, which instantiates a video downloader and a user interface.
     */
    public static void main( String[] args ) {
        VideoDownloader videoDownloader = new VideoDownloader();
        InterfaceWindow userInterface = new InterfaceWindow( videoDownloader );
    }
}