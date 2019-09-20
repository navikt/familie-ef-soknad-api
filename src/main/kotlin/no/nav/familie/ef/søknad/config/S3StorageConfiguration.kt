package no.nav.familie.ef.søknad.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
//@Profile("!dev")
class S3StorageConfiguration(@Value("\${familie.ef.soknad.s3.endpoint}") val endpoint: String,
                             @Value("\${familie.ef.soknad.s3.region}") val region: String,
                             @Value("\${familie.ef.soknad.s3.username}") val username: String,
                             @Value("\${familie.ef.soknad.s3.password}") val password: String,
                             @Value("\${familie.ef.soknad.s3.passphrase}") val passphrase: String,
                             @Value("\${familie.ef.soknad.s3.attachmentMazSize}") val attachmentMazSize: Int) {

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
