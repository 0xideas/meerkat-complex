package ada.core.components.contextmodels

import breeze.linalg._
import breeze.numerics._
import breeze.stats.distributions.{Gaussian, MultivariateGaussian}
import smile.regression.{OnlineRegression, LinearModel}
import io.circe.Json
import scala.language.implicitConversions

abstract class BayesianLinearRegressionAbstract(nfeatures: Int, alpha: Double, private var beta: Double)
    extends OnlineRegression[Array[Double]]{
    private var mean = DenseVector.zeros[Double](nfeatures)
    private var covInv = DenseMatrix.eye[Double](nfeatures).map(_/alpha)
    private var cov = DenseMatrix.zeros[Double](nfeatures, nfeatures)
    private var w_cov = DenseMatrix.eye[Double](nfeatures).map(_/alpha)

    implicit def toVector(array: Array[Double]): DenseVector[Double] = DenseVector(array:_*)

    def update(x: Array[Double], y: Double): Unit = {
        val xvec = toVector(x)
        val outer = (xvec * xvec.t)
        val covInvT = covInv + outer.map(_ * beta)
        cov = inv(covInvT)
        mean = cov * ((covInv * mean) + (xvec.map(_ * beta * y)))
        covInv = covInvT

        //w_cov = cov
        w_cov = inv(covInv)
    }

    def predictProb(x: Array[Double]): Gaussian = {
        val xvec = toVector(x)
        val y_pred_mean = xvec.t * mean

        //val w_cov = inv(covInv)
        val y_pred_var = (1/ beta) + (xvec.t * w_cov * xvec)
        new Gaussian(y_pred_mean, y_pred_var)
    }

    def weights: MultivariateGaussian = {
        MultivariateGaussian(mean, cov)
    }

    def setMean(mean: DenseVector[Double]): Unit = {
        require(mean.length == this.mean.length)
        this.mean = mean
    }

    def setCovInv(covInv: DenseMatrix[Double]): Unit = {
        require(covInv.size == this.covInv.size)
        this.covInv = covInv
    }

    def set(mean: DenseVector[Double], covInv: DenseMatrix[Double]): Unit = {
        setMean(mean)
        setCovInv(covInv)
    }

    def export: Json = Json.fromFields(Map(
        "beta" -> Json.fromDouble(beta).get,
        "mean" -> Json.fromValues(mean.map(a => Json.fromDouble(a).get).toArray),
        "covInv" -> Json.fromValues(mean.map(a => Json.fromDouble(a).get).toArray)
    ))

    def changeBeta(increment: Double = 0.0, factor: Double = 1.0, max: Double = 5000.0): Unit = {
        beta = math.min((beta+increment)*factor, max)
    }
}


class BayesianSampleLinearRegression(val nfeatures: Int, val alpha: Double, val beta: Double)
    extends BayesianLinearRegressionAbstract(nfeatures, alpha, beta){
    def predict(x: Array[Double]): Double = predictProb(x).sample
}

//basically identical to point estimate linear regression - not used at the moment
class BayesianMeanLinearRegression(val nfeatures: Int, val alpha: Double, val beta: Double)
    extends BayesianLinearRegressionAbstract(nfeatures, alpha, beta){
    def predict(x: Array[Double]): Double = predictProb(x).mean
}




