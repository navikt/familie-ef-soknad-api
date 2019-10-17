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

@ConfigurationProperties(prefix = "s3")
@ConstructorBinding
data class S3StorageConfiguration(val endpoint: String,
                                  val region: String,
                                  val username: String,
                                  val password: String,
                                  val passphrase: String,
                                  val attachmentMazSize: Int) {

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
        log.info("Initializing s3 endpoint configuration with endpoint {} and region {}", endpoint, region)
        return AwsClientBuilder.EndpointConfiguration(endpoint, region)
    }

    @Bean
    fun credentialsProvider(): AWSCredentialsProvider {
        val awsCredentials = BasicAWSCredentials(username, password)
        return AWSStaticCredentialsProvider(awsCredentials)
    }

    companion object {

        private val log = LoggerFactory.getLogger(S3StorageConfiguration::class.java)
    }

}
