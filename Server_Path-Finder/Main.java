import java.io.File;
import java.io.IOException;

class Main{

    //args[0] == filename;
    public static void main(String args[])
    {
        String filename = args[0];
        if(MazeSolver.IsImage(filename))
        {
            Pixel wall = new Pixel(0,0,0);
            Pixel blank = new Pixel(255,255,255);
            Pixel start = new Pixel(255,0,0);
            Pixel end = new Pixel(0,0,255);
            ImageSolver solver = new ImageSolver(filename, blank, wall, start, end);
       	       try{
//delete ascii solved file if exist from older calls so that c server can guess the right file
       	    File cleaner = new File("SolvedMaze.tek");
       	    cleaner.delete();
       	    }
          
          catch(Exception e){e.printStackTrace();}
        }
        else 
        {
            Character wall = '#';
            Character blank = ' ';
            Character start = '^';
            Character end = '$';
            AsciiSolver solver = new AsciiSolver(filename, blank, wall, start, end);
      try{
//delete image solved file if exist from older calls so that c server can guess the right file
            File cleaner = new File("SolvedImage.png");
            cleaner.delete();
          }
          
          catch(Exception e){e.printStackTrace();}
        }

    }
}
