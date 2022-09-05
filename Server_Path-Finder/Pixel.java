public class Pixel {
    public int red;
    public int blue;
    public int green;

    public Pixel()
    {
        red = blue = green = 0;
    }

    public Pixel(int red, int blue, int green)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public boolean equals(Object otherPixel)
    {
        if(!(otherPixel instanceof Pixel)) return false;

        Pixel pix = (Pixel) otherPixel;
        if(this.red == pix.red && this.green == pix.green && this.blue == pix.blue)
            return true;
        else 
            return false;
    }
    public int pixelValue()
    {//alpha is set to max
        return (255<<24) | (this.red<<16) | (this.green<<8) | this.blue;
    }
    @Override
    public String toString()
    {
        return String.valueOf(red) +" "+ String.valueOf(green) +" " + String.valueOf(blue);
    }
}