import java.io.*;
import java.util.*;

public class AsciiSolver extends MazeSolver<Character>{
   
    public AsciiSolver(String filename, Character blank, Character wall, Character start, Character end) {
        super(filename, blank, wall, start, end);

        tileMap = ReadMazeFile();
        decimalMap = GetDecimalMaze();
		SolveMaze();
		WriteMazeFile();
    } 
    
    @Override
    public void WriteMazeFile()
    {
        String dataOut = "";
		if(exitPath != null) 
		{		
			for(int i=0;i<decimalMap.length;i++)
			{
				for(int j=0;j<decimalMap.length;j++)
				{
					switch(decimalMap[i][j])
					{
					case 0:
						dataOut+=blank;
						break;
					case 1:
						dataOut+=wall;
						break;
					case 2:
						if(blank != '*' && wall != '*') dataOut+='*';
						else dataOut+='@';
						break;
					}
				}
				dataOut+="\n";
			}
		}
		else
		{
			for(int i=0;i<decimalMap.length;i++)
			{
				for(int j=0;j<decimalMap.length;j++)
				{
					dataOut+=".x_x.";
				}
				dataOut+="\n";
			}
		}
			
			try {
				PrintWriter mazeWrite = new PrintWriter("SolvedMaze.tek");
				//mazeWrite.print("");
				mazeWrite.print(dataOut.substring(1));	
				mazeWrite.close();
				System.out.println("Successfully wrote to the file.");
			
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		}			
	
    }

    @Override
    public Character[][] ReadMazeFile()
	{//citim un fisier text
        try
        {
            File file = new File(filename);
		
            Scanner reader = new Scanner(file);
		
            String data="";

            List<List<Character>> matrix = new ArrayList<>();
            List<Character> row = new ArrayList<>();
            
            while(reader.hasNextLine())
            {
                data = reader.nextLine(); 
                
                for(char element: data.toCharArray())
                {
                    row.add(element);
                    
                }
                matrix.add(row);
                row = new ArrayList<>();
            }
            
            reader.close();
            
            //convertim List<List<Character>> in Character[][];
            System.out.println(matrix.size());//+ " " +matrix.get(0).size());
			//   Character[][] maze = matrix.stream().map(l -> l.stream().mapToInt(Character::charValue).toArray()).toArray(Character[][]::new);
			Character[][] maze = new Character[matrix.size()][matrix.get(0).size()];	
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
                    if(maze[i][j] == start)
                    {
                        Point.start = new Point(j, i, null);
                        maze[i][j] = blank;
                    }
                    if(maze[i][j] == end)
                    {
                        Point.end = new Point(j, i, null);
                        maze[i][j] = blank;
                    }
                }
            }       

            return maze;

            }	
            catch(FileNotFoundException e) 
            {
                //fereasca
                System.out.println("Eroare la citirea din fisier, nu ma intreba de ce");
                e.printStackTrace();
            }

         return null;
	}

	

}
