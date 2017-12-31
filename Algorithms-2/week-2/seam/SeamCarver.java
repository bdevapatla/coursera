import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

	// private final Picture picture;
	
	private int[][] color;
	private int H, W;
	

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null) {
			throw new java.lang.IllegalArgumentException("Picture arguemnt to constructor cannot be NULL");
		}
		this.H = picture.height();
		this.W = picture.width();
		this.createColorGrid(picture);
	}

	

	private void createColorGrid(Picture picture) {
		this.color = new int[this.W][this.H];
		for (int row = 0; row < this.H; row++) {
			for (int col = 0; col < this.W; col++) {
				this.color[col][row] = picture.getRGB(col, row);
			}
		}
	}

	// current picture
	public Picture picture() {
		// return this.picture;
		Picture picture = new Picture(this.W, this.H);
		for (int row = 0; row < this.H; row++) {
			for (int col = 0; col < this.W; col++) {
				picture.set(col, row, new Color(this.color[col][row]));
			}
		}
		return picture;
	}

	// width of current picture
	public int width() {
		return this.W;
	}

	// height of current picture
	public int height() {
		return this.H;
	}

	// energy of pixel at column x and row y
	// dual-gradient energy function
	public double energy(int x, int y) {
		if (!(x >= 0 && x < this.W && y >= 0 && y < this.H)) {
			throw new java.lang.IllegalArgumentException("Invalid coordinates to energy function");
		}

		// If border pixel then energy to 1000 - strictly greater than the
		// energy of interior pixel
		// TODO: Analyze why - think this has something to do with RGB values
		// between 0 and 255

		

		if (x == 0 || x == this.W - 1 || y == 0 || y == this.H - 1) {
			return 1000.0;
		}

		double energy = 0.0, energyXSquare = 0.0, energyYSquare = 0.0;
		Color colorPlus1, colorMinus1;

		// Calculate X-Square first

		colorPlus1 = new Color(this.color[x + 1][y]);
		colorMinus1 = new Color(this.color[x - 1][y]);

		energyXSquare = (colorPlus1.getRed() - colorMinus1.getRed()) * (colorPlus1.getRed() - colorMinus1.getRed())
				+ (colorPlus1.getGreen() - colorMinus1.getGreen()) * (colorPlus1.getGreen() - colorMinus1.getGreen())
				+ (colorPlus1.getBlue() - colorMinus1.getBlue()) * (colorPlus1.getBlue() - colorMinus1.getBlue());

		colorPlus1 = new Color(this.color[x][y + 1]);
		colorMinus1 = new Color(this.color[x][y - 1]);

		energyYSquare = (colorPlus1.getRed() - colorMinus1.getRed()) * (colorPlus1.getRed() - colorMinus1.getRed())
				+ (colorPlus1.getGreen() - colorMinus1.getGreen()) * (colorPlus1.getGreen() - colorMinus1.getGreen())
				+ (colorPlus1.getBlue() - colorMinus1.getBlue()) * (colorPlus1.getBlue() - colorMinus1.getBlue());

		energy = Math.sqrt(energyXSquare + energyYSquare);
		return energy;

	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		double[][] distTo = new double[this.W][this.H];
		int[][] seam = new int[this.W][this.H];
		int[] horizSeam = new int[this.W];
		int lastRowBestY = 0;		
		for (int col = 0; col < this.W; col++) {
			double lastColBestEnergy = Double.NaN;
			for (int row = 0; row < this.H; row++) {
				// Find how many way you can reach here first
				// Assign the shortest distance
				double pixelEnergy = 0;
				if (col == 0) {
					seam[col][row] = row;
				}
				// Using natural order of rows is very important here
				else if (col - 1 >= 0) {
					if (row - 1 >= 0) {
						pixelEnergy = distTo[col - 1][row - 1];
						seam[col][row] = row - 1;

						if (Double.compare(distTo[col-1][row], pixelEnergy) < 0 || (col == this.W - 1
								&& Double.compare(distTo[col-1][row], pixelEnergy) == 0)) {
							pixelEnergy = distTo[col-1][row];
							seam[col][row] = row;
						}

					} else if (row == 0) {
						pixelEnergy = distTo[col-1][row];
						seam[col][row] = row;
					}

					
					if (row + 1 < this.H && Double.compare(distTo[col - 1][row + 1], pixelEnergy) < 0) {
						pixelEnergy = distTo[col - 1][row + 1];
						seam[col][row] = row + 1;
					}
				}

				pixelEnergy = pixelEnergy + this.energy(col,row);
				distTo[col][row] = pixelEnergy;
				if (col == this.W - 1) {
					if (Double.isNaN(lastColBestEnergy) || Double.compare(pixelEnergy, lastColBestEnergy) < 0) {
						lastColBestEnergy = pixelEnergy;
						lastRowBestY = row;
					}
				}
			}
		}

		// Now construct vert-seam
		for (int col = this.W - 1; col >= 0; col--) {
			if (col != this.W - 1) {
				lastRowBestY = seam[col+1][lastRowBestY];
			}
			horizSeam[col] = lastRowBestY;
		}
		return horizSeam;

	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		double[][] distTo = new double[this.W][this.H];
		int[][] seam = new int[this.W][this.H];
		int[] vertSeam = new int[this.H];
		int lastRowBestX = 0;
		for (int row = 0; row < this.H; row++) {
			double lastRowBestEnergy = Double.NaN;
			for (int col = 0; col < this.W; col++) {
				// Find how many way you can reach here first
				// Assign the shortest distance
				double pixelEnergy = 0;
				if (row == 0) {
					seam[col][row] = col;
				}
				// Using natural order of columns is very important here
				else if (row - 1 >= 0) {
					if (col - 1 >= 0) {
						pixelEnergy = distTo[col - 1][row - 1];
						seam[col][row] = col - 1;

						if (Double.compare(distTo[col][row - 1], pixelEnergy) < 0 || (row == this.height() - 1
								&& Double.compare(distTo[col][row - 1], pixelEnergy) == 0)) {
							pixelEnergy = distTo[col][row - 1];
							seam[col][row] = col;
						}

					} else if (col == 0) {
						pixelEnergy = distTo[col][row - 1];
						seam[col][row] = col;
					}

					if (col + 1 < this.W && Double.compare(distTo[col + 1][row - 1], pixelEnergy) < 0) {
						pixelEnergy = distTo[col + 1][row - 1];
						seam[col][row] = col + 1;
					}
				}

				pixelEnergy = pixelEnergy + this.energy(col,row);
				distTo[col][row] = pixelEnergy;
				if (row == this.H - 1) {
					if (Double.isNaN(lastRowBestEnergy) || Double.compare(pixelEnergy, lastRowBestEnergy) < 0) {
						lastRowBestEnergy = pixelEnergy;
						lastRowBestX = col;
					}
				}
			}
		}

		// Now construct vert-seam
		for (int row = this.H - 1; row >= 0; row--) {
			if (row != this.H - 1) {
				lastRowBestX = seam[lastRowBestX][row + 1];
			}
			vertSeam[row] = lastRowBestX;
		}
		return vertSeam;
	}

	private boolean isValidHorizontalSeam(int[] seam) {
		return true;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null || seam.length != this.width() || !isValidHorizontalSeam(seam)) {
			throw new java.lang.IllegalArgumentException("seam arguemnt to removeHorizontalSeam method cannot be NULL");
		}
		
		this.H--;
		int[][] temp = new int[this.W][this.H];

		for (int row = 0; row < this.H; row++) {
			for (int col = 0; col < this.W; col++) {
				if (row < seam[col]) {
					temp[col][row] = this.color[col][row];
				}
				else {
					temp[col][row] = this.color[col][row+1];
				}
			}
		}
		this.color = temp;
	}

	private boolean isValidVerticalSeam(int[] seam) {
		return true;
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null || seam.length != this.height() || !isValidVerticalSeam(seam)) {
			throw new java.lang.IllegalArgumentException("seam arguemnt to removeVerticalSeam method cannot be NULL");
		}
		
		this.W--;
		int[][] temp = new int[this.W][this.H];

		for (int row = 0; row < this.H; row++) {
			for (int col = 0; col < this.W; col++) {
				if (col < seam[row]) {
					temp[col][row] = this.color[col][row];
				}
				else {
					temp[col][row] = this.color[col+1][row];
				}
			}
		}		

		this.color = temp;	
	}

	public static void main(String[] args) {
		SeamCarver seamCarver = new SeamCarver(new Picture("C:\\Princeton\\Assignment22\\12x10.png"));
		int[] vertSeam = seamCarver.findVerticalSeam();
		System.out.println("Vertical seam::");
		for (int i = 0; i < vertSeam.length; i++) {
			System.out.println(vertSeam[i]);
		}
		
		

		seamCarver.removeVerticalSeam(vertSeam);
		seamCarver.picture().save("C:\\Princeton\\Assignment22\\6x5-temp-vert.png");
		
		int[] horizSeam = seamCarver.findHorizontalSeam();
		System.out.println("horiz seam::");
		for (int i = 0; i < horizSeam.length; i++) {
			System.out.println(horizSeam[i]);
		}
		
		seamCarver.removeHorizontalSeam(horizSeam);
		seamCarver.picture().save("C:\\Princeton\\Assignment22\\6x5-temp-vert-horiz.png");

	}

}
