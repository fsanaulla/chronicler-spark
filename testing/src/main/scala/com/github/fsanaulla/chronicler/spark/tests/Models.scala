package com.github.fsanaulla.chronicler.spark.tests

import com.github.fsanaulla.chronicler.core.model.InfluxWriter
import com.github.fsanaulla.chronicler.macros.Influx
import com.github.fsanaulla.chronicler.macros.annotations.{field, tag}
import com.github.fsanaulla.scalacheck.Arb
import org.scalacheck.{Arbitrary, Gen}

object Models {

  final case class Entity(@tag name: String, @field surname: String)

  object Entity {
    implicit val srtArb: Arbitrary[String] = Arbitrary(Gen.alphaStr.filter(_.nonEmpty))

    val entityArb: Arbitrary[Entity] = Arb.dummy[Entity]
    val wr: InfluxWriter[Entity] = Influx.writer[Entity]

    def samples(count: Int = 20): Seq[Entity] = {

      def samplesRec(samples: Seq[Entity], acc: Int): Seq[Entity] = {
        if (acc >= count) samples
        else {
          entityArb.arbitrary.sample match {
            case Some(e) => samplesRec(samples :+ e, acc + 1)
            case _ => samplesRec(samples, acc)
          }
        }
      }

      samplesRec(Nil, 0)
    }
  }
}
