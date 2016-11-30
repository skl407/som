package io.flatmap.ml.som

import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.numerics._
import io.flatmap.ml.numerics._
import io.flatmap.ml.som.SelfOrganizingMap.Neuron

trait NeighborhoodKernel {

  def neighborhood(winner: Neuron): DenseMatrix[Double]

}

trait GaussianNeighborboodKernel extends NeighborhoodKernel {

  self: SelfOrganizingMap =>

  def gaussian(size: Int, mean: Int, sigma: Double): DenseVector[Double] = {
    val initDistributions = DenseVector.range(0, size) - mean
    val numerator = pow(initDistributions, 2).map(_.toDouble)
    val denominator = 2*math.Pi*sigma*sigma
    exp(-(numerator /:/ denominator))
  }

  override def neighborhood(winner: Neuron): DenseMatrix[Double] = {
    // vectors with gaussian distribution of neighborhood coefficients
    val gaussianX = gaussian(codeBook.cols, winner._1, sigma)
    val gaussianY = gaussian(codeBook.rows, winner._2, sigma)
    // return 2 dimensional gaussian dist. surface by creating the outer product
    outer(gaussianX, gaussianY)
  }

}