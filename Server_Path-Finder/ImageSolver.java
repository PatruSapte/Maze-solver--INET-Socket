import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageSolver extends MazeSolver<Pixel> {

    public ImageSolver(String filename, Pixel blank, Pixel wall, Pixel start, Pixel end) {
        super(filename, blank, wall, start, end);

        tileMap = ReadMazeFile();
        decimalMap = GetDecimalMaze();
		SolveMaze();
        WriteMazeFile();
    }
    
    @Override
    public void WriteMazeFile()
    {
        BufferedImage newImage;
        newImage = new BufferedImage(111,111, BufferedImage.TYPE_INT_ARGB);
        for(int i=0;i<decimalMap.length;i++)
        {
            for(int j=0;j<decimalMap.length;j++)
            {
                switch(decimalMap[i][j])
                {
					case 0:
						newImage.setRGB(j, i, blank.pixelValue());
						break;
					case 1:
                        newImage.setRGB(j, i, wall.pixelValue());
                        break;
					case 2:
                        newImage.setRGB(j, i, new Pixel(255, 0, 255).pixelValue());
						break;
                }
            } 
        }
        
        File wF = new File("SolvedImage.png");
        try {
            ImageIO.write(newImage, "png", wF);
            System.out.println("ImageSolver.java: Maze path saved in SolvedImage.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public Pixel[][] ReadMazeFile() 
    {   
        List<List<Pixel>> matrix = new ArrayList<>(); 
        List<Pixel> row = new ArrayList<>();

        //deschidem imaginea
        File file = new File(filename);
        try {//citim pixelii imaginii
            BufferedImage image = ImageIO.read(file);
            for(int x = 0; x < image.getWidth(); x++)
            {
                for(int y = 0; y < image.getHeight(); y++)
                {
                    int clr = image.getRGB(x, y);
                    int red = (clr & 0x00ff0000) >> 16;
                    int green = (clr & 0x0000ff00) >> 8;
                    int blue = clr & 0x000000ff;
                    row.add(new Pixel(red, blue, green));          
                }
                matrix.add(row);
                row = new ArrayList<>();
            }

            //Pixel[][] maze = pixelMaze.stream().map(l -> l.stream().mapToInt(null).toArray()).toArray(Pixel[][]::new);
            
            Pixel[][] maze = new Pixel[matrix.size()][matrix.get(0).size()];	
            for(int i = 0; i < matrix.size(); i++)
            {
                for(int j = 0; j < matrix.get(0).size(); j++)
                {
                    maze[i][j] = matrix.get(i).get(j);
                }
            }


            //pregatim punctele de start si end
            for(int i=0;i<maze.length;i++)
            {
                for(int j=0;j<maze.length;j++)
                {//inversam i si j in new Point() pentru a trata "harta" in sys. de coord. XoY(Y dir sus->jos, x stanga->dreapta)
                    if(maze[i][j].equals(start))
                    {
                        Point.start = new Point(j, i, null);
                        maze[i][j] = blank;
                    }
                    if(maze[i][j].equals(end))
                    {
                        Point.end = new Point(j, i, null);
                        maze[i][j] = blank;
                    }
                }
            }       

            return maze;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    
}

