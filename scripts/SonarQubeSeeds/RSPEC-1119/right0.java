
for (int row = 1; row < matrix.length; row++) {          // Compliant
  for (int col = 0; col < row; col++) {
    System.out.println(matrix[row][col]);                // Also prints 4, 7 and 8
  }
}
