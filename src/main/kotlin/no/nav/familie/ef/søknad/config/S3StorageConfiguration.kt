package no.nav.familie.ef.søknad.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import java.net.URI

@ConfigurationProperties("s3")
@ConstructorBinding
data class S3StorageConfiguration(val uri: URI,
                                  val region: String,
                                  val brukernavn: String,
                                  val passord: String,
                                  val passordfrase: String,
                                  val maxMbVedlegg: Int) {

    @Bean
    fun s3(endpointConfiguration: AwsClientBuilder.EndpointConfiguration,
           credentialsProvider: AWSCredentialsProvider): AmazonS3 {

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentialsProvider)
                .enablePathStyleAccess()
                .build()
    }

    @Bean
    fun endpointConfiguration(): AwsClientBuilder.EndpointConfiguration {
        log.info("Initializing s3 endpoint configuration with endpoint {} and region {}", uri, region)
        return AwsClientBuilder.EndpointConfiguration(uri.toString(), region)
    }

    @Bean
    fun credentialsProvider(): AWSCredentialsProvider {
        val awsCredentials = BasicAWSCredentials(brukernavn, passord)
        return AWSStaticCredentialsProvider(awsCredentials)
    }

    companion object {

        private val log = LoggerFactory.getLogger(S3StorageConfiguration::class.java)
    }

}
