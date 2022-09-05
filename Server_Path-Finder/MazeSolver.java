import java.util.ArrayList;
import java.util.List;

public abstract class MazeSolver<Tile> {
    public String filename="";
    public int[][] decimalMap;
    
    public Tile blank;
    public Tile wall;
    public Tile start;
    public Tile end;
    public Tile[][] tileMap;
    public List<Point> exitPath;
    public MazeSolver(String filename, Tile blank, Tile wall, Tile start, Tile end)
    {
        this.filename = filename;
        this.blank = blank;
        this.wall = wall;
        this.start = start;
        this.end = end;

    }
    public abstract Tile[][] ReadMazeFile(); 
    public abstract void WriteMazeFile();

    public void SolveMaze()
    {
        this.exitPath = FindPath();
        try{
        
            if(this.exitPath != null)
            {
                for (Point point : exitPath) 
                {
                    decimalMap[point.y][point.x] = 2;
                }
                
                decimalMap[Point.end.y][Point.end.x] = 2;
            }
        }
        catch(NullPointerException e){}

    }

    public int[][] GetDecimalMaze()
    {
        List<List<Integer>> matrix = new ArrayList<>();
        List<Integer> row = new ArrayList<>();
            
        for(Tile[] tileRow: tileMap)
        {
            for(Tile tile: tileRow)
            {
                if(tile.equals(blank)) row.add(0);
                else if(tile.equals(wall)) row.add(1);
                else if(tile.equals(start)) row.add(2);
                else if(tile.equals(end)) row.add(3);
                else System.out.println(tile.toString());

            }

            matrix.add(row);
            row = new ArrayList<>();
        }
        //convertim List<List<Integer>> in int[][];
        int[][] maze = matrix.stream().map(l -> l.stream().mapToInt(Integer::intValue).toArray()).toArray(int[][]::new);

        
        //pregatim punctele de start si end
        for(int i=0;i<maze.length;i++)
        {
            for(int j=0;j<maze[0].length;j++)
            {//inversam i si j in new Point() pentru a trata "harta" in sys. de coord. XoY(Y dir sus->jos, x stanga->dreapta)
                if(maze[i][j] == 2)
                {
                    Point.start = new Point(j, i, null);
                    maze[i][j] = 0;
                }
                if(maze[i][j] == 3)
                {
                    Point.end = new Point(j, i, null);
                    maze[i][j] = 0;
                }
                
            }
        }
        return maze;  
    }

    public static boolean IsImage(String name)
    {
        if(name.length() > 3)
        {
            if(name.substring(name.length()-4).equalsIgnoreCase(".png") || name.substring(name.length()-4) == " .jpg")
                return true;
        }
        return false;
    }

    public boolean IsWalkable(Point point) {
        if (point.y < 0 || point.y > decimalMap.length - 1) return false;
        if (point.x < 0 || point.x > decimalMap[0].length - 1) return false;
        return decimalMap[point.y][point.x] == 0;
    }

    public List<Point> FindNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();
        Point up = point.offset(0,  1);
        Point down = point.offset(0,  -1);
        Point left = point.offset(-1, 0);
        Point right = point.offset(1, 0);

        if (IsWalkable(up)) neighbors.add(up);
        if (IsWalkable(down)) neighbors.add(down);
        if (IsWalkable(left)) neighbors.add(left);
        if (IsWalkable(right)) neighbors.add(right);
        return neighbors;
    }

    public List<Point> FindPath() {
        boolean finished = false;
        List<Point> used = new ArrayList<>();
        used.add(Point.start);
        while (!finished) {
            List<Point> newOpen = new ArrayList<>();
            for(int i = 0; i < used.size(); ++i){
                Point point = used.get(i);
                for (Point neighbor : FindNeighbors(point)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for(Point point : newOpen) {
                used.add(point);
                if (Point.end.equals(point)) {
                    finished = true;
                    break;
                }
            }

            if (!finished && newOpen.isEmpty())
                return null;
        }

        List<Point> path = new ArrayList<>();
        Point point = used.get(used.size() - 1);
        while(point.previous != null) {
            path.add(0, point);
            point = point.previous;
        }
        return path;
    }
     
}