package config

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.{Duration, FiniteDuration}

object AppConfig {

  val config: Config = ConfigFactory.load()



}
