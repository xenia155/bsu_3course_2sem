package by.quantumquartet.quanthink.math;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Matrix {
    private int rows;
    private int cols;
    private double[][] data;

    @JsonCreator
    public Matrix(@JsonProperty("rows") int rows,
                  @JsonProperty("cols") int cols,
                  @JsonProperty("data") double[][] data) {
        setRows(rows);
        setCols(cols);
        setData(data);
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int[] getSize() {
        return new int[]{ this.rows, this.cols };
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public double[][] getData() {
        return this.data;
    }

    public double getElememnt(int row, int column){
        return this.data[row][column];
    }

    public void setElememnt(double value, int row, int column){
        this.data[row][column] = value;
    }

    public double[] getRow(int i){
        if (i < 0 || i >= this.rows)
            throw new IndexOutOfBoundsException("No such row. Index is out of bounds");
        return this.data[i];
    }

    public double[] getColumn(int j){
        if (j < 0 || j >= this.cols)
            throw new IndexOutOfBoundsException("No such column. Index is out of bounds");
        double[] column = new double[this.rows];
        for (int i = 0; i < this.rows; i++)
            column[i] = this.data[i][j];
        return column;
    }
}

