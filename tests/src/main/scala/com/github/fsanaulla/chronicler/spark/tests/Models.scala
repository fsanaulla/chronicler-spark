package com.github.fsanaulla.chronicler.spark.tests

import com.github.fsanaulla.chronicler.core.model.InfluxWriter
import com.github.fsanaulla.chronicler.macros.Macros
import com.github.fsanaulla.chronicler.macros.annotations.{field, tag}
import com.github.fsanaulla.scalacheck.Arb
import org.scalacheck.Arbitrary

object Models {

  final case class Entity(@tag name: String, @field surname: String)

  object Entity {
    val entityArb: Arbitrary[Entity] = Arb.of[Entity]
    val wr: InfluxWriter[Entity] = Macros.writer[Entity]

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
