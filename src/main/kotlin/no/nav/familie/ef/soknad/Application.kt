package no.nav.familie.ef.soknad

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication(scanBasePackages = ["no.nav.familie.ef.soknad"])
@EnableSwagger2
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
