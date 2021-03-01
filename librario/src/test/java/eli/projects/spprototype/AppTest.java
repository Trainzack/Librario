package eli.projects.spprototype;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }
    
    // TODO Figure out how this works
    /**
     * Instrument.initInstruments();
    	
    	Piece p = new Piece("files/In The Stone");
    	
        
        File file = new File("Bells.pdf");
        try {
        	PDDocument doc = PDDocument.load(file);
        	
        	PDFRenderer renderer = new PDFRenderer(doc);
        	
        	BufferedImage image = renderer.renderImage(0, 10);
        	
        	ImageIO.write(image, "JPEG", new File("image.jpg"));
        	
        	doc.close();
			
		} catch (InvalidPasswordException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


        System.out.println( "Let's PDF this bitch motherfuckers!" );
     */

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
